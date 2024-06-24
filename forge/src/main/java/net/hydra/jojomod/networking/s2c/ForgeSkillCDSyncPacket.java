package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeSkillCDSyncPacket {
    private final byte power;
    private final int cooldown;
    private final int maxCooldown;

    public ForgeSkillCDSyncPacket(byte power, int cooldown, int maxCooldown){
        this.power = power;
        this.cooldown = cooldown;
        this.maxCooldown = maxCooldown;
    }
    public ForgeSkillCDSyncPacket(FriendlyByteBuf buf){
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
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                StandPowers powers = ((StandUser) player).getStandPowers();
                powers.setCooldown(power,cooldown,maxCooldown);
            }
        });
        return true;
    }
}
