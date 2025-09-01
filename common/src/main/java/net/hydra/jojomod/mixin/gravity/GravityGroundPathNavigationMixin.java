package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IGravityEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(GroundPathNavigation.class)
public abstract class GravityGroundPathNavigationMixin extends PathNavigation {
    public GravityGroundPathNavigationMixin(Mob $$0, Level $$1) {
        super($$0, $$1);
    }
    @Inject(
            method = "createPath(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/pathfinder/Path;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rdbt$createPath(BlockPos $$0, int $$1, CallbackInfoReturnable<Path> cir) {
        if (this.mob != null) {
            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
            if (dir == Direction.DOWN)
                return;

            if (this.level.getBlockState($$0).isAir()) {
                BlockPos $$2 = $$0.relative(dir);

                while ($$2.getY() > this.level.getMinBuildHeight() && this.level.getBlockState($$2).isAir()) {
                    $$2 = $$2.relative(dir);
                }

                if ($$2.getY() > this.level.getMinBuildHeight()) {
                    cir.setReturnValue(super.createPath($$2.relative(dir.getOpposite()), $$1));
                    return;
                }

                while ($$2.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState($$2).isAir()) {
                    $$2 = $$2.relative(dir.getOpposite());
                }

                $$0 = $$2;
            }

            if (!this.level.getBlockState($$0).isSolid()) {
                cir.setReturnValue(super.createPath($$0, $$1));
                return;
            } else {
                BlockPos $$3 = $$0.relative(dir.getOpposite());

                while ($$3.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState($$3).isSolid()) {
                    $$3 = $$3.relative(dir.getOpposite());
                }

                cir.setReturnValue(super.createPath($$3, $$1));
            }
        }
    }
    @Inject(
            method = "getTempMobPos()Lnet/minecraft/world/phys/Vec3;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rdbt$getTempMobPos(CallbackInfoReturnable<Vec3> cir) {
        if (this.mob != null) {
            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
            if (dir == Direction.DOWN)
                return;

            if (dir == Direction.UP) {
                cir.setReturnValue(new Vec3(this.mob.getX(), (double)rdbt$getSurfaceY2(), this.mob.getZ()));
            } if (dir == Direction.NORTH) {
                cir.setReturnValue(new Vec3(this.mob.getX(), this.mob.getY(), rdbt$getSurfaceZ()));
            } if (dir == Direction.SOUTH) {
                cir.setReturnValue(new Vec3(this.mob.getX(), this.mob.getY(), rdbt$getSurfaceZ2()));
            } if (dir == Direction.WEST) {
                cir.setReturnValue(new Vec3(rdbt$getSurfaceX(), this.mob.getY(), this.mob.getZ()));
            } if (dir == Direction.EAST) {
                cir.setReturnValue(new Vec3(rdbt$getSurfaceX2(), this.mob.getY(), this.mob.getZ()));
            }
        }
    }

    private int rdbt$getSurfaceY2() {
        if (this.mob.isInWater() && this.canFloat()) {
            int $$0 = this.mob.getBlockY();
            BlockState $$1 = this.level.getBlockState(BlockPos.containing(this.mob.getX(), (double)$$0, this.mob.getZ()));
            int $$2 = 0;

            while ($$1.is(Blocks.WATER)) {
                $$1 = this.level.getBlockState(BlockPos.containing(this.mob.getX(), (double)(--$$0), this.mob.getZ()));
                if (++$$2 > 16) {
                    return this.mob.getBlockY();
                }
            }

            return $$0;
        } else {
            return Mth.floor(this.mob.getY() - 0.5);
        }
    }
    private int rdbt$getSurfaceZ() {
        if (this.mob.isInWater() && this.canFloat()) {
            int $$0 = this.mob.getBlockZ();
            BlockState $$1 = this.level.getBlockState(BlockPos.containing(this.mob.getX(), this.mob.getY(), (double)$$0));
            int $$2 = 0;

            while ($$1.is(Blocks.WATER)) {
                $$1 = this.level.getBlockState(BlockPos.containing(this.mob.getX(), this.mob.getY(), (double)(++$$0)));
                if (++$$2 > 16) {
                    return this.mob.getBlockZ();
                }
            }

            return $$0;
        } else {
            return Mth.floor(this.mob.getZ() + 0.5);
        }
    }
    private int rdbt$getSurfaceZ2() {
        if (this.mob.isInWater() && this.canFloat()) {
            int $$0 = this.mob.getBlockZ();
            BlockState $$1 = this.level.getBlockState(BlockPos.containing(this.mob.getX(), this.mob.getY(), (double)$$0));
            int $$2 = 0;

            while ($$1.is(Blocks.WATER)) {
                $$1 = this.level.getBlockState(BlockPos.containing(this.mob.getX(), this.mob.getY(), (double)(--$$0)));
                if (++$$2 > 16) {
                    return this.mob.getBlockZ();
                }
            }

            return $$0;
        } else {
            return Mth.floor(this.mob.getZ() - 0.5);
        }
    }
    private int rdbt$getSurfaceX() {
        if (this.mob.isInWater() && this.canFloat()) {
            int $$0 = this.mob.getBlockX();
            BlockState $$1 = this.level.getBlockState(BlockPos.containing((double)$$0, this.mob.getY(), this.mob.getZ()));
            int $$2 = 0;

            while ($$1.is(Blocks.WATER)) {
                $$1 = this.level.getBlockState(BlockPos.containing((double)(++$$0), this.mob.getY(), this.mob.getZ()));
                if (++$$2 > 16) {
                    return this.mob.getBlockX();
                }
            }

            return $$0;
        } else {
            return Mth.floor(this.mob.getX() + 0.5);
        }
    }
    private int rdbt$getSurfaceX2() {
        if (this.mob.isInWater() && this.canFloat()) {
            int $$0 = this.mob.getBlockX();
            BlockState $$1 = this.level.getBlockState(BlockPos.containing((double)$$0, this.mob.getY(), this.mob.getZ()));
            int $$2 = 0;

            while ($$1.is(Blocks.WATER)) {
                $$1 = this.level.getBlockState(BlockPos.containing((double)(--$$0), this.mob.getY(), this.mob.getZ()));
                if (++$$2 > 16) {
                    return this.mob.getBlockX();
                }
            }

            return $$0;
        } else {
            return Mth.floor(this.mob.getX() - 0.5);
        }
    }

}
