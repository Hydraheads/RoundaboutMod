package net.hydra.jojomod.client;

import com.google.common.collect.Maps;
import net.hydra.jojomod.access.ICamera;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.gui.*;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.hydra.jojomod.item.BodyBagItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


public class ClientUtil {
    public static int checkthis = 0;
    public static int checkthisdat = 0;

    /**
     * A generalized packet for sending ints to the client. Context is what to do with the data int
     */
    public static void handleIntPacketS2C(LocalPlayer player, int data, byte context) {
        if (context == 1) {
            ((StandUser) player).roundabout$setGasolineTime(data);
        } else if (context == PacketDataIndex.S2C_POWER_INVENTORY) {
            checkthisdat = data;
            checkthis = 1;
        } else if (context == PacketDataIndex.S2C_INT_OXYGEN_TANK) {
            ((StandUser) player).roundabout$getStandPowers().setAirAmount(data);
        } else if (context== PacketDataIndex.S2C_INT_GRAB_ITEM){
            Entity target = player.level().getEntity(data);
            if (target instanceof ItemEntity IE) {
                IE.getItem().shrink(1);
            }
        } else if (context== PacketDataIndex.S2C_INT_ATD){
            ((StandUser) player).roundabout$getStandPowers().setAttackTimeDuring(data);
        } else if (context == PacketDataIndex.S2C_INT_SEAL){
            if (data > 0){
                ((StandUser) player).roundabout$setMaxSealedTicks(data);
                ((StandUser) player).roundabout$setSealedTicks(data);
            }
        }
    }

