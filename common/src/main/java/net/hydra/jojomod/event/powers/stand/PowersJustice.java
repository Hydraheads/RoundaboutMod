package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.presets.DashPreset;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PowersJustice extends DashPreset {
    public PowersJustice(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersJustice(entity);
    }
    @Override
    public boolean canSummonStand(){
        return true;
    }
    @Override
    public boolean interceptAttack(){
        return false;
    }
    @Override
    public boolean interceptGuard(){
        return false;
    }

    @Override
    public boolean isMiningStand() {
        return false;
    }

    @Override
    public void onStandSwitch(){
        if (this.getSelf() instanceof  Player PE){
            IPlayerEntity ipe = ((IPlayerEntity)PE);
            byte morph = ipe.roundabout$getShapeShift();
            if (!ShapeShifts.getShiftFromByte(morph).equals(ShapeShifts.PLAYER)){
                ipe.roundabout$shapeShift();
                ipe.roundabout$setShapeShift(ShapeShifts.PLAYER.id);
            }
        }
    }


    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public StandEntity getNewStandEntity(){
        if (((StandUser)this.getSelf()).roundabout$getStandSkin() == JusticeEntity.PIRATE){
            return ModEntities.JUSTICE_PIRATE.create(this.getSelf().level());
        } else if (((StandUser)this.getSelf()).roundabout$getStandSkin() == JusticeEntity.DARK_MIRAGE){
            return ModEntities.DARK_MIRAGE.create(this.getSelf().level());
        }
        return ModEntities.JUSTICE.create(this.getSelf().level());
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        byte bt = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            if (bt == JusticeEntity.FLAMED || bt == JusticeEntity.BLUE_FLAMED){
                return ModSounds.SUMMON_JUSTICE_2_EVENT;
            }
            return ModSounds.SUMMON_JUSTICE_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }


    @Override
    public void playSummonEffects(boolean forced){
        if (!forced) {
            Level lv = this.getSelf().level();
            if ((this.getSelf()) instanceof Player PE && lv.getBiome(this.getSelf().getOnPos()).is(Biomes.BASALT_DELTAS)){
                StandUser user = ((StandUser)PE);
                ItemStack stack = user.roundabout$getStandDisc();
                if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_JUSTICE)){
                    IPlayerEntity ipe = ((IPlayerEntity) PE);
                    if (!ipe.roundabout$getUnlockedBonusSkin()){
                        if (!lv.isClientSide()) {
                            ipe.roundabout$setUnlockedBonusSkin(true);
                            lv.playSound(null, PE.getX(), PE.getY(),
                                    PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                            ((ServerLevel) lv).sendParticles(ParticleTypes.END_ROD, PE.getX(),
                                    PE.getY()+PE.getEyeHeight(), PE.getZ(),
                                    10, 0.5, 0.5, 0.5, 0.2);
                            user.roundabout$setStandSkin(JusticeEntity.FLAMED);
                            ((ServerPlayer) ipe).displayClientMessage(
                                    Component.translatable("unlock_skin.roundabout.justice.bad_bone"), true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getDisplayPowerInventoryScale(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == JusticeEntity.DARK_MIRAGE){
            return super.getDisplayPowerInventoryScale();
        }
        return 14;
    }
    @Override
    public int getDisplayPowerInventoryYOffset(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == JusticeEntity.DARK_MIRAGE){
            return super.getDisplayPowerInventoryYOffset();
        }
        return -7;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 1, StandIcons.JUSTICE_DISGUISE, PowerIndex.SKILL_1_SNEAK);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.JUSTICE_CAST_FOG, PowerIndex.NO_CD);

        }

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.SKILL_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.JUSTICE_FOG_CHAIN, PowerIndex.SKILL_2);

        }

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.NONE, PowerIndex.NONE);
        } else {
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        }

            setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.SKILL_4);
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.fog_sword",
                "instruction.roundabout.passive", StandIcons.JUSTICE_FOG_SWORD, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.cast_fog",
                "instruction.roundabout.press_skill", StandIcons.JUSTICE_CAST_FOG,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.fog_morph",
                "instruction.roundabout.press_skill_crouch", StandIcons.JUSTICE_DISGUISE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.fog_chain",
                "instruction.roundabout.press_skill", StandIcons.JUSTICE_FOG_CHAIN,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        return $$1;
    }
    @Override
    public float getBonusAttackSpeed() {
        return 1.3F;
    }

    @Override
    public float getBonusPassiveMiningSpeed(){
        return 1.3F;
    }

    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(TheWorldEntity.PART_3_SKIN);
        $$1.add(JusticeEntity.SKELETON_SKIN);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(JusticeEntity.MANGA_SKIN);
            } if (Level > 2 || bypass){
                $$1.add(JusticeEntity.OVA_SKIN);
            } if (Level > 3 || bypass){
                $$1.add(JusticeEntity.STRAY_SKIN);
                $$1.add(JusticeEntity.BOGGED);
            } if (Level > 4 || bypass){
                $$1.add(JusticeEntity.WITHER);
                $$1.add(JusticeEntity.TWILIGHT);
            } if (Level > 5 || bypass){
                $$1.add(JusticeEntity.TAROT);
                $$1.add(JusticeEntity.DARK_MIRAGE);
            } if (Level > 6 || bypass){
                $$1.add(JusticeEntity.PIRATE);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(JusticeEntity.FLAMED);
                $$1.add(JusticeEntity.BLUE_FLAMED);
            }
        }
        return $$1;
    }

    public boolean hold1 = false;
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
            if (this.getSelf().level().isClientSide) {
                if (!isHoldingSneak()) {
                    if (keyIsDown) {
                        if (!hold1) {
                            hold1 = true;
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1);
                        }
                    } else {
                        hold1 = false;
                    }
                } else {
                    if (keyIsDown) {
                        if (!hold1) {
                            if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)){
                                hold1 = true;
                                ClientUtil.setJusticeScreen();
                            }
                        }
                    } else {

                        hold1 = false;
                    }
                }
            }
        super.buttonInput1(keyIsDown, options);
    }

    public BlockPos bpos;
    public boolean hold2 = false;
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (!isHoldingSneak()) {
                if (keyIsDown) {
                    if (!hold2) {
                        hold2 = true;
                        if (!this.onCooldown(PowerIndex.SKILL_2)) {
                            Vec3 vec3d = this.self.getEyePosition(0);
                            Vec3 vec3d2 = this.self.getViewVector(0);
                            Vec3 vec3d3 = vec3d.add(vec3d2.x * 100, vec3d2.y * 100, vec3d2.z * 100);
                            BlockHitResult blockHit = this.self.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.self));
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                            ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_2,blockHit.getBlockPos());
                        }
                    }
                } else {
                    hold2 = false;
                }
            }
        }
    }


    public boolean tryPosPower(int move, boolean forced, BlockPos blockPos){
        this.bpos = blockPos;
        return tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_1) {
            return this.castFog();
        } else if (move == PowerIndex.POWER_2) {
            return this.yankChain();
        }
        return super.setPowerOther(move,lastMove);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        if ((slot == 2 || slot == 4) && !this.isHoldingSneak()){
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (!icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                return true;
            }
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    public boolean yankChain(){
        if (!this.getSelf().level().isClientSide()) {
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                List<Entity> entities = DamageHandler.genHitbox(this.self, this.self.getX(), this.self.getY(),
                        this.self.getZ(), 50, 50, 50);
                boolean success = false;
                for (Entity value : entities) {
                    if (!(!value.showVehicleHealth() || !value.isAttackable() || value.isInvulnerable() ||
                            !value.isAlive())) {
                        if (icast.roundabout$inPermaCastFogRange(value)) {
                            if (bpos != null){
                                if (value instanceof LivingEntity LE){
                                    if (LE.hasEffect(ModEffects.BLEED)){
                                        double random = (Math.random() * 1.2) - 0.6;
                                        double random2 = (Math.random() * 1.2) - 0.6;
                                        double random3 = (Math.random() * 1.2) - 0.6;
                                        Vec3 vector = new Vec3((bpos.getX() - LE.getX()),
                                                (bpos.getY()+2 - LE.getY()),
                                                (bpos.getZ() - LE.getZ())).normalize().scale(1.5F);
                                        ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.POOF, value.getX(),
                                                value.getY() + value.getEyeHeight(), value.getZ(),
                                                0,
                                                vector.x+random,
                                                vector.y+random2,
                                                vector.z+random3,
                                                0.15);

                                        LE.setDeltaMovement(LE.getDeltaMovement().add(vector.x,vector.y*0.55+0.2F,vector.z
                                        ));
                                        success = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (success) {
                    int cdr = 80;
                    ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()),
                            PowerIndex.SKILL_2, cdr);
                    this.setCooldown(PowerIndex.SKILL_2, cdr);
                    this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                            this.self.getZ(), ModSounds.INHALE_EVENT, this.self.getSoundSource(), 100.0F, 0.5F);
                }
            }
        }
        return true;
    }

    @Override
    public byte getPermaCastContext(){
        return PermanentZoneCastInstance.FOG_FIELD;
    }
    @Override
    public boolean canSeeThroughFog(){
        return true;
    }
    public boolean castFog(){
        if (!this.getSelf().level().isClientSide()) {
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (!icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                icast.roundabout$addPermaCaster(this.getSelf());
            } else {
                icast.roundabout$removePermaCastingEntity(this.getSelf());
            }
        }
        return true;
    }
}
