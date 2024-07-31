package net.hydra.jojomod.block;

import com.google.common.annotations.VisibleForTesting;
import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.ContainerSingleItem;

import javax.annotation.Nullable;
import java.util.Objects;

public class StereoBlockEntity extends BlockEntity implements Clearable, ContainerSingleItem {

    private static final int SONG_END_PADDING = 20;
    private final NonNullList<ItemStack> items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    private int ticksSinceLastEvent;
    private long tickCount;
    private long recordStartedTick;
    private boolean isPlaying;

    public StereoBlockEntity(BlockPos $$0, BlockState $$1)
    {
        super(ModBlocks.STEREO_BLOCK_ENTITY, $$0, $$1);
    }

    @Override
    public void load(CompoundTag $$0) {
        super.load($$0);
        if ($$0.contains("RecordItem", 10)) {
            this.items.set(0, ItemStack.of($$0.getCompound("RecordItem")));
        }

        this.isPlaying = $$0.getBoolean("IsPlaying");
        this.recordStartedTick = $$0.getLong("RecordStartTick");
        this.tickCount = $$0.getLong("TickCount");
    }

    @Override
    protected void saveAdditional(CompoundTag $$0) {
        super.saveAdditional($$0);
        if (!this.getFirstItem().isEmpty()) {
            $$0.put("RecordItem", this.getFirstItem().save(new CompoundTag()));
        }

        $$0.putBoolean("IsPlaying", this.isPlaying);
        $$0.putLong("RecordStartTick", this.recordStartedTick);
        $$0.putLong("TickCount", this.tickCount);
    }

    public boolean isRecordPlaying() {
        return !this.getFirstItem().isEmpty() && this.isPlaying;
    }

    private void setHasRecordBlockState(@Nullable Entity $$0, boolean $$1) {
        if (this.level.getBlockState(this.getBlockPos()) == this.getBlockState()) {
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(JukeboxBlock.HAS_RECORD, Boolean.valueOf($$1)), 2);
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of($$0, this.getBlockState()));
        }
    }

    @VisibleForTesting
    public void startPlaying() {
        this.recordStartedTick = this.tickCount;
        this.isPlaying = true;
        this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        this.level.levelEvent(null, 1010, this.getBlockPos(), Item.getId(this.getFirstItem().getItem()));
        this.setChanged();
    }

    private void stopPlaying() {
        this.isPlaying = false;
        this.level.gameEvent(GameEvent.JUKEBOX_STOP_PLAY, this.getBlockPos(), GameEvent.Context.of(this.getBlockState()));
        this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        this.level.levelEvent(1011, this.getBlockPos(), 0);
        this.setChanged();
    }

    private void tick(Level $$0, BlockPos $$1, BlockState $$2) {
        this.ticksSinceLastEvent++;
        if (this.isRecordPlaying() && this.getFirstItem().getItem() instanceof RecordItem $$3) {
            if (this.shouldRecordStopPlaying($$3)) {
                this.stopPlaying();
            } else if (this.shouldSendJukeboxPlayingEvent()) {
                this.ticksSinceLastEvent = 0;
                $$0.gameEvent(GameEvent.JUKEBOX_PLAY, $$1, GameEvent.Context.of($$2));
                this.spawnMusicParticles($$0, $$1);
            }
        }

        this.tickCount++;
    }

    private boolean shouldRecordStopPlaying(RecordItem $$0) {
        return this.tickCount >= this.recordStartedTick + (long)$$0.getLengthInTicks() + 20L;
    }

    private boolean shouldSendJukeboxPlayingEvent() {
        return this.ticksSinceLastEvent >= 20;
    }

    @Override
    public ItemStack getItem(int $$0) {
        return this.items.get($$0);
    }

    @Override
    public ItemStack removeItem(int $$0, int $$1) {
        ItemStack $$2 = Objects.requireNonNullElse(this.items.get($$0), ItemStack.EMPTY);
        this.items.set($$0, ItemStack.EMPTY);
        if (!$$2.isEmpty()) {
            this.setHasRecordBlockState(null, false);
            this.stopPlaying();
        }

        return $$2;
    }

    @Override
    public void setItem(int $$0, ItemStack $$1) {
        if ($$1.is(ItemTags.MUSIC_DISCS) && this.level != null) {
            this.items.set($$0, $$1);
            this.setHasRecordBlockState(null, true);
            this.startPlaying();
        }
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean stillValid(Player $$0) {
        return Container.stillValidBlockEntity(this, $$0);
    }

    @Override
    public boolean canPlaceItem(int $$0, ItemStack $$1) {
        return $$1.is(ItemTags.MUSIC_DISCS) && this.getItem($$0).isEmpty();
    }

    @Override
    public boolean canTakeItem(Container $$0, int $$1, ItemStack $$2) {
        return $$0.hasAnyMatching(ItemStack::isEmpty);
    }

    private void spawnMusicParticles(Level $$0, BlockPos $$1) {
        if ($$0 instanceof ServerLevel $$2) {
            Vec3 $$3 = Vec3.atBottomCenterOf($$1).add(0.0, 0.6F, 0.0);
            float $$4 = (float)$$0.getRandom().nextInt(4) / 24.0F;
            $$2.sendParticles(ParticleTypes.NOTE, $$3.x(), $$3.y(), $$3.z(), 0, (double)$$4, 0.0, 0.0, 1.0);
        }
    }

    public void popOutRecord() {
        if (this.level != null && !this.level.isClientSide) {
            BlockPos $$0 = this.getBlockPos();
            ItemStack $$1 = this.getFirstItem();
            if (!$$1.isEmpty()) {
                this.removeFirstItem();
                Vec3 $$2 = Vec3.atLowerCornerWithOffset($$0, 0.5, 0.4, 0.5).offsetRandom(this.level.random, 0.4F);
                ItemStack $$3 = $$1.copy();
                ItemEntity $$4 = new ItemEntity(this.level, $$2.x(), $$2.y(), $$2.z(), $$3);
                $$4.setDefaultPickUpDelay();
                this.level.addFreshEntity($$4);
            }
        }
    }

    public static void playRecordTick(Level $$0, BlockPos $$1, BlockState $$2, StereoBlockEntity $$3) {
        $$3.tick($$0, $$1, $$2);
    }

    @VisibleForTesting
    public void setRecordWithoutPlaying(ItemStack $$0) {
        this.items.set(0, $$0);
        this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        this.setChanged();
    }

}
