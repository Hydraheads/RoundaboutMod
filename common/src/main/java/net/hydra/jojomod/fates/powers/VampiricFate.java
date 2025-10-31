package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

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
                } else {
                    bloodSuckingTarget = null;
                    xTryPower(PowerIndex.NONE,true);
                }
            }
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
                context.blit(StandIcons.JOJO_ICONS, k, j, 192, 44, 17, 8);

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
