package net.hydra.jojomod.mixin.gravity.item;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BehaviorUtils.class)
public class GravityBehaviorUtilsMixin {
    @Inject(
            method = "throwItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;F)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void rdbt$throwItem(
            LivingEntity entity, ItemStack $$1, Vec3 $$2, Vec3 $$3, float $$4, CallbackInfo ci
    ) {
        Direction gravityDirection = GravityAPI.getGravityDirection(entity);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

        double $$5 = entity.getEyeY() - (double)$$4;

        Vec3 eyeOffset = GravityAPI.getEyeOffset(entity);
        Vec3 offset = eyeOffset.normalize().scale($$5);
        Vec3 itemPos = entity.position().add(eyeOffset).subtract(offset);
        ItemEntity itemEntity = new ItemEntity(
                entity.level(), itemPos.x(), itemPos.y(), itemPos.z(), $$1
        );
        ((IGravityEntity)itemEntity).roundabout$setBaseGravityDirection(
                GravityAPI.getGravityDirection(entity)
        );

        itemEntity.setThrower(entity.getUUID());
        Vec3 $$7 = $$2.subtract(entity.position());
        $$7 = $$7.normalize().multiply($$3.x, $$3.y, $$3.z);
        GravityAPI.setWorldVelocity(itemEntity, $$7);
        itemEntity.setDefaultPickUpDelay();
        entity.level().addFreshEntity(itemEntity);
    }

}
