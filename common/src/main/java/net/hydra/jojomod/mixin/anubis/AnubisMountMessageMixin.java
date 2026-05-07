package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.entity.pathfinding.AnubisPossessorEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class AnubisMountMessageMixin {

    @Shadow
    private ClientLevel level;

    @Inject(method = "handleSetEntityPassengersPacket",at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"),cancellable = true)
    private void roundabout$cancelMountMessage(ClientboundSetPassengersPacket $$0, CallbackInfo ci) {
        Entity entity = this.level.getEntity($$0.getVehicle());
        if (entity instanceof AnubisPossessorEntity) {
            ci.cancel();
        }
    }
}
