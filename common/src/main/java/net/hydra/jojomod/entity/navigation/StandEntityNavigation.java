package net.hydra.jojomod.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class StandEntityNavigation extends WallClimberNavigation {

    private boolean avoidFire = true;

    public void setAvoidFire(boolean value) {
        this.avoidFire = value;
    }

    private boolean shouldAvoidFire(BlockPathTypes blockPathTypes) {
        return blockPathTypes != BlockPathTypes.DANGER_FIRE && this.avoidFire;
    }

    public StandEntityNavigation(Mob $$0, Level $$1) {
        super($$0, $$1);
    }

    public boolean canCutCorner(BlockPathTypes blockPathTypes) {
        return this.shouldAvoidFire(blockPathTypes) && blockPathTypes != BlockPathTypes.DANGER_OTHER && blockPathTypes != BlockPathTypes.WALKABLE_DOOR;
    }

}
