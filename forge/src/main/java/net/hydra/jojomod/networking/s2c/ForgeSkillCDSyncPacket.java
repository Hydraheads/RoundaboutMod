package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeSkillCDSyncPacket {
    private final byte power;
    private final int cooldown;

    public ForgeSkillCDSyncPacket(byte power, int cooldown){
        this.power = power;
        this.cooldown = cooldown;
    }
    public ForgeSkillCDSyncPacket(FriendlyByteBuf buf){
        this.power = buf.readByte();
        this.cooldown = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(power);
        buf.writeInt(cooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            MainUtil.skillCDSyncPacket(power, cooldown);
        });
        return true;
    }
}
