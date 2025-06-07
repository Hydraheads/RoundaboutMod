package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class SafePlaceContext extends BlockPlaceContext {
    private final Direction direction;

    public SafePlaceContext(Level $$0, Player ent, BlockPos $$1, Direction $$2, ItemStack $$3, Direction $$4) {
        super($$0, ent, InteractionHand.MAIN_HAND, $$3, new BlockHitResult(Vec3.atBottomCenterOf($$1), $$4, $$1, false));
        this.direction = $$2;
    }

    @Override
    public BlockPos getClickedPos() {
        return this.getHitResult().getBlockPos();
    }

    @Override
    public boolean canPlace() {
        return this.getLevel().getBlockState(this.getHitResult().getBlockPos()).canBeReplaced(this);
    }

    @Override
    public boolean replacingClickedOnBlock() {
        return this.canPlace();
    }

    @Override
    public Direction getNearestLookingDirection() {
        return Direction.DOWN;
    }

    @Override
    public Direction[] getNearestLookingDirections() {
        switch (this.direction) {
            case DOWN:
            default:
                return new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP};
            case UP:
                return new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
            case NORTH:
                return new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.SOUTH};
            case SOUTH:
                return new Direction[]{Direction.DOWN, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.NORTH};
            case WEST:
                return new Direction[]{Direction.DOWN, Direction.WEST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.EAST};
            case EAST:
                return new Direction[]{Direction.DOWN, Direction.EAST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.WEST};
        }
    }

    @Override
    public Direction getHorizontalDirection() {
        return this.direction.getAxis() == Direction.Axis.Y ? Direction.NORTH : this.direction;
    }

    @Override
    public boolean isSecondaryUseActive() {
        return false;
    }

    @Override
    public float getRotation() {
        return (float)(this.direction.get2DDataValue() * 90);
    }
}