    public static boolean canSeeStands(Player lp){
        return !(lp != null && (((StandUser)lp).roundabout$getStandDisc().isEmpty() &&
                !lp.isSpectator()) && ConfigManager.getClientConfig().onlyStandUsersCanSeeStands);
    }
    public static boolean checkIfClientHoldingBag() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getMainHandItem().getItem() instanceof BodyBagItem
            || player.getOffhandItem().getItem() instanceof BodyBagItem){
                return true;
            }
        }
        return false;
    }
    public static boolean checkIfGamePaused() {
        return Minecraft.getInstance().isPaused();
    }
    public static boolean checkIfStandIsYoursAndFirstPerson(StandEntity stand) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (stand.getUser() != null){
                if (stand.getUser().is(player)){
                    if (Minecraft.getInstance().options.getCameraType().isFirstPerson()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkIfStandIsYoursAndFirstPersonandPiloting(StandEntity stand) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (stand.getUser() != null){
                if (stand.getUser().is(player)){
                    if (Minecraft.getInstance().options.getCameraType().isFirstPerson()){
                        if (((StandUser)stand.getUser()).roundabout$getStandPowers().isPiloting()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public static boolean setCameraEntity(Entity entity) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        ((ICamera)camera).roundabout$setEntity(entity);
        return entity != null && entity.isAlive() && !entity.isRemoved();
    }

    public static int getOutlineColor(Entity entity) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            if (powers.isPiloting()) {
                LivingEntity ent = powers.getPilotingStand();
                if (ent != null && powers instanceof PowersJustice) {
                    Entity TE = MainUtil.getTargetEntity(ent,100,10);
                    if (TE != null && TE.is(entity) && !(TE instanceof StandEntity && !TE.isAttackable())) {
                        Vec3 vec3d = ent.getEyePosition(0);
                        Vec3 vec3d2 = ent.getViewVector(0);
                        Vec3 vec3d3 = vec3d.add(vec3d2.x * 100, vec3d2.y * 100, vec3d2.z * 100);
                        BlockHitResult blockHit = ent.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, ent));
                        if ((blockHit.distanceTo(ent)-1) < ent.distanceToSqr(entity)){
                        } else {
                            if (TE.is(player)){
                                return 16701501;
                            } else {
                                if (TE instanceof FallenMob fm && fm.getController() == player.getId()){
                                    if (fm.getSelected()){
                                        return 1503183;
                                    } else {
                                        return 1503059;
                                    }

                                    //Blue -> 3972095
                                    //Green -> 8385147
                                }
                                return 14233126;
                            }
                        }


                        // 3847130 corpse
                    }
                    if (entity instanceof FallenMob fm){
                        if (fm.getSelected() && fm.getController() == player.getId()){
                            return 3972095;
                        }
                    }
                }
            }
        }

        if (entity instanceof LivingEntity LE){
            int yes = ((StandUser)LE).roundabout$getDetectTicks();
            if (yes > -1){
                return 16285219;
            }
        }

        return  -1;
    }
    public static int wasFrozen = 0;
    public static boolean getWasFrozen(){
        return wasFrozen != 0;
    }
    public static boolean getScreenFreeze(){
        if (ConfigManager.getClientConfig().timeStopSettings.timeStopFreezesScreen) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                boolean canTS = ((TimeStop) player.level()).CanTimeStopEntity(player);
                if (canTS) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPlayer(Entity PE){
        if (PE != null){
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                return PE.is(player);
            }
        }
        return false;
    }
    public static Player getPlayer(){
        return Minecraft.getInstance().player;
    }

    public static boolean poseHeld = false;
    public static void strikePose(Player player, Minecraft C, boolean keyIsDown, Options option) {
        if (keyIsDown){
            if (!poseHeld){
                C.setScreen(new PoseSwitcherScreen());
            }
            poseHeld = true;
        } else {
            if (poseHeld){
                poseHeld = false;
            }
        }
    }
    public static void openCorpseBag(ItemStack stack) {
        Minecraft client = Minecraft.getInstance();
        client.setScreen(new CorpseBagScreen(stack));
    }

    public static void handleIntPacketS2C(int data, byte context) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            handleIntPacketS2C(player, data, context);
        }
    }

    /**
     * A generalized packet for sending ints to the client. Context is what to do with the data int
     */
    public static void handleBlipPacketS2C(LocalPlayer player, int data, byte context, Vector3f vec) {
        if (context == 2) {
            /*This code makes the world using mobs appear to teleport by skipping interpolation*/
            Entity target = player.level().getEntity(data);
            if (target instanceof LivingEntity LE) {
                ((StandUser) target).roundabout$setBlip(vec);

                StandEntity SE = ((StandUser) target).roundabout$getStand();
                if (SE != null) {
                    ((StandUser) SE).roundabout$setBlip(vec);
                }
            }
        }
    }

    public static void clashUpdatePacket(int clashOpID, float progress) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$getStandPowers().setClashOp((LivingEntity) player.level().getEntity(clashOpID));
            ((StandUser) player).roundabout$getStandPowers().setClashOpProgress(progress);
        }
    }

    public static void handlePlaySoundPacket(int startPlayerID, byte soundQue) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Entity User = player.level().getEntity(startPlayerID);
            ((StandUserClient) User).roundabout$clientQueSound(soundQue);
        }
    }

    public static void setJusticeTacticsScreen() {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new JusticeTacticsScreen());
    }
    public static void setJusticeScreen() {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new JusticeMobSwitcherScreen());
    }
    public static void setJusticeBlockScreen() {
        ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_OPEN_FOG_INVENTORY);
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(
                new FogInventoryScreen(
                        mc.player, mc.player.connection.enabledFeatures(), mc.options.operatorItemsTab().get()
                ));
    }
    public static float getDelta() {
        Minecraft mc = Minecraft.getInstance();
        return mc.getDeltaFrameTime();
    }
    public static float getFrameTime() {
        Minecraft mc = Minecraft.getInstance();
        return mc.getFrameTime();
    }
    public static void handlePowerFloatPacket(byte activePower, float data){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$getStandPowers().updatePowerFloat(activePower,data);
        }
    }
    public static void handlePowerIntPacket(byte activePower, int data){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$getStandPowers().updatePowerInt(activePower,data);
        }
    }
    public static void handleGuardUpdate(float guardPoints, boolean guardBroken){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$setGuardPoints(guardPoints);
            ((StandUser) player).roundabout$setGuardBroken(guardBroken);
        }
    }
    public static void CDSyncPacket(int attackTime, int attackTimeMax, int attackTimeDuring,
                                    byte activePower, byte activePowerPhase){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            MainUtil.syncCooldownsForAttacks(attackTime, attackTimeMax, attackTimeDuring,
                    activePower, activePowerPhase, player);
        }
    }

    public static void skillCDSyncPacket(byte power, int cooldown){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            StandPowers powers = ((StandUser) player).roundabout$getStandPowers();
            powers.setCooldown(power,cooldown);
        }
    }

    public static void handleEntityResumeTsPacket(Vec3i vec3i){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            BlockEntity openedBlock = player.level().getBlockEntity(new BlockPos(vec3i.getX(),vec3i.getY(),vec3i.getZ()) );
            if (openedBlock != null){
                ((TimeStop) player.level()).processTSBlockEntityPacket(openedBlock);
            }
        }
    }

    public static void updateDazePacket(byte dazeTime){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$setDazeTime(dazeTime);
        }
    }
    public static void handleBlipPacketS2C(int data, byte context, Vector3f vec){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
             handleBlipPacketS2C(player,data,context,vec);
        }
    }
    public static void handleBundlePacketS2C(byte context, byte one, byte two, byte three){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            handleBundlePacketS2C(player,context,one,two,three);
        }
    }

    public static void handleTimeStoppingEntityPacket(int timeStoppingEntity, double x, double y, double z, double range, int duration, int maxDuration){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((TimeStop) player.level()).processTSPacket(timeStoppingEntity,x,y,z,range,duration,maxDuration);
        }
    }

    public static void processTSRemovePacket(int entityID){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((TimeStop) player.level()).processTSRemovePacket(entityID);
        }
    }

    public static void handlePermaCastingEntityPacket(int timeStoppingEntity, double x, double y, double z, double range, byte context){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((IPermaCasting) player.level()).roundabout$processPermaCastPacket(timeStoppingEntity,x,y,z,range,context);
        }
    }
    public static void handlePermaCastingRemovePacket(int entityID){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((IPermaCasting) player.level()).roundabout$processPermaCastRemovePacket(entityID);
        }
    }
    public static void handleStopSoundPacket(int data, byte context){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Entity User = player.level().getEntity(data);
            ((StandUserClient)User).roundabout$clientQueSoundCanceling(context);
        }
    }

    public static void tickTSFreezeScreen() {
        if (ConfigManager.getClientConfig().timeStopSettings.timeStopFreezesScreen) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && mc.level != null) {
                if (((TimeStop) mc.level).CanTimeStopEntity(player)) {
                    if (mc.screen == null) {
                        mc.setScreen(new PauseTSScreen(false));
                    }
                }
            }
        }
    }

    public void pauseGame(boolean $$0) {
    }


    /**A generalized packet for sending bytes to the client. Only a context is provided.*/
    public static void handleBundlePacketS2C(LocalPlayer player, byte context, byte byte1, byte byte2, byte byte3){
        if (context == PacketDataIndex.S2C_BUNDLE_POWER_INV){
            IPlayerEntity ple = ((IPlayerEntity) player);
            StandUser se = ((StandUser) player);
            if (byte2 > 0) {
                ple.roundabout$setUnlockedBonusSkin(true);
            } else {
                ple.roundabout$setUnlockedBonusSkin(false);
            }
            se.roundabout$setStandSkin(byte1);

        }
    }

    public static void handleLPPowerInventoryOptionsPacketS2C(int anchorPlace, float distanceOut, float idleOpacity,
                                                            float combatOpacity, float enemyOpacity){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            IPlayerEntity ple = ((IPlayerEntity) player);
            ple.roundabout$setAnchorPlace(anchorPlace);
            ple.roundabout$setDistanceOut(distanceOut);
            ple.roundabout$setSizePercent(idleOpacity);
            ple.roundabout$setIdleRotation(combatOpacity);
            ple.roundabout$setIdleYOffset(enemyOpacity);
        }

    }
    public static void handlePowerInventoryOptionsPacketS2C(LocalPlayer player, int anchorPlace, float distanceOut, float idleOpacity,
                                                            float combatOpacity, float enemyOpacity){
        IPlayerEntity ple = ((IPlayerEntity) player);
        ple.roundabout$setAnchorPlace(anchorPlace);
        ple.roundabout$setDistanceOut(distanceOut);
        ple.roundabout$setSizePercent(idleOpacity);
        ple.roundabout$setIdleRotation(combatOpacity);
        ple.roundabout$setIdleYOffset(enemyOpacity);

    }
    /**A generalized packet for sending bytes to the client. Only a context is provided.*/
    public static void handleSimpleBytePacketS2C(LocalPlayer player, byte context){
        if (context == 1) {
            ((StandUser) player).roundabout$setGasolineTime(context);
        } else if (context == PacketDataIndex.S2C_SIMPLE_FREEZE_STAND) {
            if (((StandUser)player).roundabout$getStandPowers().hasCooldowns()) {
                int punishTicks = ClientNetworking.getAppropriateConfig().cooldownsInTicks.switchStandDiscWhileOnCooldowns;
                if (punishTicks > 0){
                    ((StandUser) player).roundabout$setMaxSealedTicks(punishTicks);
                    ((StandUser) player).roundabout$setSealedTicks(punishTicks);
                }
            } else {
                int switchTicks = ClientNetworking.getAppropriateConfig().cooldownsInTicks.switchStandDisc;
                if (switchTicks > 0){
                    ((StandUser) player).roundabout$setMaxSealedTicks(switchTicks);
                    ((StandUser) player).roundabout$setSealedTicks(switchTicks);
                }
            }
        } else if (context == PacketDataIndex.S2C_SIMPLE_SUSPEND_RIGHT_CLICK) {
            ((StandUser) player).roundabout$getStandPowers().suspendGuard = true;
            ((StandUser) player).roundabout$getStandPowers().scopeLevel = 0;
        }
    } public static void handleSimpleBytePacketS2C(byte context){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            handleSimpleBytePacketS2C(player,context);
        }
    }
}
