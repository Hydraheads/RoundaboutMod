package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeSkillCDSyncMaxPacket {
    private final byte power;
    private final int cooldown;
    private final int maxCooldown;

    public ForgeSkillCDSyncMaxPacket(byte power, int cooldown, int maxCooldown){
        this.power = power;
        this.cooldown = cooldown;
        this.maxCooldown = maxCooldown;
    }
    public ForgeSkillCDSyncMaxPacket(FriendlyByteBuf buf){
        this.power = buf.readByte();
        this.cooldown = buf.readInt();
        this.maxCooldown = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(power);
        buf.writeInt(cooldown);
        buf.writeInt(maxCooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.skillMaxCDSyncPacket(power, cooldown,maxCooldown);
        });
        return true;
    }
}
