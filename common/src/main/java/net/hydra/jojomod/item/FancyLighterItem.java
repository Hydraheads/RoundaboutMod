package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.FancyLighterBlock;
import net.hydra.jojomod.block.FancyLighterBlockEntity;
import net.hydra.jojomod.block.FogTrapBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class FancyLighterItem extends BlockItem {

    //TODO: maybe remove the loggers after I finished everything, rn they are vital

    public FancyLighterItem(Block $$0, Properties $$1) {
        super($$0, $$1);
    }

    private static final String IS_LIT_TAG = "IsLighterLit";

    private boolean getIsLit(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(IS_LIT_TAG);
    }

    public void setIsNotLit(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(IS_LIT_TAG, value);
    }

    public static ItemStack stuff(ItemStack $$0, Player p){
        Item item = ModItems.FANCY_LIGHTER;
        if(!$$0.hasTag()){
             $$0.getOrCreateTagElement("UserId").putString("Stuff", p.getStringUUID());
             $$0.getOrCreateTagElement("UserIdUUID").putUUID("StuffOther", p.getUUID());
        }

        ItemStack itemStack = item.getDefaultInstance();

        return itemStack;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if(entity.isInWaterOrRain()){
            if(!getIsLit(stack)) {
                unlit(stack);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if($$3.getItem() instanceof FancyLighterItem FI){
            FI.lirOrUnlit($$0, $$1, $$3);
        }
        InteractionResultHolder<ItemStack> placed = super.use($$0,$$1,$$2);
        return placed;
    }

    public void unlit(ItemStack stack){
        this.setIsNotLit(stack, true);
    }

    public void lirOrUnlit(Level $$0, Player $$1, ItemStack $$2){
        if(!$$1.isInWaterOrRain()) {
            if (!getIsLit($$2)) {
                setIsNotLit($$2, true);
            } else {
                setIsNotLit($$2, false);
                blackSabbathCheck($$0, $$1, $$2);
            }
        }
    }

    private void blackSabbathCheck(Level $$0, Player $$1, ItemStack $$2){
            if($$2.getItem() instanceof  FancyLighterItem FI){
                if($$2.hasTag() && $$2 != null && $$2.getTag() != null) {
                    if ($$0 instanceof ServerLevel SL) {
                        Entity ownerEntity = null;
                        if ($$2.getTag().contains("UserIdUUID")) {
                            ownerEntity = SL.getEntity($$2.getTagElement("UserIdUUID").getUUID("StuffOther"));

                            if(SL.getEntity($$2.getTagElement("UserIdUUID").getUUID("StuffOther")) != null) {
                                System.out.println(ownerEntity);

                                if($$1 == SL.getEntity($$2.getTagElement("UserIdUUID").getUUID("StuffOther"))){
                                    System.out.println("Black Sabbath is Bing Chillin'");
                                } else {
                                    System.out.println("Black Sabbath is definitely NOT Bing Chillin'");
                                }
                            } else {
                                System.out.println("Owner Entity doesn't exist");
                            }
                        } else {
                            System.out.println("Does not contain UUID");
                        }
                    }
                }
            }
    }

    public float getCurrentPredicateValue(Level level, ItemStack stack) {

    if(level != null) {
            return getIsLit(stack) ? 0.2f : 0.0f;
        }
          return  0.0f;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        BlockPos abovePos = blockPos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (!aboveState.isAir()) return InteractionResult.FAIL;
        if (!level.isClientSide) {
            InteractionResult $$1 = this.placeLighter(new BlockPlaceContext(context));
            return $$1;
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /* this to bottom is copied and modified code for assigning the owner of the lighter block, putting everything in private/protected is evil, cuz I can't just super.[method] :/ */
    public InteractionResult placeLighter(BlockPlaceContext $$0) {
        if (!this.getBlock().isEnabled($$0.getLevel().enabledFeatures())) {
            return InteractionResult.FAIL;
        } else if (!$$0.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext $$1 = this.updatePlacementContext($$0);
            if ($$1 == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState $$2 = this.getPlacementState($$1);
                if ($$2 == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock($$1, $$2)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos $$3 = $$1.getClickedPos();
                    Level $$4 = $$1.getLevel();
                    Player $$5 = $$1.getPlayer();
                    ItemStack $$6 = $$1.getItemInHand();
                    BlockState $$7 = $$4.getBlockState($$3);
                    if ($$7.is($$2.getBlock())) {
                        $$7 = this.updateBlockStateFromTag($$3, $$4, $$6, $$7);
                        this.updateCustomBlockEntityTag($$3, $$4, $$5, $$6, $$7);
                        $$7.getBlock().setPlacedBy($$4, $$3, $$7, $$5, $$6);
                        if ($$5 instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)$$5, $$3, $$6);
                            if($$2.getBlock() instanceof FancyLighterBlock FB){
                                if($$4.getBlockEntity($$3) instanceof FancyLighterBlockEntity FLBE){
                                    if($$6.getItem() instanceof FancyLighterItem FI){
                                        if($$4 instanceof ServerLevel $$8) {
                                            if ($$6.getTag() != null) {
                                                if ($$6.getTag().contains("UserIdUUID")) {

                                                    if ($$6.getTagElement("UserIdUUID").getUUID("StuffOther") != null) {

                                                        FLBE.setValue($$8.getEntity($$6.getTagElement("UserIdUUID").getUUID("StuffOther")));

                                                        if ($$5 == $$8.getEntity($$6.getTagElement("UserIdUUID").getUUID("StuffOther"))) {
                                                            System.out.println(FLBE.getOwner());
                                                            System.out.println("Black Sabbath is Bing Chillin'");
                                                        } else {
                                                            System.out.println("Black Sabbath is definitely NOT Bing Chillin'");
                                                        }
                                                    } else {
                                                        System.out.println("Owner Entity doesn't exist");
                                                    }
                                                } else {
                                                    System.out.println("Does not contain UUID");
                                                }
                                            } else {
                                                System.out.println("Does not contain a Tag");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    SoundType $$8 = $$7.getSoundType();
                    $$4.playSound($$5, $$3, this.getPlaceSound($$7), SoundSource.BLOCKS, ($$8.getVolume() + 1.0F) / 2.0F, $$8.getPitch() * 0.8F);
                    $$4.gameEvent(GameEvent.BLOCK_PLACE, $$3, GameEvent.Context.of($$5, $$7));
                    if ($$5 == null || !$$5.getAbilities().instabuild) {
                        $$6.shrink(1);
                    }

                    return InteractionResult.sidedSuccess($$4.isClientSide);
                }
            }
        }
    }

    private BlockState updateBlockStateFromTag(BlockPos $$0, Level $$1, ItemStack $$2, BlockState $$3) {
        BlockState $$4 = $$3;
        CompoundTag $$5 = $$2.getTag();
        if ($$5 != null) {
            CompoundTag $$6 = $$5.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> $$7 = $$3.getBlock().getStateDefinition();

            for (String $$8 : $$6.getAllKeys()) {
                Property<?> $$9 = $$7.getProperty($$8);
                if ($$9 != null) {
                    String $$10 = $$6.get($$8).getAsString();
                    $$4 = updateState($$4, $$9, $$10);
                }
            }
        }

        if ($$4 != $$3) {
            $$1.setBlock($$0, $$4, 2);
        }

        return $$4;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState $$0, Property<T> $$1, String $$2) {
        return $$1.getValue($$2).map($$2x -> $$0.setValue($$1, $$2x)).orElse($$0);
    }
}
