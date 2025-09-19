package net.hydra.jojomod.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nullable;
import java.util.function.Predicate;



public class StoneMaskBlock extends HorizontalDirectionalBlock{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    @Nullable
    private BlockPattern snowGolemBase;
    @Nullable
    private BlockPattern snowGolemFull;
    @Nullable
    private BlockPattern ironGolemBase;
    @Nullable
    private BlockPattern ironGolemFull;
    private static final Predicate<BlockState> PUMPKINS_PREDICATE = $$0 -> $$0 != null && ($$0.is(Blocks.CARVED_PUMPKIN) || $$0.is(Blocks.JACK_O_LANTERN));

    protected StoneMaskBlock(BlockBehaviour.Properties $$0) {
        super($$0);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void onPlace(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        if (!$$3.is($$0.getBlock())) {
            this.trySpawnGolem($$1, $$2);
        }
    }

    public boolean canSpawnGolem(LevelReader $$0, BlockPos $$1) {
        return this.getOrCreateSnowGolemBase().find($$0, $$1) != null || this.getOrCreateIronGolemBase().find($$0, $$1) != null;
    }

    private void trySpawnGolem(Level $$0, BlockPos $$1) {
        BlockPattern.BlockPatternMatch $$2 = this.getOrCreateSnowGolemFull().find($$0, $$1);
        if ($$2 != null) {
            SnowGolem $$3 = EntityType.SNOW_GOLEM.create($$0);
            if ($$3 != null) {
                spawnGolemInWorld($$0, $$2, $$3, $$2.getBlock(0, 2, 0).getPos());
            }
        } else {
            BlockPattern.BlockPatternMatch $$4 = this.getOrCreateIronGolemFull().find($$0, $$1);
            if ($$4 != null) {
                IronGolem $$5 = EntityType.IRON_GOLEM.create($$0);
                if ($$5 != null) {
                    $$5.setPlayerCreated(true);
                    spawnGolemInWorld($$0, $$4, $$5, $$4.getBlock(1, 2, 0).getPos());
                }
            }
        }
    }

    private static void spawnGolemInWorld(Level $$0, BlockPattern.BlockPatternMatch $$1, Entity $$2, BlockPos $$3) {
        clearPatternBlocks($$0, $$1);
        $$2.moveTo((double)$$3.getX() + 0.5, (double)$$3.getY() + 0.05, (double)$$3.getZ() + 0.5, 0.0F, 0.0F);
        $$0.addFreshEntity($$2);

        for (ServerPlayer $$4 : $$0.getEntitiesOfClass(ServerPlayer.class, $$2.getBoundingBox().inflate(5.0))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger($$4, $$2);
        }

        updatePatternBlocks($$0, $$1);
    }

    public static void clearPatternBlocks(Level $$0, BlockPattern.BlockPatternMatch $$1) {
        for (int $$2 = 0; $$2 < $$1.getWidth(); $$2++) {
            for (int $$3 = 0; $$3 < $$1.getHeight(); $$3++) {
                BlockInWorld $$4 = $$1.getBlock($$2, $$3, 0);
                $$0.setBlock($$4.getPos(), Blocks.AIR.defaultBlockState(), 2);
                $$0.levelEvent(2001, $$4.getPos(), Block.getId($$4.getState()));
            }
        }
    }

    public static void updatePatternBlocks(Level $$0, BlockPattern.BlockPatternMatch $$1) {
        for (int $$2 = 0; $$2 < $$1.getWidth(); $$2++) {
            for (int $$3 = 0; $$3 < $$1.getHeight(); $$3++) {
                BlockInWorld $$4 = $$1.getBlock($$2, $$3, 0);
                $$0.blockUpdated($$4.getPos(), Blocks.AIR);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        return this.defaultBlockState().setValue(FACING, $$0.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(FACING);
    }

    private BlockPattern getOrCreateSnowGolemBase() {
        if (this.snowGolemBase == null) {
            this.snowGolemBase = BlockPatternBuilder.start()
                    .aisle(" ", "#", "#")
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
                    .build();
        }

        return this.snowGolemBase;
    }

    private BlockPattern getOrCreateSnowGolemFull() {
        if (this.snowGolemFull == null) {
            this.snowGolemFull = BlockPatternBuilder.start()
                    .aisle("^", "#", "#")
                    .where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
                    .build();
        }

        return this.snowGolemFull;
    }

    private BlockPattern getOrCreateIronGolemBase() {
        if (this.ironGolemBase == null) {
            this.ironGolemBase = BlockPatternBuilder.start()
                    .aisle("~ ~", "###", "~#~")
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
                    .where('~', $$0 -> $$0.getState().isAir())
                    .build();
        }

        return this.ironGolemBase;
    }

    private BlockPattern getOrCreateIronGolemFull() {
        if (this.ironGolemFull == null) {
            this.ironGolemFull = BlockPatternBuilder.start()
                    .aisle("~^~", "###", "~#~")
                    .where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
                    .where('~', $$0 -> $$0.getState().isAir())
                    .build();
        }

        return this.ironGolemFull;
    }
}