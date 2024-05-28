package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeCDSyncPacket {
    private final int attackTime;
    private final int attackTimeMax;
    private final int attackTimeDuring;
    private final byte activePower;
    private final byte activePowerPhase;

    public ForgeCDSyncPacket(int attackTime, int attackTimeMax, int attackTimeDuring, byte activePower, byte activePowerPhase){
        this.attackTime = attackTime;
        this.attackTimeMax = attackTimeMax;
        this.attackTimeDuring = attackTimeDuring;
        this.activePower = activePower;
        this.activePowerPhase = activePowerPhase;
    }
    public ForgeCDSyncPacket(FriendlyByteBuf buf){
        this.attackTime = buf.readInt();
        this.attackTimeMax = buf.readInt();
        this.attackTimeDuring = buf.readInt();
        this.activePower = buf.readByte();
        this.activePowerPhase = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(attackTime);
        buf.writeInt(attackTimeMax);
        buf.writeInt(attackTimeDuring);
        buf.writeByte(activePower);
        buf.writeByte(activePowerPhase);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                StandPowers powers = ((StandUser) player).getStandPowers();
                powers.setAttackTime(attackTime);
                powers.setAttackTimeMax(attackTimeMax);
                powers.setAttackTimeDuring(attackTimeDuring);
                powers.setActivePower(activePower);
                powers.setActivePowerPhase(activePowerPhase);
                powers.kickStartClient();
            }
        });
        return true;
    }
}
