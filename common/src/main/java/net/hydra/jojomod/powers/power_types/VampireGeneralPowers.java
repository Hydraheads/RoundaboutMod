package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class VampireGeneralPowers extends PunchingGeneralPowers {
    public VampireGeneralPowers(LivingEntity self) {
        super(self);
    }
    public VampireGeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new VampireGeneralPowers(entity);
    }

    /**The text name of the fate*/
    public Component getPowerName(){
        return Component.translatable("text.roundabout.powers.vampire");
    }
    public Component getPowerTagName(){
        return Component.translatable("text.roundabout.powers.vampire_select");
    }
    public int getMaxGuardPoints(){
        return 13;
    }

    @Override
    public void powerActivate(PowerContext context) {
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vp) {
            switch (context) {
                case SKILL_3_NORMAL -> {
                    vp.dashOrWallWalk();
                }
                case SKILL_3_CROUCH -> {
                    vp.dashOrWallWalk();
                }
            }
        }
    };


    public float getPunchStrength(Entity entity){
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            if (this.getReducedDamage(entity)){
                return 0.75F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            } else {
                return 2.1F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            }
        } else {
            return super.getPunchStrength(entity);
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vp) {
            if ((vp.canLatchOntoWall() || (vp.isPlantedInWall() && !isHoldingSneak())) && vp.canWallWalkConfig()) {
                setSkillIcon(context, x, y, 3, StandIcons.WALL_WALK_VAMP, PowerIndex.FATE_3);
            } else if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        }
    }
}
