package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.hydra.jojomod.Roundabout;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

import java.awt.*;

public record ClientboundBlackSabbathOpenPacket(int playerId, int containerId) implements FabricPacket {

    public static final PacketType<ClientboundBlackSabbathOpenPacket> TYPE = PacketType.create(new ResourceLocation(Roundabout.MOD_ID, "black_sabbath_chest_screen_packet"), ClientboundBlackSabbathOpenPacket::new);

    public ClientboundBlackSabbathOpenPacket(FriendlyByteBuf buf) {
        this(buf.readUnsignedByte(), buf.readInt());

    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeInt(this.playerId);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

}
