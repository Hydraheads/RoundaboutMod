package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.ServerPlayerAccess;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.hydra.jojomod.stand.NBTData.syncModNbt;

@Mixin(ServerPlayer.class)
    public abstract class PlayerEntityServer extends Player implements ServerPlayerAccess {



        /** This code makes sure stand is summoned properly when switching dimensions or performing other tasks*/
        public int compatSync = 2;

        public PlayerEntityServer(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
            super(world, pos, yaw, gameProfile);
        }

        @Inject(method = "doTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;tick()V", shift = At.Shift.AFTER))
        public void playerTickMixin(CallbackInfo info) {
            if (compatSync > 0) {
                compatSync--;
                if (compatSync == 1) {
                }
            }
        }

    /**Make NBT save on death*/
    @Inject(method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V", at = @At(value = "TAIL"))
    public void roundabout$restoreFrom(ServerPlayer $$0, boolean $$1, CallbackInfo info) {
        ((StandUser)this).roundabout$setStandDisc(((StandUser)$$0).roundabout$getStandDisc());
    }


    @Override
    public void compatSync() {
            //MyComponents.STAND_USER.get(((ServerPlayerEntity) (Object) this)).sync();
            syncModNbt((ServerPlayer) (Object) this);
             ((StandUser)((ServerPlayer) (Object) this)).summonStand(this.level(), true,false);
        compatSync = 5;
    }
}