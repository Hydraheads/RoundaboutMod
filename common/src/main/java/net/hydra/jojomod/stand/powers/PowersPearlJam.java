package net.hydra.jojomod.stand.powers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.PearlJamEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.compress.utils.Lists;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.*;

import static net.hydra.jojomod.event.index.FateTypes.*;

public class PowersPearlJam extends NewDashPreset {

    private ItemEntity foodEntity = null;
    private Player user = (Player)this.getSelf();

    private int palmReadTimeDuring = -1;
    private int ticksSinceLastRead = -1;

    public Map<LivingEntity, List<MobEffectInstance>> palmReadEffects = new HashMap<>();
    public List<LivingEntity> palmReadOrder = new ArrayList<>();
    public Map<LivingEntity, Integer> tickOffsets = new HashMap<>();


    public static final byte PALM_READ_SOUND = 123;

    public static final List<PowersPearlJam> INSTANCES = new ArrayList<>();

    public static List<PowersPearlJam> getInstances(){
        synchronized (INSTANCES){
            return Collections.unmodifiableList(new ArrayList<>(INSTANCES));
        }
    }

    public PowersPearlJam(LivingEntity self) {
        super(self);
        INSTANCES.add(this);
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().pearlJamSettings.enablePearlJam;
    }

    @Override
    public StandEntity getNewStandEntity() {return ModEntities.PEARL_JAM.create(this.getSelf().level());}
    @Override
    public boolean canSummonStand() {return true;}
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {return new PowersPearlJam(entity); }

    @Override
    public boolean isSecondaryStand() {return true;}

