package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class StarPlatinumBaseballEntity extends StarPlatinumEntity{
    public StarPlatinumBaseballEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            BASEBALL_CHARGE = 50,
            BASEBALL_SWING = 51;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == BASEBALL_CHARGE) {
                this.baseballCharge.startIfStopped(this.tickCount);
            } else {
                this.baseballCharge.stop();
            }

            if (this.getAnimation() == BASEBALL_SWING) {
                this.baseballSwing.startIfStopped(this.tickCount);
            } else {
                this.baseballSwing.stop();
            }

            if (this.getAnimation() == BASEBALL_CHARGE || this.getAnimation() == BASEBALL_SWING) {
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
