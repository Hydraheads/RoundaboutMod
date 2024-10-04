package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeNBTPacket {
    private final CompoundTag NBT;

    public ForgeNBTPacket(CompoundTag NBT){
        this.NBT = NBT;
    }
    public ForgeNBTPacket(FriendlyByteBuf buf){
        this.NBT = buf.readNbt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(NBT);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
            }
        });
        return true;
    }
}
