package net.hydra.jojomod.block;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Services;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class HandBlockEntity extends BlockEntity {
    public static final String TAG_SKULL_OWNER = "SkullOwner";
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
        super(BlockEntityType.SKULL, $$0, $$1);
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

    protected void saveAdditional(CompoundTag $$0) {
        super.saveAdditional($$0);
        if (this.owner != null) {
            CompoundTag $$1 = new CompoundTag();
            NbtUtils.writeGameProfile($$1, this.owner);
            $$0.put("SkullOwner", $$1);
        }

        if (this.noteBlockSound != null) {
            $$0.putString("note_block_sound", this.noteBlockSound.toString());
        }

    }

    public void load(CompoundTag $$0) {
        super.load($$0);
        if ($$0.contains("SkullOwner", 10)) {
            this.setOwner(NbtUtils.readGameProfile($$0.getCompound("SkullOwner")));
        } else if ($$0.contains("ExtraType", 8)) {
            String $$1 = $$0.getString("ExtraType");
            if (!StringUtil.isNullOrEmpty($$1)) {
                this.setOwner(new GameProfile((UUID)null, $$1));
            }
        }

        if ($$0.contains("note_block_sound", 8)) {
            this.noteBlockSound = ResourceLocation.tryParse($$0.getString("note_block_sound"));
        }

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

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public void setOwner(@Nullable GameProfile $$0) {
        /*synchronized(this) {
            this.owner = $$0;
        }*/

        this.updateOwnerProfile();
    }

    private void updateOwnerProfile() {
        /*updateGameprofile(this.owner, ($$0) -> {
            this.owner = $$0;
            this.setChanged();
        });*/
    }

    public static void updateGameprofile(@Nullable GameProfile $$0, Consumer<GameProfile> $$1) {


        /*if ($$0 != null && !StringUtil.isNullOrEmpty($$0.getName()) && (!$$0.isComplete() || !$$0.getProperties().containsKey("textures")) && profileCache != null && sessionService != null) {
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
                        GameProfileCache $$6 = profileCache;
                        if ($$6 != null) {
                            $$6.add(final$$1x);
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
        }*/
    }
}
