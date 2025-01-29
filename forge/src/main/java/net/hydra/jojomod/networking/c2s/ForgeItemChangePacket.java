package net.hydra.jojomod.networking.c2s;


import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ForgeItemChangePacket {
    private final byte cont;
    private final ItemStack stack;
    private final byte cont2;
    private final Vector3f vec;

    public ForgeItemChangePacket(byte cont, ItemStack stack, byte cont2, Vector3f vec) {
        this.cont = cont;
        this.stack = stack;
        this.cont2 = cont2;
        this.vec = vec;
    }

    public ForgeItemChangePacket(FriendlyByteBuf buf) {
        this.cont = buf.readByte();
        this.stack = buf.readItem();
        this.cont2 = buf.readByte();
        this.vec =  buf.readVector3f();
        ;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(cont);
        buf.writeItem(stack);
        buf.writeByte(cont2);
        buf.writeVector3f(vec);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                MainUtil.handleChangeItem(player, cont, stack, cont2, vec);
            }
        });
        return true;
    }
}

