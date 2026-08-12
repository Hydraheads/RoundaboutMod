package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class ReaperKingCrimsonEntity extends KingCrimsonEntity{
    public ReaperKingCrimsonEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public final AnimationState hideScythe = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        byte anim = this.getAnimation();
        if (!(anim == FINAL_ATTACK_WINDUP ||
                anim == FINAL_1 ||
                anim == FINAL_2)) {
            this.hideScythe.startIfStopped(this.tickCount);
        } else {
            this.hideScythe.stop();
        }
    }
}
