package net.hydra.jojomod.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundBlackSabbathOpenPacket {
    protected final int containerId;
    protected final int entityId;


    public ClientboundBlackSabbathOpenPacket(int containerId, int entityId) {
        this.containerId = containerId;
        this.entityId = entityId;
    }

    public static void encode(ClientboundBlackSabbathOpenPacket message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.containerId);
        buffer.writeInt(message.entityId);
    }

    public static ClientboundBlackSabbathOpenPacket decode(FriendlyByteBuf buffer) {
        return new ClientboundBlackSabbathOpenPacket(buffer.readUnsignedByte(), buffer.readInt());
    }

    public static void handle(ClientboundBlackSabbathOpenPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleClientboundBlackSabbathOpenPacket(message, context));
        });

        context.get().setPacketHandled(true);
    }
}
