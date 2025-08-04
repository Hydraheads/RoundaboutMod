package net.hydra.jojomod.block;

import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FogBlock extends Block {
    public static final BooleanProperty IN_FOG = ModBlocks.IN_FOG;
    public FogBlock(Properties $$0) {
        super($$0);
        this.registerDefaultState(this.stateDefinition.any().setValue(IN_FOG, Boolean.valueOf(false))
        );
    }
    public FogBlock(Properties $$0,boolean untrue) {
        super($$0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(BlockState p_153848_, BlockPlaceContext p_153849_) {
        return !(p_153848_.getValue(IN_FOG));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        super.onPlace($$0, $$1, $$2, $$3, $$4);
        if (!$$1.isClientSide) {
        if (((IPermaCasting)$$1).roundabout$inPermaCastFogRange($$2)){
            if (!$$0.getValue(IN_FOG)) {
                $$0 = $$0.setValue(IN_FOG, true);
                $$1.setBlockAndUpdate($$2, $$0);
            }
        } else {
                $$1.setBlockAndUpdate($$2, $$0);
        }
            $$1.scheduleTick($$2, this, 1);
        }
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return !($$0.getValue(IN_FOG));
    }
    @Override
    public void tick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        $$1.scheduleTick($$2, this, (int)(5+Math.floor(Math.random()*4)));
        if (((IPermaCasting)$$1).roundabout$inPermaCastFogRange($$2)){
            if (!$$0.getValue(IN_FOG)) {
                $$0 = $$0.setValue(IN_FOG, true);
                $$1.setBlockAndUpdate($$2, $$0);
            }
        } else {
            if ($$0.getValue(IN_FOG)) {
                $$0 = $$0.setValue(IN_FOG, false);
                $$1.setBlockAndUpdate($$2, $$0);
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        //if (Roundabout.worldInFog == 0) {
            //return RenderShape.INVISIBLE;
        //}
        return super.getRenderShape($$0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(IN_FOG)) {
            return super.getShape(blockState,blockGetter,blockPos,collisionContext);
        } else {
            return Shapes.empty();

        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(IN_FOG);
    }


    @Override
    protected void spawnDestroyParticles(Level $$0, Player $$1, BlockPos $$2, BlockState $$3) {
        if ($$0 instanceof ServerLevel){

            ((ServerLevel) $$0).sendParticles(ModParticles.FOG_CHAIN,
                    $$2.getX()+0.5,
                    $$2.getY()+0.5,
                    $$2.getZ()+0.5,
                    15, 0, 0, 0, 0.07);
        }
    }
}
