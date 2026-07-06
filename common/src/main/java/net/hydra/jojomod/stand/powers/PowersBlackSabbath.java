package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.item.FancyLighterItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class PowersBlackSabbath extends NewDashPreset {
    public PowersBlackSabbath(LivingEntity self) {
        super(self);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().blackSabbathSettings.enableBlackSabbath;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersBlackSabbath(entity);
    }

    public boolean dangerYappingOn(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }
    public boolean canSummonStandAsEntity(){
        return false;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.ANUBIS_EXP, PowerIndex.SKILL_1);
       // setSkillIcon(context, x, y, 2, StandIcons.MINING_YAP, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.METALLICA_HEAL, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH ->{
                if(!onCooldown(PowerIndex.SKILL_1)) {
                    openPolpoInventory();
                    this.setCooldown(PowerIndex.SKILL_1, 10);
                }
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                if(!onCooldown(PowerIndex.SKILL_4)) {
                    biteFingersClient();
                }
            }
        }
    }

    public int cooldownFinger = ClientNetworking.getAppropriateConfig().blackSabbathSettings.fingerBiteCooldown;

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_4 -> {
                if(this.getSelf().getHealth() > 1) {
                    return biteFingers(this.self);
                }
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    private void biteFingersClient(){
        if (!this.onCooldown(PowerIndex.SKILL_4) && !isAttackIneptVisually(PowerIndex.SKILL_4, 4)) {
            this.setCooldown(PowerIndex.SKILL_4, cooldownFinger);
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }


    private boolean biteFingers(LivingEntity ojiroSasame){
        if(this.self.isAlive()) {
            if (!isClient()) {
                if(ojiroSasame instanceof Player P && (!P.isCreative() || P.isSpectator())) {
                    ojiroSasame.hurt(ModDamageTypes.of(ojiroSasame.level(), DamageTypes.GENERIC_KILL), 1F);
                }
                ItemEntity $$4 = new ItemEntity(ojiroSasame.level(), ojiroSasame.getX(),
                        ojiroSasame.getY() + ojiroSasame.getBbHeight() - 0.10, ojiroSasame.getZ(),
                        ModItems.FANCY_LIGHTER.getDefaultInstance());
                if($$4.getItem().getItem() instanceof FancyLighterItem FI){
                    FI.setLighterOwner($$4.getItem(), this.getSelf().getId());
                }
                $$4.setPickUpDelay(0);
                $$4.setDeltaMovement(Vec3.ZERO);
                ojiroSasame.level().addFreshEntity($$4);
                ojiroSasame.level().playSound(null, ojiroSasame, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.85F, 1.0F);
            }
        }
         return true;
    }

    public void openPolpoInventory(){
        if(this.self instanceof Player player){
            if(player.level() != null) {
                this.self.playSound(SoundEvents.ENDER_CHEST_OPEN);
            }
        }
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect) {

    }
    @Override
    public void tickMobAI(LivingEntity attackTarget){

    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if(slot == 4 && this.getSelf().getHealth() <= 1) {
            return  true;
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public void tickPower() {
        super.tickPower();
    }


    @Override
    public void updateIntMove(int in) {

        super.updateIntMove(in);
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }

    public static final byte
            MANGA = 1,
            GOTHIC = 2;


    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                GOTHIC
        );
    }


    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case GOTHIC -> Component.translatable("skins.roundabout.hey_ya.gothic");
            default -> Component.translatable("skins.roundabout.hey_ya.manga");
        };
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    }
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.BLACK_SABBATH_SUMMON_EVENT;
            }

        }
        return super.getSoundFromByte(soundChoice);
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.danger_yap",
                "instruction.roundabout.press_skill", StandIcons.ANUBIS_EXP, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.mining_yap",
                "instruction.roundabout.press_skill", StandIcons.PLUNDER_SELECTION,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.yap_yap",
                "instruction.roundabout.press_skill", StandIcons.METALLICA_HEAL,4,level,bypass));
        return $$1;
    }

    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.LIGHT_PURPLE);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "14Kacper").withStyle(ChatFormatting.DARK_PURPLE);
    }
}