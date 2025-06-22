package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ICamera;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.gui.*;
import net.zetalasis.client.shader.D4CShaderFX;
import net.zetalasis.client.shader.callback.RenderCallbackRegistry;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.hydra.jojomod.item.BodyBagItem;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.zetalasis.networking.message.api.ModMessageEvents;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.world.DynamicWorld;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;


public class ClientUtil {

    public static Matrix4f savedPose;
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
        } else if (context == PacketDataIndex.S2C_INT_BUBBLE_FINISH){
            Entity target = player.level().getEntity(data);
            if (target instanceof SoftAndWetPlunderBubbleEntity IE) {
                IE.setFinished(true);
                popSounds = IE;
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
    public static int mirrorCycles = 0;
    public static boolean checkIfGamePaused() {
        return Minecraft.getInstance().isPaused();
    }

    public static boolean checkIfFirstPerson() {
        return Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

    public static boolean checkIfIsFirstPerson(Player pl) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && pl != null && pl.is(player)) {
            return Minecraft.getInstance().options.getCameraType().isFirstPerson();
        }
        return false;
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
            if (powers.getGoBeyondTarget() != null && powers.getGoBeyondTarget().is(entity)) {
                return 10978493;
            } else if (powers.isPiloting()) {
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

            if (((StandUser) player).roundabout$getStand() instanceof D4CEntity)
            {
                if (entity instanceof D4CCloneEntity clone && clone.player != null && clone.player.equals(player) && player.isCrouching())
                {
                    if (clone.isSelected())
                    {
                        return 16701501;
                    }
                    else {
                        return 16777215;
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
    public static SoftAndWetPlunderBubbleEntity popSounds = null;
    public static boolean getWasFrozen(){
        return wasFrozen != 0;
    }
    public static boolean getScreenFreeze(){
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null && clientConfig.timeStopSettings.timeStopFreezesScreen) {
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

    public static void openPlunderScreen(){
        Minecraft.getInstance().setScreen(new PlunderScreen());
    }
    public static void openStandSwitchUI(ItemStack arrow){
        Minecraft.getInstance().setScreen(new StandArrowRerollScreen(arrow));
    }
    public static void openModificationVisageUI(ItemStack visage){
        Minecraft.getInstance().setScreen(new ModificationVisageScreen(visage));
    }
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

    public static void stopDestroyingBlock(){
        Minecraft.getInstance().gameMode.stopDestroyBlock();
    }

    public static void handlePlaySoundPacket(int startPlayerID, byte soundQue) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Entity User = player.level().getEntity(startPlayerID);
            ((StandUserClient) User).roundabout$clientQueSound(soundQue);
        }
    }

    public static boolean hasPlunderUI() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen instanceof PlunderScreen vsc;
    }
    public static boolean hasCinderellaShopUI() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen instanceof VisageStoreScreen vsc && vsc.costsEmeralds;
    }
    public static boolean hasCinderellaUI() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen instanceof VisageStoreScreen vsc && !vsc.costsEmeralds;
    }
    public static void setCinderellaUI() {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new VisageStoreScreen());
    }

    public static int isInCinderellaMobUI = -1;
    public static void setCinderellaUI(boolean costs, int entid) {
        Minecraft mc = Minecraft.getInstance();

        ModPacketHandler.PACKET_ACCESS.intToServerPacket(entid, PacketDataIndex.INT_RELLA_START);
        isInCinderellaMobUI = entid;
        mc.setScreen(new VisageStoreScreen(costs));
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

    /**Dynamic Worlds packet, removing local player calls so servers don't crash on startup*/
    public static boolean packetLocPlayCheck(){
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null)
        {
            Roundabout.LOGGER.error("Errored while synchronizing Dynamic World: \"player\" is null!");
            return false;
        }

        return true;
    }
    public static void dimensionSynchForge(ResourceKey<Level> LEVEL_KEY){
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            localPlayer.connection.levels().add(LEVEL_KEY);
            ModPacketHandler.PACKET_ACCESS.ackRegisterWorld();
        } else {
            packetLocPlayCheck();
        }
    }
    public static void dimensionSynchFabric(Minecraft client, ResourceKey<Level> LEVEL_KEY) {
        LocalPlayer player = client.player;
        player.connection.levels().add(LEVEL_KEY);

        ModMessageEvents.sendToServer(
                DynamicWorld.DynamicWorldNetMessages.MESSAGES.ADD_WORLD.value
                );
    }

    public static void d4cEjectParralelRunningForge(){
        Minecraft client = Minecraft.getInstance();

        if (client.player != null)
        {
            if (((StandUser)client.player).roundabout$getStandPowers() instanceof PowersD4C d4c)
            {
                d4c.ejectParallelRunning();
            }
        }
    }


    public static D4CShaderFX fx;

    static {
        fx = new D4CShaderFX();
        RenderCallbackRegistry.register(fx);
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
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null && clientConfig.timeStopSettings.timeStopFreezesScreen){
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
                                                            float combatOpacity, float enemyOpacity, int anchorPlaceAttack){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            IPlayerEntity ple = ((IPlayerEntity) player);
            ple.roundabout$setAnchorPlace(anchorPlace);
            ple.roundabout$setDistanceOut(distanceOut);
            ple.roundabout$setSizePercent(idleOpacity);
            ple.roundabout$setIdleRotation(combatOpacity);
            ple.roundabout$setIdleYOffset(enemyOpacity);
            ple.roundabout$setAnchorPlaceAttack(anchorPlaceAttack);
        }

    }
    public static void handlePowerInventoryOptionsPacketS2C(LocalPlayer player, int anchorPlace, float distanceOut, float idleOpacity,
                                                            float combatOpacity, float enemyOpacity, int anchorPlaceAttack){
        IPlayerEntity ple = ((IPlayerEntity) player);
        ple.roundabout$setAnchorPlace(anchorPlace);
        ple.roundabout$setDistanceOut(distanceOut);
        ple.roundabout$setSizePercent(idleOpacity);
        ple.roundabout$setIdleRotation(combatOpacity);
        ple.roundabout$setIdleYOffset(enemyOpacity);
        ple.roundabout$setAnchorPlaceAttack(anchorPlaceAttack);

    }
    /**A generalized packet for sending bytes to the client. Only a context is provided.*/
    public static void handleSimpleBytePacketS2C(LocalPlayer player, byte context){
        if (context == 1) {
            ((StandUser) player).roundabout$setGasolineTime(context);
        } else if (context == PacketDataIndex.S2C_SIMPLE_FREEZE_STAND) {
            if (((StandUser)player).roundabout$getStandPowers().hasCooldowns() ||
                    ((StandUser)player).roundabout$isSealed()) {
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
        } else if (context == PacketDataIndex.S2C_SIMPLE_CLOSE_THE_RELLA) {
            if (Minecraft.getInstance().screen instanceof VisageStoreScreen vs){
                setScreenNull = true;
            }
        }
    } public static void handleSimpleBytePacketS2C(byte context){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            handleSimpleBytePacketS2C(player,context);
        }
    }

    public static boolean setScreenNull = false;

    /** From the center of the camera, raycast out at given angle and return the BlockPos at that end position.
     * If nothing is found within the limit, it will return null. */
    public static @Nullable BlockPos raycastForBlockGivenAngle(double pitchOffsetDeg, double yawOffsetDeg, int limit)
    {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return null;

        float baseYaw = player.getYRot();
        float basePitch = player.getXRot();

        float finalYaw = baseYaw + (float) yawOffsetDeg;
        float finalPitch = basePitch + (float) pitchOffsetDeg;

        double yawRad = Math.toRadians(finalYaw);
        double pitchRad = Math.toRadians(finalPitch);

        double xz = Math.cos(pitchRad);
        double x = -Math.sin(yawRad) * xz;
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * xz;

        Vec3 start = player.getEyePosition(0);
        Vec3 direction = new Vec3(x, y, z);
        Vec3 end = start.add(direction.scale(limit));

        BlockHitResult hitResult = player.level().clip(new ClipContext(
                start, end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return hitResult.getBlockPos();
        }

        return null;
    }

    @Unique
    public static void roundabout$renderBound(LivingEntity victim, float delta, PoseStack poseStack, MultiBufferSource mb, Entity binder, float focus) {
        poseStack.pushPose();
        Vec3 vec3 = binder.getRopeHoldPosition(delta);
        if (binder instanceof LivingEntity lv){
            StandUser su = ((StandUser)lv);
            StandEntity stand = su.roundabout$getStand();
            if (stand != null){
                vec3 = stand.getRopeHoldPosition(delta);
            }
        }
        double d0 = (double)(Mth.lerp(delta, victim.yBodyRotO, victim.yBodyRot) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = victim.getLeashOffset(delta);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp((double)delta, victim.xo, victim.getX()) + d1;
        double d4 = Mth.lerp((double)delta, victim.yo, victim.getY()) + vec31.y; //+3
        double d5 = Mth.lerp((double)delta, victim.zo, victim.getZ()) + d2;
        poseStack.translate(d1, vec31.y, d2);
        float f = (float)(vec3.x - d3);
        float f1 = (float)(vec3.y - d4);
        float f2 = (float)(vec3.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = mb.getBuffer(RenderType.leash());
        Matrix4f matrix4f = poseStack.last().pose();
        float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        Vec3 eyeVectorV = victim.getEyePosition(delta);
        Vec3 eyeVectorB = binder.getEyePosition(delta);
        if (victim instanceof FallenMob fm && !fm.getActivated()){
            eyeVectorV = victim.getPosition(delta);
        } else if (binder instanceof FallenMob fm && !fm.getActivated()){
            eyeVectorB = victim.getPosition(delta);
        }
        BlockPos blockpos = BlockPos.containing(eyeVectorV);
        BlockPos blockpos1 = BlockPos.containing(eyeVectorB);
        int i = roundabout$getBlockLightLevel(victim, blockpos);
        int j = roundabout$getBlockLightLevel(binder, blockpos1);
        int k = victim.level().getBrightness(LightLayer.SKY, blockpos);
        int l = victim.level().getBrightness(LightLayer.SKY, blockpos1);

        for(int i1 = 0; i1 <= 24; ++i1) {
            roundabout$addVertexPair(binder, vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false,focus);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            roundabout$addVertexPair(binder, vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true,focus);
        }

        poseStack.popPose();
    }
    public static int roundabout$getBlockLightLevel(Entity $$0, BlockPos $$1) {
        return $$0.isOnFire() ? 15 : $$0.level().getBrightness(LightLayer.BLOCK, $$1);
    }

    public static void roundabout$addVertexPair(Entity binder, VertexConsumer p_174308_, Matrix4f p_254405_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_, float focus) {
        float f = (float)p_174321_ / 24.0F;
        int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.pack(i, j);
        int tc = binder.tickCount % 9;
        float f1 = 0.7F + (float)(Math.random()*0.3);
        if (tc > 5) {
            f1*=0.92F;
        }
        if (tc > 2) {
            f1*=0.84F;
        }
        Vec3 color = roundabout$getBindColor(binder);
        float f2 = (float) (color.x() * f1);
        float f3 = (float) (color.y() * f1);
        float f4 = (float) (color.z() * f1);
        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        float width = 0.05F;
        p_174308_.vertex(p_254405_, f5 - p_174319_ - width, f6 + p_174318_ + width - focus, f7 + p_174320_ + width).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        p_174308_.vertex(p_254405_, f5 + p_174319_ + width, f6 + p_174317_ - p_174318_ - width - focus, f7 - p_174320_ - width).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    public static Vec3 roundabout$getBindColor(Entity binder) {
        if (binder instanceof LivingEntity LE && ((StandUser)binder).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                return new Vec3(0,0.8F,0.949F);
            } else if (sft == StandFireType.PURPLE.id){
                return new Vec3(0.839F,0.122F,0.949F);
            } else if (sft == StandFireType.GREEN.id){
                return new Vec3(0.345F,1F,0.2F);
            } else if (sft == StandFireType.DREAD.id){
                return new Vec3(0.788F,0,0);
            } else if (sft == StandFireType.CREAM.id){
                return new Vec3(0.949F,0.945F,0.718F);
            }
        }
        return new Vec3(0.969F,0.569F,0.102F);
    }




    public static void sendPositionalDataToServer(Minecraft client) {

        ModMessageEvents.sendToServer(
                DynamicWorld.DynamicWorldNetMessages.MESSAGES.ADD_WORLD.value
        );
    }
}
