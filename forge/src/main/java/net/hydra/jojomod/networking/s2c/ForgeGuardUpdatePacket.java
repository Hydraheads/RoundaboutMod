package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeGuardUpdatePacket {
    private final float guardPoints;
    private final boolean guardBroken;

    public ForgeGuardUpdatePacket(float guardPoints, boolean guardBroken){
        this.guardPoints = guardPoints;
        this.guardBroken = guardBroken;
    }
    public ForgeGuardUpdatePacket(FriendlyByteBuf buf){
        this.guardPoints = buf.readFloat();
        this.guardBroken = buf.readBoolean();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(guardPoints);
        buf.writeBoolean(guardBroken);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            MainUtil.handleGuardUpdate(guardPoints,guardBroken);
        });
        return true;
    }
}