    @Override
    public void powerActivate(PowerContext context) {
        switch (context){
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                doFoodEnergize();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                doPalmReadClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.PEARL_JAM_FOOD_ENERGIZE, PowerIndex.POWER_1);
        setSkillIcon(context, x, y, 2, StandIcons.PEARL_JAM_PALM_READ, PowerIndex.POWER_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> Icons = Lists.newArrayList();
        Icons.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.food_energize",
                "instruction.roundabout.press_skill", StandIcons.PEARL_JAM_FOOD_ENERGIZE, 1, level, bypass));
        Icons.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.palm_read",
                "instruction.roundabout.press_skill", StandIcons.PEARL_JAM_PALM_READ,2,level,bypass));
        Icons.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        return Icons;
    }

    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;}

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if(soundChoice == SoundIndex.SUMMON_SOUND){
            return ModSounds.SUMMON_PEARL_JAM_EVENT;
        } else if (soundChoice == PALM_READ_SOUND) {
            return ModSounds.PEARL_JAM_PALM_READ_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    public void doPalmReadClient(){
        if(!this.onCooldown(PowerIndex.POWER_2)){
            if(this.activePower == PowerIndex.POWER_2){
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                tryPowerPacket(PowerIndex.POWER_2);
            }
        }
    }

    public boolean palmRead(){
        setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.POWER_2);
        playStandUserOnlySoundsIfNearby(PALM_READ_SOUND, 27, false,false);
        LivingEntity target = (LivingEntity) MainUtil.getTargetEntity(this.getSelf(), 3);
        if (target != null){
            if (palmReadEffects.isEmpty()){
                palmReadTimeDuring = 0;
            } else if (palmReadOrder.contains(target)){
                if (palmReadOrder.size() == 1) { palmReadTimeDuring = 0;}
                palmReadOrder.remove(target);
            }
            palmReadOrder.add(target);
            palmReadEffects.put(target, Collections.unmodifiableList(target.getActiveEffects().stream().toList()));
            this.getSelf().sendSystemMessage(Component.literal(String.valueOf(ticksSinceLastRead)));
            tickOffsets.put(target, (tickOffsets.containsKey(target) && tickOffsets.size() == 1) ? -1 : ticksSinceLastRead);
            ticksSinceLastRead = 0;
            if(!this.getSelf().level().isClientSide && target instanceof Player){
                Player p = (Player) target;
                if (!(isVampire(target) || isZombie(target))){
                    user.displayClientMessage(Component.translatable("text.roundabout.pearl_jam.palm_read_hunger", p.getName(), p.getFoodData().getFoodLevel()).withStyle(ChatFormatting.YELLOW), true);
                } else if (isVampire(target)){
                    user.displayClientMessage(Component.translatable("text.roundabout.pearl_jam.palm_read_hunger", p.getName(), "??").withStyle(ChatFormatting.DARK_RED), true);;
                } else if (isUndisguisedZombie(target)){
                    user.displayClientMessage(Component.translatable("text.roundabout.pearl_jam.palm_read_hunger", p.getName(), "??").withStyle(ChatFormatting.RED), true);
                } else if (!isUndisguisedZombie(target)) {
                    user.displayClientMessage(Component.translatable("text.roundabout.pearl_jam.palm_read_hunger", p.getName(), "20").withStyle(ChatFormatting.YELLOW), true);;
                }
            }
        }
        this.setCooldown(PowerIndex.POWER_2, ClientNetworking.getAppropriateConfig().pearlJamSettings.palmReadCooldown);
        return true;
    }

    public void renderEffectIcons (LivingEntity LE, PoseStack matrixStack, MultiBufferSource bufferSource){
        if (LE != null){
            Minecraft mc = Minecraft.getInstance();
            if (LE != this.getSelf() && this.getSelf() instanceof Player && this.getSelf().distanceToSqr(LE) <= 1024 && palmReadEffects.containsKey(LE) && this.palmReadEffects.get(LE) != null && !this.palmReadEffects.get(LE).isEmpty()){
                matrixStack.pushPose();
                float height = LE.getBbHeight() + 0.25F;
                matrixStack.translate(0, height, 0);
                matrixStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
                matrixStack.scale(-0.025F, -0.025F, 0.025F);

                RenderSystem.disableDepthTest();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

                List<MobEffectInstance> effectList = palmReadEffects.get(LE);

                if (effectList != null) {
                    for (int i = 0; i < effectList.size(); i++) {

                        MobEffect effect = effectList.get(i).getEffect();
                        float x = effectList.size() > 3 ? ((i % 3) * 20F - (3 * 20) / 2F) : (i * 20F - (effectList.size() * 20F) / 2F);
                        float y = -((int) (i / 3)) * 20F;
                        TextureAtlasSprite sprite = mc.getMobEffectTextures().get(effect);
                        VertexConsumer consumer = bufferSource.getBuffer(RenderType.text(sprite.atlasLocation()));
                        Matrix4f matrix = matrixStack.last().pose();
                        Matrix3f normal = matrixStack.last().normal();

                        consumer.vertex(matrix, x, y, 0)
                                .color(255, 255, 255, 255)
                                .uv(sprite.getU0(), sprite.getV1())
                                .uv2(15728880)
                                .normal(normal, 0, 0, -1)
                                .endVertex();

                        consumer.vertex(matrix, x + 18F, y, 0)
                                .color(255, 255, 255, 255)
                                .uv(sprite.getU1(), sprite.getV1())
                                .uv2(15728880)
                                .normal(normal, 0, 0, -1)
                                .endVertex();

                        consumer.vertex(matrix, x + 18F, y - 18F, 0)
                                .color(255, 255, 255, 255)
                                .uv(sprite.getU1(), sprite.getV0())
                                .uv2(15728880)
                                .normal(normal, 0, 0, -1)
                                .endVertex();

                        consumer.vertex(matrix, x, y - 18F, 0)
                                .color(255, 255, 255, 255)
                                .uv(sprite.getU0(), sprite.getV0())
                                .uv2(15728880)
                                .normal(normal, 0, 0, -1)
                                .endVertex();
                    }
                } else {
                    Roundabout.LOGGER.info("For some bizarre reason this list instance effect is null");
                }
                RenderSystem.enableDepthTest();
                matrixStack.popPose();
            }
        }
    }

    @Override
    public void tickPower() {
        if (palmReadTimeDuring != -1) { tickPalmRead();}
        super.tickPower();
    }

    public void tickPalmRead(){
        if (ticksSinceLastRead != -1){ ticksSinceLastRead++;}
        if (ticksSinceLastRead >= ClientNetworking.getAppropriateConfig().pearlJamSettings.palmReadEffectDuration){
            ticksSinceLastRead = -1;
        }
        if (palmReadEffects.isEmpty()){
            palmReadTimeDuring = -1;
        } else {
            palmReadTimeDuring++;
            LivingEntity target = palmReadOrder.get(0);
            int ticks = tickOffsets.get(target) == -1 ? 0 : (ClientNetworking.getAppropriateConfig().pearlJamSettings.palmReadEffectDuration - tickOffsets.get(target));
            if (palmReadTimeDuring + ticks >= ClientNetworking.getAppropriateConfig().pearlJamSettings.palmReadEffectDuration){
                palmReadEffects.remove(palmReadOrder.get(0));
                tickOffsets.remove(palmReadOrder.get(0));
                palmReadOrder.remove(0);
                palmReadTimeDuring = 0;
            }
        }
    }

    public void doFoodEnergize(){
        if(!this.onCooldown(PowerIndex.POWER_1) && foodEntity == null){
            if(this.activePower == PowerIndex.POWER_1){
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                tryPowerPacket(PowerIndex.POWER_1);
            }
        }
    }


    public boolean foodEnergize(){
        ItemStack itemStack = user.getItemInHand(InteractionHand.MAIN_HAND);
        if ((itemStack.isEdible() || itemStack.getItem().getName(itemStack).getString().equals("Water Bottle")) && !itemStack.getOrCreateTag().contains("pearljamfood")){
            this.setActivePower(PowerIndex.POWER_1);
            this.setCooldown(PowerIndex.POWER_1, ClientNetworking.getAppropriateConfig().pearlJamSettings.foodEnergizeCooldown);
            foodEntity = user.drop(itemStack, false, true);
            user.getInventory().removeItem(itemStack);
            foodEntity.setNoGravity(true);
            this.setAttackTimeDuring(0);
        };
        return true;
    }

    public void updateFoodEnergize(){
        if (this.attackTimeDuring >= 10 && foodEntity != null){
            ItemStack foodStack = foodEntity.getItem();
            foodStack.enchant(null, 0);
            foodEntity.setGlowingTag(true);
            foodStack.getTag().putBoolean("pearljamfood", true);
            foodEntity.setNoGravity(false);
            foodEntity = null;
            setAttackTimeDuring(-1);
        }
    }



