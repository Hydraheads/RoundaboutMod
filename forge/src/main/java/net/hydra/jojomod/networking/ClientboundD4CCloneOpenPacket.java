package net.hydra.jojomod.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundD4CCloneOpenPacket {
    protected final int containerId;
    protected final int entityId;


    public ClientboundD4CCloneOpenPacket(int containerId, int entityId) {
        this.containerId = containerId;
        this.entityId = entityId;
    }

    public static void encode(ClientboundD4CCloneOpenPacket message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.containerId);
        buffer.writeInt(message.entityId);
    }

    public static ClientboundD4CCloneOpenPacket decode(FriendlyByteBuf buffer) {
        return new ClientboundD4CCloneOpenPacket(buffer.readUnsignedByte(), buffer.readInt());
    }

    public static void handle(ClientboundD4CCloneOpenPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleClientboundD4CCloneOpenPacket(message, context));
        });

        context.get().setPacketHandled(true);
    }
}
