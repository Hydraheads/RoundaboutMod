package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class StarPlatinumBaseballEntity extends StarPlatinumEntity{
    public StarPlatinumBaseballEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == 50) {
                this.baseballCharge.startIfStopped(this.tickCount);
            } else {
                this.baseballCharge.stop();
            }

            if (this.getAnimation() == 51) {
                this.baseballSwing.startIfStopped(this.tickCount);
            } else {
                this.baseballSwing.stop();
            }

            if (this.getAnimation() == 50 || this.getAnimation() == 51) {
                this.hideBat.stop();
            } else {
                this.hideBat.startIfStopped(this.tickCount);
            }
        }
    }
    public final AnimationState baseballCharge = new AnimationState();
    public final AnimationState baseballSwing = new AnimationState();
    public final AnimationState hideBat = new AnimationState();
}
