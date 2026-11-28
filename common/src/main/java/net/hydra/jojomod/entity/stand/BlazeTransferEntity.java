package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class BlazeTransferEntity extends ManhattanTransferEntity{
    public BlazeTransferEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public final AnimationState spinny_boi = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        spinny_boi.startIfStopped(this.tickCount);
    }
}
