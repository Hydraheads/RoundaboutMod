/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.hydra.jojomod.entity.Terrier;
import java.util.EnumSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TerrierBegGoal
        extends Goal {
    private final TerrierEntity wolf;
    @Nullable
    private Player begFrom;
    private final Level world;
    private final float begDistance;
    private int timer;
    private final TargetingConditions validPlayerPredicate;

    public TerrierBegGoal(TerrierEntity wolf, float begDistance) {
        this.wolf = wolf;
        this.world = wolf.level();
        this.begDistance = begDistance;
        this.validPlayerPredicate = TargetingConditions.forNonCombat().range(begDistance);
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.begFrom = this.world.getNearestPlayer(this.validPlayerPredicate, this.wolf);
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
        if (this.wolf.distanceToSqr(this.begFrom) > (double)(this.begDistance * this.begDistance)) {
            return false;
        }
        return this.timer > 0 && this.isAttractive(this.begFrom);
    }

    @Override
    public void start() {
        this.wolf.setIsInterested(true);
        this.timer = this.adjustedTickDelay(40 + this.wolf.getRandom().nextInt(40));
    }

    @Override
    public void stop() {
        this.wolf.setIsInterested(false);
        this.begFrom = null;
    }

    @Override
    public void tick() {
        this.wolf.getLookControl().setLookAt(this.begFrom.getX(), this.begFrom.getEyeY(), this.begFrom.getZ(), 10.0f, this.wolf.getMaxHeadXRot());
        --this.timer;
    }

    private boolean isAttractive(Player player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (this.wolf.isTame() && itemStack.is(Items.BONE)) {
                return true;
            }
            if (!this.wolf.isFood(itemStack)) continue;
            return true;
        }
        return false;
    }
}

