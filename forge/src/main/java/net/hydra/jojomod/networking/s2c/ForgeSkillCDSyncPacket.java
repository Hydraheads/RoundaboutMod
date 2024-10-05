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
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                StandPowers powers = ((StandUser) player).roundabout$getStandPowers();
                powers.setCooldown(power,cooldown);
            }
        });
        return true;
    }
}
