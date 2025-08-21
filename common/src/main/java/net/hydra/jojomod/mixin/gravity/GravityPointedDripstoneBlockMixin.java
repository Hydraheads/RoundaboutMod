package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PointedDripstoneBlock.class)
public abstract class GravityPointedDripstoneBlockMixin extends Block implements Fallable, SimpleWaterloggedBlock {
    @Shadow @Final public static DirectionProperty TIP_DIRECTION;

    @Shadow @Final public static EnumProperty<DripstoneThickness> THICKNESS;

    public GravityPointedDripstoneBlockMixin(Properties $$0) {
        super($$0);
    }

    // use Comparable<Direction> instead of Direction because of erased signature
    @Inject(
            method = "fallOn",
            at = @At(
                    value = "HEAD",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
                    ordinal = 0
            ),
            cancellable = true)
    private void rdbt$fallOn(Level $$0, BlockState $$1, BlockPos $$2, Entity $$3, float $$4, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$3);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

        if (($$1.getValue(TIP_DIRECTION) == gravityDirection.getOpposite() ? Direction.UP : Direction.DOWN) == Direction.UP && $$1.getValue(THICKNESS) == DripstoneThickness.TIP) {
            $$3.causeFallDamage($$4 + 2.0F, 2.0F, $$0.damageSources().stalagmite());
        } else {
            super.fallOn($$0, $$1, $$2, $$3, $$4);
        }
    }
}
