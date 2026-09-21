package net.hydra.jojomod.block.handBlock;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Services;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class HandBlockEntity extends BlockEntity {


    public static final String TAG_SKULL_OWNER = "HandOwner";
    public static final String TAG_NOTE_BLOCK_SOUND = "note_block_sound";
    @Nullable
    private static GameProfileCache profileCache;
    @Nullable
    private static MinecraftSessionService sessionService;
    @Nullable
    private static Executor mainThreadExecutor;
    @Nullable
    private GameProfile owner;
    @Nullable
    private ResourceLocation noteBlockSound;
    private int animationTickCount;
    private boolean isAnimating;


    public HandBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.HAND_BLOCK_ENTITY, $$0, $$1);
    }

    public static void setup(Services $$0, Executor $$1) {
        profileCache = $$0.profileCache();
        sessionService = $$0.sessionService();
        mainThreadExecutor = $$1;
    }

    public static void clear() {
        profileCache = null;
        sessionService = null;
        mainThreadExecutor = null;
    }

    public ItemStack storedStack = ItemStack.EMPTY;

    public void setStoredStack(ItemStack stack) {
        synchronized(this) {
            this.storedStack = stack.copy();
        }
        setChanged();

        CompoundTag compoundtag = storedStack.getTag();

        if (compoundtag != null && compoundtag.contains("HandProfie")) {
            this.setOwner(NbtUtils.readGameProfile(compoundtag.getCompound("HandProfie")));
        }

    }

    public ItemStack getStoredStack() {
        return storedStack;
    }

    public void popOutRecord() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        BlockPos blockPos = this.getBlockPos();
        ItemStack itemStack = this.getStoredStack();
        if (itemStack.isEmpty()) {
            return;
        }

        storedStack = null;
        Vec3 vec3 = Vec3.atLowerCornerWithOffset(blockPos, 0.5, 0.3, 0.5);
        ItemStack itemStack2 = itemStack.copy();
        ItemEntity itemEntity = new ItemEntity(this.level, vec3.x(), vec3.y(), vec3.z(), itemStack2);
        itemEntity.setPickUpDelay(2);
        itemEntity.setDeltaMovement(0,0.1F,0);
        this.level.addFreshEntity(itemEntity);
        /*level.playSound(null, vec3.x(), vec3.y(), vec3.z(),
                ModSounds.CHESS_BREAK_EVENT, SoundSource.PLAYERS, 1F,
                (float) (1.00f + Math.random() * 0.01f));*/
    }

    protected void saveAdditional(CompoundTag $$0) {
        super.saveAdditional($$0);

        if (this.noteBlockSound != null) {
            $$0.putString("note_block_sound", this.noteBlockSound.toString());
        }

        if (!storedStack.isEmpty()) {
            $$0.put("StoredStack", storedStack.save(new CompoundTag()));
        }

    }
    public void load(CompoundTag $$0) {
        super.load($$0);

        if ($$0.contains("note_block_sound", 8)) {
            this.noteBlockSound = ResourceLocation.tryParse($$0.getString("note_block_sound"));
        }

        if ($$0.contains("StoredStack")) {
            storedStack = ItemStack.of($$0.getCompound("StoredStack"));
            if (storedStack.hasTag()) {
                CompoundTag compoundtag = storedStack.getTag();
                /*if (compoundtag.contains("HandOwner")) {
                    this.setOwner(NbtUtils.readGameProfile(compoundtag.getCompound("HandOwner")));
                }else {
                    Roundabout.LOGGER.info("itemStack did not contain the HandOwner GameProfile!");
                }*/
                if (compoundtag.contains("HandProfie")) {
                    this.setOwner(NbtUtils.readGameProfile(compoundtag.getCompound("HandProfie")));
                }
            }
        } else {
            storedStack = ItemStack.EMPTY;
        }

    }

    public static void animation(Level $$0, BlockPos $$1, BlockState $$2, HandBlockEntity $$3) {

    }

    public float getAnimation(float $$0) {
        return this.isAnimating ? (float)this.animationTickCount + $$0 : (float)this.animationTickCount;
    }

    @Nullable
    public GameProfile getOwnerProfile() {
        return this.owner;
    }

    @Nullable
    public ResourceLocation getNoteBlockSound() {
        return this.noteBlockSound;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setOwner(@Nullable GameProfile $$0) {
        synchronized(this) {
            this.owner = $$0;
        }

        this.updateOwnerProfile();
    }

    private void updateOwnerProfile() {
        updateGameprofile(this.owner, ($$0) -> {
            this.owner = $$0;
            this.setChanged();
        });
    }

    public static void updateGameprofile(@Nullable GameProfile $$0, Consumer<GameProfile> $$1) {
        if ($$0 != null && !StringUtil.isNullOrEmpty($$0.getName()) && (!$$0.isComplete() || !$$0.getProperties().containsKey("textures")) && profileCache != null && sessionService != null) {
            profileCache.getAsync($$0.getName(), ($$2) -> Util.backgroundExecutor().execute(() -> Util.ifElse($$2, ($$1x) -> {
                Property $$2x = (Property) Iterables.getFirst($$1x.getProperties().get("textures"), (Object)null);
                if ($$2x == null) {
                    MinecraftSessionService $$3 = sessionService;
                    if ($$3 == null) {
                        return;
                    }

                    $$1x = $$3.fillProfileProperties($$1x, true);
                }

                Executor $$5 = mainThreadExecutor;
                if ($$5 != null) {
                    GameProfile final$$1x = $$1x;
                    $$5.execute(() -> {
                        GameProfileCache $$3x = profileCache;
                        if ($$3x != null) {
                            $$3x.add(final$$1x);
                            $$1.accept(final$$1x);
                        }

                    });
                }

            }, () -> {
                Executor $$2x = mainThreadExecutor;
                if ($$2x != null) {
                    $$2x.execute(() -> $$1.accept($$0));
                }

            })));
        } else {
            $$1.accept($$0);
        }
    }

    public GameProfile getProfile() {
        if (storedStack == null) {
            return null;
        }
        if (storedStack.hasTag()) {
            CompoundTag tag = storedStack.getTag();
            /*if (tag.contains("OwnerName") && tag.contains("OwnerUUID")) {
                Optional<UUID> id = Optional.of(UUID.fromString(tag.getString("OwnerUUID")));
                String name = tag.getString("OwnerName");
                return id.isPresent() && !name.isEmpty() ? new GameProfile(id.get(), name) : null;
            }else {
                return null;
            }*/
            if (tag != null && tag.contains("HandProfile")) {
                return NbtUtils.readGameProfile(tag.getCompound("HandProfile"));
            }
        }
        return null;
    }
}
