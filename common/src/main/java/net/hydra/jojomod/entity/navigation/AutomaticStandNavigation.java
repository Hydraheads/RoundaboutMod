package net.hydra.jojomod.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.*;

public class AutomaticStandNavigation extends GroundPathNavigation {
    public AutomaticStandNavigation(Mob $$0, Level $$1) {
        super($$0, $$1);
    }

    private boolean avoidLight;

    public void setAvoidLight(boolean $$0) {
        this.avoidLight = $$0;
    }

    @Override
    protected PathFinder createPathFinder(int $$0) {
        this.nodeEvaluator = new AvoidLightNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, $$0);
    }

    @Override
    public void setAvoidSun(boolean $$0) {
        super.setAvoidSun($$0);
    }

    private Level level() {
        return super.level;
    }
}
