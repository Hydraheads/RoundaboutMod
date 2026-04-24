package net.hydra.jojomod.entity.zombie_minion;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Map;

public class AxolotlMinion extends BaseMinion  implements LerpingModel {
    public AxolotlMinion(EntityType<? extends AxolotlMinion> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new FallenZombieAttackGoal(this, 1.0, true));
        this.addBehaviourGoals();
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.1F, 0.5F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 20);
        this.setMaxUpStep(1.0F);
    }

    private final Map<String, Vector3f> modelRotationValues = Maps.newHashMap();
    @Override
    public Map<String, Vector3f> getModelRotationValues() {
        return this.modelRotationValues;
    }
    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean isPushedByFluid() {
        return false;
    }
    public void rehydrate() {
        int $$0 = this.getAirSupply() + 1800;
        this.setAirSupply(Math.min($$0, this.getMaxAirSupply()));
    }

    public int getMaxAirSupply() {
        return 6000;
    }

    protected void handleAirSupply(int $$0) {
        if (this.isAlive() && !this.isInWaterRainOrBubble()) {
            this.setAirSupply($$0 - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().dryOut(), 2.0F);
            }
        } else {
            this.setAirSupply(this.getMaxAirSupply());
        }

    }
    @Override
    public void baseTick() {
        int $$0 = this.getAirSupply();
        super.baseTick();
        if (!this.isNoAi()) {
            this.handleAirSupply($$0);
        }
    }
    @Override
    public double getMeleeAttackRangeSqr(LivingEntity $$0) {
        return (double)1.5F + (double)$$0.getBbWidth() * (double)2.0F;
    }
    @Override
    protected float getStandingEyeHeight(Pose $$0, EntityDimensions $$1) {
        return $$1.height * 0.655F;
    }

    public int getMaxHeadXRot() {
        return 1;
    }

    public int getMaxHeadYRot() {
        return 1;
    }

    @Override
    public void travel(Vec3 $$0) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), $$0);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel($$0);
        }

    }

}
