package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class MagiciansRedEntity extends StandEntity {
    public MagiciansRedEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.base");
        } else if (skinId == BLUE_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.blue");
        } else if (skinId == PURPLE_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.purple");
        } else if (skinId == GREEN_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.green");
        } else if (skinId == DREAD_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.dread");
        }
        return Component.translatable(  "skins.roundabout.magicians_red.base");
    }
    public final AnimationState finalKick = new AnimationState();
    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalKickWindup = new AnimationState();
    public static final byte
            PART_3_SKIN = 1,
            BLUE_SKIN = 2,
            PURPLE_SKIN = 3,
            GREEN_SKIN = 4,
            DREAD_SKIN = 5;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == 85) {
                this.finalKickWindup.startIfStopped(this.tickCount);
            } else {
                this.finalKickWindup.stop();
            }
            if (this.getAnimation() == 86) {
                this.finalKick.startIfStopped(this.tickCount);
            } else {
                this.finalKick.stop();
            }
            if (this.getAnimation() == 87) {
                this.finalPunch.startIfStopped(this.tickCount);
            } else {
                this.finalPunch.stop();
            }
        }
    }
}
