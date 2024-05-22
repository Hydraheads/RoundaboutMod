package net.hydra.jojomod.mixin;

import net.hydra.jojomod.stand.NBTData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
    /** Makes absolutely certain NBT is saved and loaded from players...
     * only relevant so long as
     * @see ModEntityDataSaverMixin is.*/
    @Inject(method = "placeNewPlayer", at = @At(value = "TAIL"))
    private void onPlayerConnectMixin(Connection $$0, ServerPlayer $$1, CallbackInfo info) {
        NBTData.syncModNbt($$1);
    }

    @Inject(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addRespawnedPlayer(Lnet/minecraft/server/level/ServerPlayer;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void respawnPlayerMixin(ServerPlayer $$0, boolean $$1, CallbackInfoReturnable<ServerPlayer> info) {
        NBTData.syncModNbt($$0);
    }
}