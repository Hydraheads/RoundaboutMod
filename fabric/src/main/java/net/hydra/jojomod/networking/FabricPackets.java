package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.access.IPacketAccess;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.packet.c2s.MoveSyncPacket;
import net.hydra.jojomod.networking.packet.c2s.StandAbilityPacket;
import net.hydra.jojomod.networking.packet.s2c.CooldownSyncPacket;
import net.hydra.jojomod.networking.packet.s2c.NbtSyncPacket;
import net.hydra.jojomod.networking.packet.s2c.SoundStopPacket;
import net.hydra.jojomod.networking.packet.s2c.StandS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class FabricPackets implements IPacketAccess {
    @Override
    public void StandGuardPointPacket(ServerPlayer sp, float guard, boolean broken) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeFloat(guard);
        buffer.writeBoolean(broken);
        ServerPlayNetworking.send(sp,ModMessages.STAND_GUARD_POINT_ID, buffer);
    }
    @Override
    public void DazeTimePacket(ServerPlayer sp, byte dazeTime) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeByte(dazeTime);
        ServerPlayNetworking.send(sp,ModMessages.DAZE_ID, buffer);
    }

    @Override
    public void NBTSyncPacket(ServerPlayer sp, CompoundTag NBT) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeNbt(NBT);
        ServerPlayNetworking.send(sp, ModMessages.NBT_SYNC_ID, buffer);
    }

    @Override
    public void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring,
                                   byte activePower, byte activePowerPhase){
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(attackTime);
        buf.writeInt(attackTimeMax);
        buf.writeInt(attackTimeDuring);
        buf.writeByte(activePower);
        buf.writeByte(activePowerPhase);
        ServerPlayNetworking.send(sp, ModMessages.POWER_COOLDOWN_SYNC_ID, buf);

    }
    @Override
    public void updateClashPacket(ServerPlayer sp, int id, float clashProgress){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(id);
        buffer.writeFloat(clashProgress);
        ServerPlayNetworking.send(sp,ModMessages.BARRAGE_CLASH_UPDATE_S2C_PACKET, buffer);
    }

    @Override
    public void stopSoundPacket(ServerPlayer sp, int id){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(id);
        ServerPlayNetworking.send(sp,ModMessages.SOUND_CANCEL_ID, buffer);
    }

    @Override
    public void startSoundPacket(ServerPlayer sp, int id, byte soundNo){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(id);
        buffer.writeByte(soundNo);
        ServerPlayNetworking.send(sp,ModMessages.SOUND_PLAY_ID, buffer);
    }

    public void timeStoppingEntityPacket(ServerPlayer sp, int entityID, boolean remove){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(entityID);
        buffer.writeBoolean(remove);
        ServerPlayNetworking.send(sp,ModMessages.TIME_STOP_ENTITY_PACKET, buffer);
    }



    @Override
    public void StandGuardCancelClientPacket(){
        ClientPlayNetworking.send(ModMessages.STAND_GUARD_CANCEL_PACKET, PacketByteBufs.create());
    }
    @Override
    public void StandPowerPacket(byte power){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeByte(power);
        ClientPlayNetworking.send(ModMessages.STAND_POWER_PACKET, buffer);
    }

    @Override
    public void StandPunchPacket(int targetID, byte APP){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(targetID);
        buffer.writeByte(APP);
        ClientPlayNetworking.send(ModMessages.STAND_PUNCH_PACKET, buffer);
    }
    @Override
    public void StandBarrageHitPacket(int targetID, int ATD){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(targetID);
        buffer.writeInt(ATD);
        ClientPlayNetworking.send(ModMessages.STAND_BARRAGE_HIT_PACKET, buffer);
    }

    @Override
    public void updateClashPacket(float clashProgress, boolean clashDone){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeFloat(clashProgress);
        buffer.writeBoolean(clashDone);
        ClientPlayNetworking.send(ModMessages.BARRAGE_CLASH_UPDATE_PACKET, buffer);
    }

    @Override
    public void moveSyncPacket(byte forward, byte strafe){
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(forward);
        buffer.writeByte(strafe);
        ClientPlayNetworking.send(ModMessages.MOVE_SYNC_ID, buffer);
    }
    @Override
    public void standSummonPacket(){
        ClientPlayNetworking.send(ModMessages.STAND_SUMMON_PACKET, PacketByteBufs.create());
    }

}
