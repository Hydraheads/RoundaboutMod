package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.hydra.jojomod.Roundabout;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientboundD4CCloneOpenPacket(int cloneId, int containerId) implements FabricPacket {

    public static final PacketType<ClientboundD4CCloneOpenPacket> TYPE = PacketType.create(new ResourceLocation(Roundabout.MOD_ID, "d4c_clone_inventory_screen_packet"), ClientboundD4CCloneOpenPacket::new);

    public ClientboundD4CCloneOpenPacket(FriendlyByteBuf buf) {
        this(buf.readUnsignedByte(), buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeInt(this.cloneId);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }



}
