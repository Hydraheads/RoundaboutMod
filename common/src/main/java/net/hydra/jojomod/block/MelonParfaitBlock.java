package net.hydra.jojomod.block;

import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.VoiceLine;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.BlockView;

public class MelonParfaitBlock extends Block {
    public static final IntegerProperty EATING_STAGE = ModBlocks.EATING_STAGE;

    public MelonParfaitBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(EATING_STAGE, 1));
    }

    protected static final VoxelShape SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);

    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        return RenderShape.MODEL;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
        if (blockState.is(this)) {
            int i = blockState.getValue(EATING_STAGE);
            return (BlockState)blockState.setValue(EATING_STAGE,Math.min(3, i + 1));
        }
        return super.getStateForPlacement(blockPlaceContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EATING_STAGE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand $$4, BlockHitResult $$5) {
        int eating_stage = state.getValue(EATING_STAGE);
        if (!level.isClientSide()) {
            if (eating_stage < 3) {

                if (!FateTypes.hasBloodHunger(player)) {
                    player.getFoodData().eat(5, 0.2F);
                }
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                        SoundEvents.GENERIC_EAT , SoundSource.PLAYERS, 2f, 0.2f);
                level.setBlock(pos, (BlockState)state.setValue(EATING_STAGE, eating_stage + 1), 3);

            } else {
                ItemEntity glass_bottle = new ItemEntity(level,pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GLASS_BOTTLE));
                level.addFreshEntity(glass_bottle);
                BlockState air = Blocks.AIR.defaultBlockState();
                level.setBlock(pos,air,1);
            }
        }
        return  InteractionResult.SUCCESS;
    }
}