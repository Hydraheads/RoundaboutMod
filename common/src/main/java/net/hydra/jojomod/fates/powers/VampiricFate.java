package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.AccessFateFoodData;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class VampiricFate extends FatePowers {
    public VampiricFate(LivingEntity self) {
        super(self);
    }
    public VampiricFate() {
        super();
    }
    public static final byte BLOOD_SUCK = 27;
    @Override
    public void tick(){
    }

    @Override
    public void tickPower() {
        tickBloodSuck();
        super.tickPower();
    }

    public Entity bloodSuckingTarget = null;

    public void tickBloodSuck(){
        if (!this.self.level().isClientSide()) {

            if (self.isUsingItem()) {
                if (bloodSuckingTarget != null || this.getActivePower() == BLOOD_SUCK) {
                    bloodSuckingTarget = null;
                    xTryPower(PowerIndex.NONE, true);
                }
            }


            if (bloodSuckingTarget != null) {
                Entity TE = getTargetEntity(self, 3, 15);
                if (TE != null && MainUtil.canDrinkBloodFair(TE, self)
                        && self.hurtTime <= 0 && bloodSuckingTarget.is(TE)) {
                    if (TE instanceof LivingEntity LE) {
                        ((StandUser) LE).roundabout$setDazed((byte) 3);
                    }

                    if (self.tickCount % 2 == 0) {
                        double random = (Math.random() * 0.8) - 0.4;
                        double random2 = (Math.random() * 0.8) - 0.4;
                        double random3 = (Math.random() * 0.8) - 0.4;
                        SimpleParticleType particle = ModParticles.BLOOD;
                        if (MainUtil.hasBlueBlood(TE)) {
                            particle = ModParticles.BLUE_BLOOD;
                        }
                        ((ServerLevel) this.self.level()).sendParticles(particle, TE.getX() + random,
                                TE.getY() + TE.getEyeHeight() + random2, TE.getZ() + random3,
                                0,
                                (this.self.getX() - TE.getX()), (this.self.getY() - TE.getY() + TE.getEyeHeight()), (this.self.getZ() - TE.getZ()),
                                0.08);
                    }
                }
            }


            if (this.getActivePower() == BLOOD_SUCK){
                if (attackTimeDuring == 0 || attackTimeDuring == 5){
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_SUCK_EVENT, SoundSource.PLAYERS, 1F, 0.95F+(float)(Math.random()*0.1));

                }
                if (attackTimeDuring >= 20){
                     finishSucking();
                    bloodSuckingTarget = null;
                }
            }
        } else {
            if (bloodSuckingTarget != null) {
                Entity TE = getTargetEntity(self, 3, 15);
                if (TE != null && MainUtil.canDrinkBloodFair(TE, self)
                        && self.hurtTime <= 0 && bloodSuckingTarget.is(TE)) {
                    //safe
                } else {
                    xTryPower(PowerIndex.NONE,true);
                    C2SPacketUtil.cancelSuckingPacket();
                    bloodSuckingTarget = null;
                }
            }
            if (this.getActivePower() == BLOOD_SUCK) {
                if (attackTimeDuring >= 20) {
                    if (this.isPacketPlayer() && attackTimeDuring == 20) {
                        C2SPacketUtil.finishSuckingPacket();
                    }
                }
            }
        }
    }


    public void packetFinish(){
        if (this.getActivePower() == BLOOD_SUCK){
            finishSucking();
        }
    }
    public void packetCancel(){
        if (this.getActivePower() == BLOOD_SUCK){
            xTryPower(PowerIndex.NONE,true);
        }
        bloodSuckingTarget = null;
    }

    public void finishSucking(){
        if (bloodSuckingTarget != null && self instanceof Player pl) {

            boolean canDrainGood = MainUtil.canDrinkBloodCrit(bloodSuckingTarget,self);
            DamageSource sauce = ModDamageTypes.of(self.level(),
                    ModDamageTypes.BLOOD_DRAIN);
            if (bloodSuckingTarget.hurt(sauce, 4) && bloodSuckingTarget instanceof LivingEntity LE) {
                if (canDrainGood) {
                    if (pl.canEat(false) || ((AccessFateFoodData)pl.getFoodData()).rdbt$getRealSaturation() < 8) {
                        pl.getFoodData().eat(6, 1.0F);
                    } else {
                        pl.getFoodData().eat(6, 0.5F);
                    }
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_SUCK_DRAIN_EVENT, SoundSource.PLAYERS, 1F, 1.4F+(float)(Math.random()*0.1));
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1F, 1F+(float)(Math.random()*0.1));
                    int $$23 = (int)((double)2 * 0.5);
                    ((ServerLevel)this.self.level()).sendParticles(ParticleTypes.CRIT,
                            bloodSuckingTarget.getEyePosition().x,
                            bloodSuckingTarget.getEyePosition().y,
                            bloodSuckingTarget.getEyePosition().z,
                             15, 0.2, 0.2, 0.2, 0.0);

                } else {
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_SUCK_DRAIN_EVENT, SoundSource.PLAYERS, 1F, 1.4F+(float)(Math.random()*0.1));
                    pl.getFoodData().eat(2, 0.0F);
                }
                MainUtil.makeBleed(bloodSuckingTarget, 0, 200, null);
            }
            bloodSuckingTarget = null;
            xTryPower(PowerIndex.NONE, true);
        }
    }

    public void suckBlood(){
        if (!onCooldown(PowerIndex.FATE_2)) {
            Entity TE = getTargetEntity(self, 3, 15);
            if (TE != null && MainUtil.canDrinkBloodFair(TE, self)) {
                setActivePower(BLOOD_SUCK);
                self.setSprinting(false);
                tryIntPowerPacket(BLOOD_SUCK, TE.getId());
                bloodSuckingTarget = TE;
                this.attackTimeDuring = 0;
                this.setCooldown(PowerIndex.FATE_2, 60);
            }
        }
    }
    @Override
    public boolean tryPower(int move, boolean forced){
        if (activePower == BLOOD_SUCK && move != BLOOD_SUCK && !self.level().isClientSide()) {
            super.setPlayerPos2(PlayerPosIndex.NONE_2);
        }
        return super.tryPower(move, forced);
    }
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == BLOOD_SUCK) {
            bloodSuckingTarget = this.self.level().getEntity(chargeTime);
            setActivePower(BLOOD_SUCK);
            self.setSprinting(false);
            if (!self.level().isClientSide()) {
                super.setPlayerPos2(PlayerPosIndex.BLOOD_SUCK);
            }
            this.attackTimeDuring = 0;
            if (bloodSuckingTarget != null) {
                bloodSuckingTarget.setDeltaMovement(Vec3.ZERO);
            }
        }
        return super.tryIntPower(move, forced, chargeTime);
    }

    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        if (getActivePower() == BLOOD_SUCK){
            basis*=0.2F;
        }
        return basis;
    }
    @Override
    public float zoomMod(){
        if (getActivePower() == BLOOD_SUCK) {
            return 0.6f;
        }
        return 1;
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8 - 1;
        if (!standOn){
            Entity TE = getTargetEntity(playerEntity, 3, 15);
            if (TE != null && MainUtil.canDrinkBloodFair(TE, self)){

                if (getActivePower() == BLOOD_SUCK){
                    int test = (int) ((17F/20F) * Mth.clamp(this.attackTimeDuring,0,20));
                    context.blit(StandIcons.JOJO_ICONS, k, j, 192, 36, 17, 8);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 192, 44, 17-test, 8);
                } else {
                    context.blit(StandIcons.JOJO_ICONS, k, j, 192, 44, 17, 8);
                }

            }
        }
    }
    @Override
    public boolean cancelSprintJump(){
        return getActivePower() == BLOOD_SUCK;
    }
    @Override
    /**Cancel all sprinting*/
    public boolean cancelSprint(){
        return getActivePower() == BLOOD_SUCK;
    }
    @Override
    public boolean cancelSprintParticles(){
        return getActivePower() == BLOOD_SUCK;
    }


    /**This function grays out icons for moves you can't currently use. Slot is the icon slot from 1-4,
     * activeP is your currently active power*/
    public boolean isAttackIneptVisually(byte activeP, int slot){
        Entity TE = getUserData(self).roundabout$getStandPowers().getTargetEntity(this.self, 3, 15);
        if (slot == 2 && !MainUtil.canDrinkBloodFair(TE, self) && !isHoldingSneak())
            return true;
        return super.isAttackIneptVisually(activeP,slot);
    }

    public boolean isVisionOn(){
        return true;
    }
    @Override
    public ResourceLocation getIconYes(int slot){
        if ((slot == 2 || slot == 3) && isHoldingSneak()){
            return StandIcons.SQUARE_ICON_BLOOD;
        }
        return StandIcons.SQUARE_ICON;
    }
}
