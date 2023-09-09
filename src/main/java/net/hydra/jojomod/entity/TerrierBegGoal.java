/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.hydra.jojomod.entity;
import java.util.EnumSet;

import net.hydra.jojomod.entity.TerrierEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TerrierBegGoal
        extends Goal {
    private final TerrierEntity wolf;
    @Nullable
    private PlayerEntity begFrom;
    private final World world;
    private final float begDistance;
    private int timer;
    private final TargetPredicate validPlayerPredicate;

    public TerrierBegGoal(TerrierEntity wolf, float begDistance) {
        this.wolf = wolf;
        this.world = wolf.getWorld();
        this.begDistance = begDistance;
        this.validPlayerPredicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(begDistance);
        this.setControls(EnumSet.of(Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        this.begFrom = this.world.getClosestPlayer(this.validPlayerPredicate, this.wolf);
        if (this.begFrom == null) {
            return false;
        }
        return this.isAttractive(this.begFrom);
    }

    @Override
    public boolean shouldContinue() {
        if (!this.begFrom.isAlive()) {
            return false;
        }
        if (this.wolf.squaredDistanceTo(this.begFrom) > (double)(this.begDistance * this.begDistance)) {
            return false;
        }
        return this.timer > 0 && this.isAttractive(this.begFrom);
    }

    @Override
    public void start() {
        this.wolf.setBegging(true);
        this.timer = this.getTickCount(40 + this.wolf.getRandom().nextInt(40));
    }

    @Override
    public void stop() {
        this.wolf.setBegging(false);
        this.begFrom = null;
    }

    @Override
    public void tick() {
        this.wolf.getLookControl().lookAt(this.begFrom.getX(), this.begFrom.getEyeY(), this.begFrom.getZ(), 10.0f, this.wolf.getMaxLookPitchChange());
        --this.timer;
    }

    private boolean isAttractive(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (this.wolf.isTamed() && itemStack.isOf(Items.BONE)) {
                return true;
            }
            if (!this.wolf.isBreedingItem(itemStack)) continue;
            return true;
        }
        return false;
    }
}

