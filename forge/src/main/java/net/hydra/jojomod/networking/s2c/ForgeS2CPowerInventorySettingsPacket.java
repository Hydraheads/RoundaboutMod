package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeS2CPowerInventorySettingsPacket {
    private final int anchorPlace;
    private final float distanceOut;
    private final float idleOpacity;
    private final float combatOpacity;
    private final float enemyOpacity;

    public ForgeS2CPowerInventorySettingsPacket(int anchorPlace, float distanceOut, float idleOpacity,
                             float combatOpacity, float enemyOpacity){
        this.anchorPlace = anchorPlace;
        this.distanceOut = distanceOut;
        this.idleOpacity = idleOpacity;
        this.combatOpacity = combatOpacity;
        this.enemyOpacity = enemyOpacity;
    }
    public ForgeS2CPowerInventorySettingsPacket(FriendlyByteBuf buf){
        this.anchorPlace = buf.readInt();
        this.distanceOut = buf.readFloat();
        this.idleOpacity = buf.readFloat();
        this.combatOpacity = buf.readFloat();
        this.enemyOpacity = buf.readFloat();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(anchorPlace);
        buf.writeFloat(distanceOut);
        buf.writeFloat(idleOpacity);
        buf.writeFloat(combatOpacity);
        buf.writeFloat(enemyOpacity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.handleLPPowerInventoryOptionsPacketS2C(anchorPlace,distanceOut,idleOpacity,combatOpacity,
                    enemyOpacity);
        });
        return true;
    }

}
