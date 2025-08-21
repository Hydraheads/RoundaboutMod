package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = Shulker.class, priority = 1001)
public abstract class GravityShulkerMixin extends AbstractGolem implements VariantHolder<Optional<DyeColor>>, Enemy {
    @Shadow
    private static float getPhysicalPeek(float f) {
        return 0;
    }

    @Shadow private float currentPeekAmount;

    @Shadow private float currentPeekAmountO;

    @Shadow public abstract Direction getAttachFace();

    @Shadow
    public static AABB getProgressDeltaAabb(Direction direction, float f, float g) {
        return null;
    }

    protected GravityShulkerMixin(EntityType<? extends AbstractGolem> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(
            method = "onPeekAmountChange",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$onPeekAmountChange(CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this);
        if (gravityDirection == Direction.DOWN)
            return;

        ci.cancel();


        this.reapplyPosition();
        float $$0 = getPhysicalPeek(this.currentPeekAmount);
        float $$1 = getPhysicalPeek(this.currentPeekAmountO);
        Direction $$2 = this.getAttachFace().getOpposite();
        float $$3 = $$0 - $$1;
        if (!($$3 <= 0.0F)) {
            for (Entity $$5 : this.level()
                    .getEntities(
                            this,
                            getProgressDeltaAabb($$2, $$1, $$0).move(this.getX() - 0.5, this.getY(), this.getZ() - 0.5),
                            EntitySelector.NO_SPECTATORS.and($$0x -> !$$0x.isPassengerOfSameVehicle(this))
                    )) {
                if (!($$5 instanceof Shulker) && !$$5.noPhysics) {
                    $$5.move(
                            MoverType.SHULKER,
                            RotationUtil.vecWorldToPlayer(new Vec3((double)($$3 * (float)$$2.getStepX()),
                                    (double)($$3 * (float)$$2.getStepY()), (double)($$3 * (float)$$2.getStepZ())), gravityDirection)
                    );
                }
            }
        }
    }
}