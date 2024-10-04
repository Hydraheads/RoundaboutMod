package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayer.class)
    public abstract class PlayerEntityServer extends Player {



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

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value = "TAIL"))
    public void roundabout$teleportTo(ServerLevel p_9000_, double p_9001_, double p_9002_, double p_9003_, float p_9004_, float p_9005_, CallbackInfo info) {
        if (p_9000_ == this.level()) {
            ((StandUser) this).roundabout$getFollowers().forEach($$0 -> {
                for (StandEntity $$1 : ((StandUser) this).roundabout$getFollowers()) {
                    $$1.moveTo(this.getX(),this.getY(),this.getZ());
                }
            });
            StandUser standUserData = ((StandUser) this);
            if (standUserData.hasStandOut()) {

                standUserData.updateStandOutPosition(standUserData.getStand(), Entity::moveTo);
            }
        }
    }

    /**Make NBT save on death*/
    @Inject(method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V", at = @At(value = "TAIL"))
    public void roundabout$restoreFrom(ServerPlayer $$0, boolean $$1, CallbackInfo info) {
        ((StandUser)this).roundabout$setStandDisc(((StandUser)$$0).roundabout$getStandDisc());
    }

}