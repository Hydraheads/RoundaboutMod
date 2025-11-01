package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

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

            if (bloodSuckingTarget != null) {
                Entity TE = getTargetEntity(self, 3, 15);
                if (TE != null && MainUtil.canDrinkBloodFair(TE, self)
                        && self.hurtTime <= 0 && bloodSuckingTarget.is(TE)) {
                    if (TE instanceof LivingEntity LE){
                        ((StandUser)LE).roundabout$setDazed((byte)3);
                    }

                        double random = (Math.random() * 1.2) - 0.6;
                        double random2 = (Math.random() * 1.2) - 0.6;
                        double random3 = (Math.random() * 1.2) - 0.6;
                        SimpleParticleType particle = ModParticles.BLOOD;
                        if (MainUtil.hasBlueBlood(TE)){
                            particle = ModParticles.BLUE_BLOOD;
                        }
                        ((ServerLevel) this.self.level()).sendParticles(particle, TE.getX() + random,
                                TE.getY() + TE.getEyeHeight() + random2, TE.getZ() + random3,
                                0,
                                (this.self.getX() - TE.getX()), (this.self.getY() - TE.getY() + TE.getEyeHeight()), (this.self.getZ() - TE.getZ()),
                                0.08);

                } else {
                    bloodSuckingTarget = null;
                    xTryPower(PowerIndex.NONE,true);
                }
            }


            if (this.getActivePower() == BLOOD_SUCK){
                if (attackTimeDuring == 0 || attackTimeDuring == 5){
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_SUCK_EVENT, SoundSource.PLAYERS, 1F, 0.95F+(float)(Math.random()*0.1));

                }
                if (attackTimeDuring >= 20){
                    if (this.isPacketPlayer() && attackTimeDuring == 20){
                    } else {
                        if (!this.self.level().isClientSide()){
                            finishSucking();
                        }
                    }
                }
            }
        }
    }

    public void finishSucking(){
        if (bloodSuckingTarget != null) {
            bloodSuckingTarget = null;
            xTryPower(PowerIndex.NONE, true);
        }
    }

    public void suckBlood(){
        Entity TE = getTargetEntity(self, 3, 15);
        if (TE != null && MainUtil.canDrinkBloodFair(TE,self)){
            setActivePower(BLOOD_SUCK);
            tryIntPowerPacket(BLOOD_SUCK,TE.getId());
            this.attackTimeDuring = 0;
        }
    }
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == BLOOD_SUCK) {
            bloodSuckingTarget = this.self.level().getEntity(chargeTime);
            setActivePower(BLOOD_SUCK);
            this.attackTimeDuring = 0;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }

    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        if (getActivePower() == BLOOD_SUCK){
            basis*=0;
        }
        return basis;
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
