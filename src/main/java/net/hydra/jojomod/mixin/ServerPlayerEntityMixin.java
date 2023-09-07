package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.ServerPlayerAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.hydra.jojomod.stand.StandData.syncStandActive;

@Mixin(ServerPlayerEntity.class)
    public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerAccess {
        public int compatSync = 0;

        public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
            super(world, pos, yaw, gameProfile);
        }

        @Inject(method = "playerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;tick()V", shift = At.Shift.AFTER))
        public void playerTickMixin(CallbackInfo info) {
            if (compatSync > 0) {
                compatSync--;
                if (compatSync == 1) {
                    syncStandActive(false, (ServerPlayerEntity) (Object) this);
                }
            }
        }


        @Override
    public void compatSync() {
           RoundaboutMod.LOGGER.info("LMAO " + 5);
        compatSync = 5;
    }
}