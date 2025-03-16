package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.access.IPacketAccess;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.Networking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.swing.text.html.parser.Entity;

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
    public void syncSkillCooldownPacket(ServerPlayer sp, byte moveOnCooldown, int cooldown) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(moveOnCooldown);
        buf.writeInt(cooldown);
        ServerPlayNetworking.send(sp, ModMessages.SKILL_COOLDOWN_SYNC_ID, buf);
    }

    @Override
    public void updateClashPacket(ServerPlayer sp, int id, float clashProgress){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(id);
        buffer.writeFloat(clashProgress);
        ServerPlayNetworking.send(sp,ModMessages.BARRAGE_CLASH_UPDATE_S2C_PACKET, buffer);
    }

    @Override
    public void stopSoundPacket(ServerPlayer sp, int id, byte soundNo){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(id);
        buffer.writeByte(soundNo);
        ServerPlayNetworking.send(sp,ModMessages.SOUND_CANCEL_ID, buffer);
    }

    @Override
    public void startSoundPacket(ServerPlayer sp, int id, byte soundNo){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(id);
        buffer.writeByte(soundNo);
        ServerPlayNetworking.send(sp,ModMessages.SOUND_PLAY_ID, buffer);
    }

    @Override
    public void timeStoppingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, int chargeTime, int maxChargeTime) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(entityID);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeDouble(range);
        buffer.writeInt(chargeTime);
        buffer.writeInt(maxChargeTime);
        ServerPlayNetworking.send(sp,ModMessages.TIME_STOP_ENTITY_PACKET, buffer);
    }

    @Override
    public void timeStoppingEntityRemovalPacket(ServerPlayer sp, int entityID) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(entityID);
        ServerPlayNetworking.send(sp,ModMessages.TIME_STOP_ENTITY_REMOVAL_PACKET, buffer);
    }



    @Override
    public void permaCastingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range,
                                         byte context) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(entityID);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeDouble(range);
        buffer.writeByte(context);
        ServerPlayNetworking.send(sp,ModMessages.PERMA_CASTING_ENTITY_PACKET, buffer);
    }

    @Override
    public void permaCastingEntityRemovalPacket(ServerPlayer sp, int entityID) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(entityID);
        ServerPlayNetworking.send(sp,ModMessages.PERMA_CASTING_ENTITY_REMOVAL_PACKET, buffer);
    }

    @Override
    public void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeInt(vec3i.getX());
        buffer.writeInt(vec3i.getY());
        buffer.writeInt(vec3i.getZ());
        ServerPlayNetworking.send(sp, ModMessages.RESUME_TILE_ENTITY_TS_PACKET, buffer);
    }
    @Override
    public void sendFloatPowerPacket(ServerPlayer sp, byte activePower, float data) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(activePower);
        buffer.writeFloat(data);
        ServerPlayNetworking.send(sp, ModMessages.SEND_FLOAT_POWER_DATA_PACKET, buffer);
    }

    @Override
    public void sendIntPowerPacket(ServerPlayer sp, byte activePower, int data) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(activePower);
        buffer.writeInt(data);
        ServerPlayNetworking.send(sp, ModMessages.SEND_INT_POWER_DATA_PACKET, buffer);
    }

    @Override
    public void sendIntPacket(ServerPlayer sp, byte activePower, int data) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(activePower);
        buffer.writeInt(data);
        ServerPlayNetworking.send(sp, ModMessages.SEND_INT_DATA_PACKET, buffer);
    }


    @Override
    public void sendBundlePacket(ServerPlayer sp, byte context, byte one, byte two, byte three) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(context);
        buffer.writeByte(one);
        buffer.writeByte(two);
        buffer.writeByte(three);
        ServerPlayNetworking.send(sp, ModMessages.SEND_BUNDLE_PACKET, buffer);
    }


    @Override
    public void sendConfig(ServerPlayer sp) {
        String serialized = ConfigManager.serializeConfig();
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(Networking.isDedicated());
        buf.writeUtf(serialized);
        ServerPlayNetworking.send(sp, ModMessages.CONFIG_SYNC, buf);
    }

    @Override
    public void sendNewDynamicWorld(ServerPlayer sp, String name) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUtf(name);

        ServerPlayNetworking.send(sp, ModMessages.DYNAMIC_WORLD_SYNC, buf);
    }

    @Override
    public void sendBlipPacket(ServerPlayer sp, byte activePower, int data, Vector3f vec) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(activePower);
        buffer.writeInt(data);
        buffer.writeVector3f(vec);
        ServerPlayNetworking.send(sp, ModMessages.BLIP_PACKET, buffer);
    }

    @Override
    public void sendSimpleByte(ServerPlayer sp, byte context) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(context);
        ServerPlayNetworking.send(sp, ModMessages.SEND_SIMPLE_BYTE_PACKET, buffer);
    }
    @Override
    public void s2cPowerInventorySettings(ServerPlayer sp, int anchorPlace, float distanceOut, float idleOpacity,
                                          float combatOpacity, float enemyOpacity) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeInt(anchorPlace);
        buffer.writeFloat(distanceOut);
        buffer.writeFloat(idleOpacity);
        buffer.writeFloat(combatOpacity);
        buffer.writeFloat(enemyOpacity);
        ServerPlayNetworking.send(sp, ModMessages.SEND_S2C_POWER_INVENTORY_OPTIONS, buffer);
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
    public void StandChargedPowerPacket(byte power, int chargeTime){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeByte(power);
        buffer.writeInt(chargeTime);
        ClientPlayNetworking.send(ModMessages.STAND_CHARGED_POWER_PACKET, buffer);
    }

    @Override
    public void StandPosPowerPacket(byte power, BlockPos blockPos){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeByte(power);
        buffer.writeBlockPos(blockPos);
        ClientPlayNetworking.send(ModMessages.STAND_POS_POWER_PACKET, buffer);
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
    public void timeStopFloat(boolean TSJump) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeBoolean(TSJump);
        ClientPlayNetworking.send(ModMessages.TIME_STOP_JUMP_ID, buffer);
    }
    @Override
    public void updatePilot(LivingEntity entity) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeFloat((float) entity.getX());
        buffer.writeFloat((float) entity.getY());
        buffer.writeFloat((float) entity.getZ());
        buffer.writeFloat(entity.getYRot());
        buffer.writeFloat(entity.getXRot());
        buffer.writeInt(entity.getId());
        ClientPlayNetworking.send(ModMessages.STAND_PILOT_ID, buffer);
    }

    @Override
    public void standSummonPacket(){
        ClientPlayNetworking.send(ModMessages.STAND_SUMMON_PACKET, PacketByteBufs.create());
    }

    @Override
    public void byteToServerPacket(byte value, byte context){
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(value);
        buffer.writeByte(context);
        ClientPlayNetworking.send(ModMessages.BYTE_C2S_PACKET, buffer);
    }
    @Override
    public void floatToServerPacket(float value, byte context){
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeFloat(value);
        buffer.writeByte(context);

        ClientPlayNetworking.send(ModMessages.FLOAT_C2S_PACKET, buffer);
    }
    @Override
    public void intToServerPacket(int value, byte context){
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeInt(value);
        buffer.writeByte(context);
        ClientPlayNetworking.send(ModMessages.INT_C2S_PACKET, buffer);
    }
    @Override
    public void singleByteToServerPacket(byte context){
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(context);
        ClientPlayNetworking.send(ModMessages.SINGLE_BYTE_C2S_PACKET, buffer);
    }
    @Override
    public void inventoryToServer(int slotNum, ItemStack stack, byte context){
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeInt(slotNum);
        buffer.writeItem(stack);
        buffer.writeByte(context);
        ClientPlayNetworking.send(ModMessages.INVENTORY_C2S_PACKET, buffer);
    }
    @Override
    public void itemContextToServer(byte context, ItemStack stack, byte context2, Vector3f vec){
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(context);
        buffer.writeItem(stack);
        buffer.writeByte(context2);
        buffer.writeVector3f(vec);
        ClientPlayNetworking.send(ModMessages.ITEM_CONTEXT_C2S_PACKET, buffer);
    }
    @Override
    public void glaivePacket(ItemStack glaive, int target){
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeInt(target);
        buffer.writeItem(glaive);
        ClientPlayNetworking.send(ModMessages.GLAIVE_C2S_PACKET, buffer);
    }

    @Override
    public void handshake(){
        FriendlyByteBuf buffer = PacketByteBufs.create();
        ClientPlayNetworking.send(ModMessages.HANDSHAKE, buffer);
    }
}
