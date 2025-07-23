package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IIronGolem;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(DefendVillageTargetGoal.class)
public class JusticeDefendVillageTargetGoal {

    /**Part two of the Iron Golem aggro mixin for justice morphs, helps iron golems detect or get aggro negated */

    @Inject(method = "start", at = @At(value = "HEAD"))
    protected void roundabout$start(CallbackInfo ci) {
        IIronGolem igolem = ((IIronGolem) golem);
        if (igolem.roundabout$getNegation()) {
            potentialTarget = null;
            igolem.roundabout$setNegation(false);
        }
    }
    @Inject(method = "canUse()Z", at = @At(value = "HEAD"))
    protected void roundabout$canUse(CallbackInfoReturnable<Boolean> cir) {


        AABB $$0 = this.golem.getBoundingBox().inflate(10.0, 8.0, 10.0);
        List<Player> $$2 = this.golem.level().getNearbyPlayers(this.attackTargeting, this.golem, $$0);
        for (Player $$5 : $$2) {

            IPlayerEntity ple = ((IPlayerEntity) $$5);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);

            if (ShapeShifts.isZombie(shift) || ShapeShifts.isSkeleton(shift)) {
                this.potentialTarget = $$5;
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    private IronGolem golem;
    @Nullable
    @Shadow
    private LivingEntity potentialTarget;
    @Shadow
    @Final
    private TargetingConditions attackTargeting;
}