/*
    public Entity getFoodEntity(){
        if (foodEntity != null){
            return (Entity) foodEntity;
        }
        return null;
    }

    @Override
    public boolean highlightsEntity(Entity ent, Player player) {
        if (foodEntity != null){
            if (ent instanceof ItemEntity){
                if (((ItemEntity) ent).getItem().getOrCreateTag().contains("pearljamfood")){
                    return true;
                }
            }
        }
        return super.highlightsEntity(ent, player);
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player) {
        if (foodEntity != null){
            if (ent instanceof ItemEntity){
                if (((ItemEntity) ent).getItem().getOrCreateTag().contains("pearljamfood")){
                    return 16776960;
                }
            }
        }
        return super.highlightsEntityColor(ent, player);
    }

 */

    @Override
    public void updateUniqueMoves() {
        if (this.activePower == PowerIndex.POWER_1){
            updateFoodEnergize();
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if(move == PowerIndex.POWER_2){
            return this.palmRead();
        } else if (move == PowerIndex.POWER_1) {
            return this.foodEnergize();
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public Component getSkinName(byte skinId) {return getSkinNameT(skinId);}

    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(PearlJamEntity.ANIME);
        $$1.add(PearlJamEntity.MANGA);
        return $$1;
    }

    public static Component getSkinNameT(byte skinId){
        if (skinId == PearlJamEntity.MANGA) {
            return Component.translatable("skins.roundabout.pearl_jam.manga");
        }
        return Component.translatable("skins.roundabout.pearl_jam.anime");
    }

    @Override
    public boolean isWip() {return true;}

    @Override
    public Component ifWipListDevStatus() {return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.RED);}

    @Override
    public Component ifWipListDev() {return Component.literal(  "Victor Bryan").withStyle(ChatFormatting.DARK_RED);}
}
