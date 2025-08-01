package net.hydra.jojomod.networking;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.hydra.jojomod.access.IPacketAccess;
import net.hydra.jojomod.networking.c2s.*;
import net.hydra.jojomod.networking.s2c.*;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.Networking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ForgePackets implements IPacketAccess {

    @Override
    public void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring, byte activePower, byte activePowerPhase) {
        ForgePacketHandler.sendToClient(new ForgeCDSyncPacket(attackTime,attackTimeMax,attackTimeDuring,activePower,activePowerPhase), sp);
    }

    @Override
    public void syncSkillCooldownPacket(ServerPlayer sp, byte moveOnCooldown, int cooldown) {
        ForgePacketHandler.sendToClient(new ForgeSkillCDSyncPacket(moveOnCooldown,cooldown), sp);
    }
    @Override
    public void syncSkillCooldownPacket(ServerPlayer sp, byte moveOnCooldown, int cooldown, int max) {
        ForgePacketHandler.sendToClient(new ForgeSkillCDSyncMaxPacket(moveOnCooldown,cooldown,max), sp);
    }

    @Override
    public void stopSoundPacket(ServerPlayer sp, int id, byte soundId) {
        ForgePacketHandler.sendToClient(new ForgeStopSoundPacket(id,soundId), sp);
    }

    @Override
    public void startSoundPacket(ServerPlayer sp, int id, byte soundNo) {
        ForgePacketHandler.sendToClient(new ForgePlaySoundPacket(id, soundNo), sp);
    }

    @Override
    public void timeStoppingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, int duration, int maxDuration) {
        ForgePacketHandler.sendToClient(new ForgeTimeStoppingEntityPacket(entityID, x,y,z, range, duration, maxDuration), sp);
    }
    @Override
    public void timeStoppingEntityRemovalPacket(ServerPlayer sp, int entityID) {
        ForgePacketHandler.sendToClient(new ForgeTimeStoppingEntityRemovalPacket(entityID), sp);
    }
    @Override
    public void permaCastingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, byte context) {
        ForgePacketHandler.sendToClient(new ForgePermaCastingEntityPacket(entityID, x,y,z, range, context), sp);
    }
    @Override
    public void permaCastingEntityRemovalPacket(ServerPlayer sp, int entityID) {
        ForgePacketHandler.sendToClient(new ForgePermaCastingEntityRemovalPacket(entityID), sp);
    }

    @Override
    public void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i) {
        ForgePacketHandler.sendToClient(new ForgeBlockEntityResumeTSPacket(vec3i), sp);
    }

    @Override
    public void sendFloatPowerPacket(ServerPlayer sp, byte activePower, float data) {
        ForgePacketHandler.sendToClient(new ForgePowerFloatPacket(activePower,data), sp);
    }
    @Override
    public void sendIntPowerPacket(ServerPlayer sp, byte activePower, int data) {
        ForgePacketHandler.sendToClient(new ForgePowerIntPacket(activePower,data), sp);
    }
    @Override
    public void sendBundlePacket(ServerPlayer sp, byte context, byte one, byte two, byte three) {
        ForgePacketHandler.sendToClient(new ForgeBundlePacket(context,one,two,three), sp);
    }
    @Override
    public void sendBlipPacket(ServerPlayer sp, byte activePower, int data, Vector3f blip){
        ForgePacketHandler.sendToClient(new ForgeBlipPacket(activePower,data, blip), sp);
    }
    @Override
    public void sendIntPacket(ServerPlayer sp, byte activePower, int data) {
        ForgePacketHandler.sendToClient(new ForgeGenericIntPacket(activePower,data), sp);
    }
    @Override
    public void sendSimpleByte(ServerPlayer sp, byte context) {
        ForgePacketHandler.sendToClient(new ForgeSimpleBytePacket(context), sp);
    }
    @Override
    public void s2cPowerInventorySettings(ServerPlayer sp, int anchorPlace, float distanceOut, float idleOpacity,
                                          float combatOpacity, float enemyOpacity, int anchorPlaceAttack) {
        ForgePacketHandler.sendToClient(new ForgeS2CPowerInventorySettingsPacket(anchorPlace,
                distanceOut, idleOpacity, combatOpacity, enemyOpacity,anchorPlaceAttack), sp);
    }

    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create();



    @Override
    @SuppressWarnings("deprecation") // markWorldsDirty
    public void sendNewDynamicWorld(ServerPlayer sp, String name, ServerLevel level, @Nullable ServerPlayer player) {
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(level));
        level.getServer().markWorldsDirty();

        int id = -1;
        if (player != null)
            id = player.getId();

        ForgePacketHandler.sendToClient(new ForgeDynamicWorldSync(name), sp);
    }

    @Override
    public void deregisterDynamicWorld(ServerPlayer sp, String name) {

    }

    @Override
    public void ejectPRunning(ServerPlayer sp) {
        ForgePacketHandler.sendToClient(new ForgeEjectPRunning(), sp);
    }

}
