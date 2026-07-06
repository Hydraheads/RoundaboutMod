/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.hydra.jojomod.entity.mobs.TerrierEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class StrayCatBegGoal
        extends Goal {
    private final StrayCatEntity stray;
    @Nullable
    private Player begFrom;
    private final Level world;
    private final float begDistance;
    private int timer;
    private final TargetingConditions validPlayerPredicate;

    public StrayCatBegGoal(StrayCatEntity stray, float begDistance) {
        this.stray = stray;
        this.world = stray.level();
        this.begDistance = begDistance;
        this.validPlayerPredicate = TargetingConditions.forNonCombat().range(begDistance);
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.begFrom = this.world.getNearestPlayer(this.validPlayerPredicate, this.stray);
        if (this.begFrom == null) {
            return false;
        }
        return this.isAttractive(this.begFrom);
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.begFrom.isAlive()) {
            return false;
        }
        if (this.stray.distanceToSqr(this.begFrom) > (double)(this.begDistance * this.begDistance)) {
            return false;
        }
        return this.timer > 0 && this.isAttractive(this.begFrom);
    }

    @Override
    public void start() {
        //this.stray.setIsInterested(true);
        this.timer = this.adjustedTickDelay(40 + this.stray.getRandom().nextInt(40));
    }

    @Override
    public void stop() {
        //this.stray.setIsInterested(false);
        this.begFrom = null;
    }

    @Override
    public void tick() {
        //this.stray.getLookControl().setLookAt(this.begFrom.getX(), this.begFrom.getEyeY(), this.begFrom.getZ(), 10.0f, this.wolf.getMaxHeadXRot());
        --this.timer;
    }

    private boolean isAttractive(Player player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (this.stray.isTame() && itemStack.is(Items.BONE)) {
                return true;
            }
            if (!this.stray.isFood(itemStack)) continue;
            return true;
        }
        return false;
    }
}

