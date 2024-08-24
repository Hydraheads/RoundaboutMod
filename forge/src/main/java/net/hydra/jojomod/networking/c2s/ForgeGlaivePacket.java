package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.item.GlaiveItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeGlaivePacket {
    private final int target;
    private final ItemStack glaive;

    public ForgeGlaivePacket(int target, ItemStack glaive){
        this.target = target;
        this.glaive = glaive;
    }
    public ForgeGlaivePacket(FriendlyByteBuf buf){
        this.target = buf.readInt();
        this.glaive = buf.readItem();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(target);
        buf.writeItem(glaive);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Entity entity = player.level().getEntity(target);
                if (entity != null && glaive.getItem() instanceof GlaiveItem) {
                    ((GlaiveItem)glaive.getItem()).glaiveAttack(glaive,player.level(),player,entity);
                }
            }
        });
        return true;
    }
}
