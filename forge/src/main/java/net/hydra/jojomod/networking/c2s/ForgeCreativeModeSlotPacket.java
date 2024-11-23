package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeCreativeModeSlotPacket {
    private final int slotNo;
    private final ItemStack stack;
    private final byte cont;

    public ForgeCreativeModeSlotPacket(int slotNo, ItemStack stack, byte dataContext) {
        this.slotNo = slotNo;
        this.stack = stack;
        this.cont = dataContext;
    }

    public ForgeCreativeModeSlotPacket(FriendlyByteBuf buf) {
        this.slotNo = buf.readInt();
        this.stack = buf.readItem();
        this.cont = buf.readByte();
        ;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slotNo);
        buf.writeItem(stack);
        buf.writeByte(cont);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                MainUtil.handleSetCreativeModeSlot(player, slotNo, stack, cont);
            }
        });
        return true;
    }
}
