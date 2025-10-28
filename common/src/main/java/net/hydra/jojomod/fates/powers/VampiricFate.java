package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.FatePowers;
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

    @Override
    public void tick(){
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (!standOn){
            Entity TE = standUser.roundabout$getStandPowers().getTargetEntity(playerEntity, 3, 15);
            if (TE != null && MainUtil.canDrinkBlood(TE)){
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 44, 16, 8);

            }
        }
    }

    /**This function grays out icons for moves you can't currently use. Slot is the icon slot from 1-4,
     * activeP is your currently active power*/
    public boolean isAttackIneptVisually(byte activeP, int slot){
        Entity TE = getUserData(self).roundabout$getStandPowers().getTargetEntity(this.self, 3, 15);
        if (slot == 2 && !MainUtil.canDrinkBlood(TE) && !isHoldingSneak())
            return true;
        return super.isAttackIneptVisually(activeP,slot);
    }

    @Override
    public ResourceLocation getIconYes(int slot){
        if ((slot == 2 || slot == 3) && isHoldingSneak()){
            return StandIcons.SQUARE_ICON_BLOOD;
        }
        return StandIcons.SQUARE_ICON;
    }
}
