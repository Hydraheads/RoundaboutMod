package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.RoundaboutCommands;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FogTrapBlock extends FogBlock implements EntityBlock{

    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty POWERED;
    protected static final VoxelShape AABB;
    protected static final VoxelShape NOT_ATTACHED_AABB;

    public FogTrapBlock(Properties $$0) {
        super($$0);
    }

    public FogTrapBlock(Properties $$0, boolean untrue) {
        super($$0, untrue);
    }

    @Override
    public void onPlace(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        super.onPlace($$0, $$1, $$2, $$3, $$4);

    }

    private boolean checkWire(ServerLevel lv, BlockPos pos, BooleanProperty prop){
        if(lv.getBlockState(pos).is(ModBlocks.FOG_TRAP) ||
                lv.getBlockState(pos).is(Blocks.TRIPWIRE)){

            BlockState bstate =  lv.getBlockState(pos);
            if(!bstate.getValue(prop)){
                bstate = bstate.setValue(prop,true);
                lv.setBlockAndUpdate(pos,bstate);
            }
            return true;
        }
        return false;
    }

    @Override
    public void tick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        if($$0.getValue(IN_FOG)) {

            List<? extends Entity> entsIn = $$1.getEntities((Entity)null, $$0.getShape($$1, $$2).bounds().move($$2));
            if(!entsIn.isEmpty() && !entsIn.get(0).isIgnoringBlockTriggers()){
                stepped($$1,$$2,$$0,entsIn.get(0));
            }
                //Run checks

                //I could technically keep calling setBlockAndUpdate every 5 ticks
                //but I feel like that would tank performance
                //so
                boolean isNorth = checkWire($$1,$$2.north(),SOUTH);
                boolean isSouth = checkWire($$1,$$2.south(),NORTH);
                boolean isEast = checkWire($$1,$$2.east(),WEST);
                boolean isWest = checkWire($$1,$$2.west(),EAST);

                if($$0.getValue(NORTH) != isNorth
                  || $$0.getValue(SOUTH) != isSouth
                  || $$0.getValue(EAST) != isEast
                  || $$0.getValue(WEST) != isWest){

                    $$0 = $$0.setValue(NORTH,isNorth);
                    $$0 = $$0.setValue(SOUTH,isSouth);
                    $$0 = $$0.setValue(EAST,isEast);
                    $$0 = $$0.setValue(WEST,isWest);

                    $$1.setBlockAndUpdate($$2,$$0);
                }











        }

        super.tick($$0, $$1, $$2, $$3);
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return true;
    }

    public void stepped(ServerLevel $$0, BlockPos $$1, BlockState $$2, Entity stepper) {
        FogTrapBlockEntity fbe = (FogTrapBlockEntity) $$0.getBlockEntity($$1);
        Entity ownerEntity = null;
        String name = "";
        if(fbe != null && fbe.getOwner() != null) {
            ownerEntity = $$0.getEntity(fbe.getOwner());
            name = fbe.getName();
        }
        if($$2.getValue(IN_FOG) &&
                ownerEntity != null && !stepper.is(ownerEntity)
                && ownerEntity instanceof StandUser se && se.roundabout$getStandPowers() instanceof PowersJustice justice &&
                !justice.queryJusticeEntities().contains(stepper)){

            if(stepper instanceof ServerPlayer stp){
                stp.playSound(SoundEvents.TRIPWIRE_CLICK_ON);
            }
            if(ownerEntity instanceof ServerPlayer sp){
                if(!(name == null) && !(name.isEmpty()) && !name.equals("[Fog Trap]")){
                    sp.displayClientMessage(Component.literal(name).append(Component.translatable("text.roundabout.warn_trap_triggered").withStyle(ChatFormatting.YELLOW)), true);
                } else{
                    sp.displayClientMessage(Component.translatable("text.roundabout.warn_unnamed_trap_triggered").withStyle(ChatFormatting.YELLOW),true);
                }



                sp.playSound(ModSounds.SUMMON_SOUND_EVENT);
                for( LivingEntity e : justice.queryJusticeEntities()){
                     if(e instanceof FallenMob m && stepper instanceof LivingEntity enemy && e.distanceToSqr($$1.getX(),$$1.getY(),$$1.getZ()) < fbe.getRange()){

                         m.setTarget(enemy);
                     }
                }

            }
        }
        super.stepOn($$0, $$1, $$2, stepper);
    }

    @Override
    public void setPlacedBy(Level $$0, BlockPos $$1, BlockState $$2, @Nullable LivingEntity $$3, ItemStack $$4) {
        if(!($$3 == null) && $$0.getBlockEntity($$1) instanceof FogTrapBlockEntity fe){
            fe.setValues($$3,$$3.getItemInHand($$3.getUsedItemHand()).getDisplayName().getString());
        }
        super.setPlacedBy($$0, $$1, $$2, $$3, $$4);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(POWERED, IN_FOG, NORTH, EAST, WEST, SOUTH);
    }
    static {
        POWERED = BlockStateProperties.POWERED;
        AABB = Block.box((double)0.0F, (double)1.0F, (double)0.0F, (double)16.0F, (double)2.5F, (double)16.0F);

        NOT_ATTACHED_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)8.0F, (double)16.0F);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(IN_FOG)) {
            return NOT_ATTACHED_AABB;
        } else {
            return Shapes.empty();

        }
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FogTrapBlockEntity(blockPos,blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return EntityBlock.super.getTicker($$0, $$1, $$2);
    }

    @Override
    public @Nullable <T extends BlockEntity> GameEventListener getListener(ServerLevel $$0, T $$1) {
        return EntityBlock.super.getListener($$0, $$1);
    }
}
