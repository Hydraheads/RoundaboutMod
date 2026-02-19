package net.hydra.jojomod.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.gui.*;
import net.hydra.jojomod.client.models.layers.anubis.AnubisLayer;
import net.hydra.jojomod.client.models.visages.parts.FirstPersonArmsModel;
import net.hydra.jojomod.client.models.visages.parts.FirstPersonArmsSlimModel;
import net.hydra.jojomod.entity.TickableSoundInstances.RoadRollerAmbientSound;
import net.hydra.jojomod.entity.TickableSoundInstances.RoadRollerExplosionSound;
import net.hydra.jojomod.entity.TickableSoundInstances.RoadRollerMixingSound;
import net.hydra.jojomod.entity.projectile.CinderellaVisageDisplayEntity;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.VampireData;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.entity.TickableSoundInstances.BowlerHatFlyingSound;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.powers.power_types.PunchingGeneralPowers;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.stand.powers.*;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.TooltipFlag;
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
import net.zetalasis.networking.message.api.ModMessageEvents;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
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
import net.zetalasis.networking.packet.api.IClientNetworking;
import net.zetalasis.world.DynamicWorld;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;


public class ClientUtil {


    public static Matrix4f savedPose;
    public static int checkthis = 0;
    public static int checkthisdat = 0;
    public static int renderBloodTicks = 0;
    public static boolean skipInterpolation = false;

    /**Fallback in case the client exits the range and can't be fed the packet anymore.
     * Not a perfect solution but it should help.*/
    public static int skipInterpolationFixAccidentTicks = -1;


    public static void animateZombieArmsNoBob(ModelPart $$0, ModelPart $$1, boolean $$2, float $$3, float $$4) {
        float $$5 = Mth.sin($$3 * (float) Math.PI);
        float $$6 = Mth.sin((1.0F - (1.0F - $$3) * (1.0F - $$3)) * (float) Math.PI);
        $$1.zRot = 0.0F;
        $$0.zRot = 0.0F;
        $$1.yRot = -(0.1F - $$5 * 0.6F);
        $$0.yRot = 0.1F - $$5 * 0.6F;
        float $$7 = (float) -Math.PI / ($$2 ? 1.5F : 2.25F);
        $$1.xRot = $$7;
        $$0.xRot = $$7;
        $$1.xRot += $$5 * 1.2F - $$6 * 0.4F;
        $$0.xRot += $$5 * 1.2F - $$6 * 0.4F;
    }

    public static void setCheck(){
        //Right-clicking a visage opens the power inventory (see inputevents mixin for another use case)
        KeyInputs.menuKey(Minecraft.getInstance().player, Minecraft.getInstance());
    }
    public static boolean isPlayerOrCamera(Entity ent){
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.getCameraEntity() != null && ent.is(mc.getCameraEntity())) &&
                !(mc.player !=null && ent.is(mc.player))) {
            return true;
        }
        return false;
    }


    public static double getCameradDistance(Entity ent){
        return Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().distanceTo(ent.position());
    }

    public static boolean rendersRipperEyes(Entity ent){
        if (ent instanceof Player pl && ((IPlayerEntity)pl).roundabout$GetPos2() == PlayerPosIndex.RIPPER_EYES_ACTIVE){
            return true;
        }
        return false;
    }
    public static boolean disableBobbing(Entity ent){
        return rendersRipperEyes(ent);
    }

    public static boolean hasChangedArms(Entity ent){
        if (HeatUtil.isArmsFrozen(ent)){
            return true;
        }
        return false;
    }
    public static ResourceLocation getChangedArmTexture(Entity ent){
        if (HeatUtil.isArmsFrozen(ent)){
            return StandIcons.ICICLE_LAYER;
        }
        return StandIcons.ICICLE_LAYER;
    }

    public static boolean hasChangedLegs(Entity ent){
        if (HeatUtil.isLegsFrozen(ent)){
            return true;
        }
        return false;
    }
    public static ResourceLocation getChangedLegTexture(Entity ent){
        if (HeatUtil.isArmsFrozen(ent)){
            return StandIcons.ICICLE_LAYER;
        }
        return StandIcons.ICICLE_LAYER;
    }
    public static boolean hasChangedHead(Entity ent){
        if (HeatUtil.isBodyFrozen(ent)){
            return true;
        }
        return false;
    }
    public static ResourceLocation getChangedHeadTexture(Entity ent){
        if (HeatUtil.isArmsFrozen(ent)){
            return StandIcons.ICICLE_LAYER;
        }
        return StandIcons.ICICLE_LAYER;
    }
    public static boolean hasChangedBody(Entity ent){
        if (HeatUtil.isBodyFrozen(ent)){
            return true;
        }
        return false;
    }
    public static ResourceLocation getChangedBodyTexture(Entity ent){
        if (HeatUtil.isArmsFrozen(ent)){
            return StandIcons.ICICLE_LAYER;
        }
        return StandIcons.ICICLE_LAYER;
    }
    public static ResourceLocation getChangedBodyBreastTexture(Entity ent){
        if (HeatUtil.isArmsFrozen(ent)){
            return StandIcons.ICE_CHEST_LAYER;
        }
        return StandIcons.ICE_CHEST_LAYER;
    }
    public static boolean hideCapeAndEars(Entity ent){
        if (HeatUtil.isBodyFrozen(ent)){
            return true;
        }
        return false;
    }
    public static boolean hideArmor(Entity ent){
        if (HeatUtil.isBodyFrozen(ent) &&
                isHiddenIceEntity(ent)){
            return true;
        }
        return false;
    }
    public static boolean isHiddenIceEntity(Entity ent){
        return (ent != null && (ent instanceof Player) || (ent instanceof Mob mb
        && !mb.isBaby() && (ent.getType()==EntityType.ZOMBIE
                ||ent.getType()==EntityType.HUSK
                ||ent.getType()==EntityType.CREEPER
                ||ent.getType()==EntityType.DROWNED
                ||ent.getType()==EntityType.SKELETON)));
    }
    public static boolean hideLegs(Entity ent){
        if (HeatUtil.isLegsFrozen(ent) &&
                isHiddenIceEntity(ent)){
            return true;
        }
        return false;
    }
    public static int getFrozenLevel(){
        return frozenLevel;
    }
    public static int clientTicker;
    public static int getClientTicker(){
        return clientTicker;
    }
    public static void tickClientUtilStuff(){
        clientTicker++;

        if (renderBloodTicks > 0){
            renderBloodTicks--;
        }
        /**
        Minecraft mc = Minecraft.getInstance();
        if (mc!= null && mc.player != null) {
            markBlockAsInvisible(mc.player.getOnPos());
            markBlockAsInvisible(mc.player.getOnPos().below());
        }
         **/
        if (ClientUtil.popSounds != null){
            ClientUtil.popSounds.popSounds();
            ClientUtil.popSounds = null;
        }

        if (roadRollerPickingRRE != null) {
            if (!roadRollerPickingRRE.isAlive() && roadRollerPickingRRE.isRemoved()) {
                roadRollerPickingRRE = null;
            }
        }

        if (ClientUtil.isInCinderellaMobUI > -1){
            if (!ClientUtil.hasCinderellaShopUI()){
                C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_RELLA_CANCEL,ClientUtil.isInCinderellaMobUI);
                ClientUtil.isInCinderellaMobUI = -1;
            }
        } if (ClientUtil.setScreenNull){
            ClientUtil.setScreenNull = false;
            Minecraft.getInstance().setScreen(null);
        }
        if (skipInterpolationFixAccidentTicks > -1){
            skipInterpolationFixAccidentTicks--;
        } if (skipInterpolation){
            if (skipInterpolationFixAccidentTicks <= -1){
                skipInterpolation = false;
            }
        }
    }
    public static void preRenderLifeTracker(LifeTrackerEntity ent, double $$1, double $$2, double $$3, float $$4, PoseStack pose, MultiBufferSource $$6) {
        ent.travelAheadRender($$4);
    }
    public static void preRenderCrossfire(CrossfireHurricaneEntity ent, double $$1, double $$2, double $$3, float $$4, PoseStack pose, MultiBufferSource $$6){
            if (((TimeStop)ent.level()).inTimeStopRange(ent)){
                $$4 = 0;
            }
            LivingEntity user = ent.getStandUser();
            if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                if (ent.getCrossNumber() > 0) {
                    if (ent.getCrossNumber() < 5) {
                        if (PMR.hurricaneSpecial == null) {
                            PMR.hurricaneSpecial = new ArrayList<>();
                        }
                        List<CrossfireHurricaneEntity> hurricaneSpecial2 = new ArrayList<>(PMR.hurricaneSpecial) {
                        };
                        if (!hurricaneSpecial2.isEmpty()) {
                            PMR.spinint = PMR.lastSpinInt + ($$4 * PMR.maxSpinint);
                            int totalnumber = hurricaneSpecial2.size();
                            double lerpX = (user.getX() * $$4) + (user.xOld * (1.0f - $$4));
                            double lerpY = (user.getY() * $$4) + (user.yOld * (1.0f - $$4));
                            double lerpZ = (user.getZ() * $$4) + (user.zOld * (1.0f - $$4));
                            PMR.transformHurricane(ent, totalnumber, lerpX,
                                    lerpY, lerpZ, ent.getRenderSize());
                        }
                    } else if (ent.getCrossNumber() == 5){

                        double lerpX = (user.getX() * $$4) + (user.xOld * (1.0f - $$4));
                        double lerpY = (user.getY() * $$4) + (user.yOld * (1.0f - $$4));
                        double lerpZ = (user.getZ() * $$4) + (user.zOld * (1.0f - $$4));
                        PMR.transformHurricane(ent, 1, lerpX,
                                lerpY, lerpZ, ent.getRenderSize());
                    } else if (ent.getCrossNumber() == 6){
                        PMR.transformGiantHurricane(ent);
                    }
                }
            }
    }

    public static void preRenderSurvivor(Entity ent, double $$1, double $$2, double $$3, float $$4, PoseStack pose, MultiBufferSource $$6) {
        float lerpYRot = (float) ((ILivingEntityAccess)ent).roundabout$getLerpYRot();
        ent.yRotO = lerpYRot;
        ent.setYRot(lerpYRot);
        ent.setYBodyRot(lerpYRot);
        ent.setYHeadRot(lerpYRot);
    }

    private static RoadRollerMixingSound roadRollerMixingSound;
    private static RoadRollerEntity roadRollerPickingRRE;

    public static void setRoadRollerPickingEntity(RoadRollerEntity RRE) {
        roadRollerPickingRRE = RRE;
    }

    public static RoadRollerEntity getRoadRollerPickingRRE() {
        return roadRollerPickingRRE;
    }

    public static void handleRoadRollerAmbientSound(Entity entity) {
        Minecraft.getInstance().getSoundManager().play(new RoadRollerAmbientSound(ModSounds.ROAD_ROLLER_AMBIENT_EVENT, SoundSource.PLAYERS, 1, 0, entity));
    }

    public static void handleRoadRollerExplosionSound(Entity entity) {
        Minecraft.getInstance().getSoundManager().play(new RoadRollerExplosionSound(ModSounds.ROAD_ROLLER_EXPLOSION_EVENT, SoundSource.PLAYERS, 1, 0, entity));
    }

    public static void handleRoadRollerMixingSound(Entity entity) {
        roadRollerMixingSound = new RoadRollerMixingSound(ModSounds.ROAD_ROLLER_MIXING_EVENT, SoundSource.PLAYERS, 1.0F, 0.0F, entity);
        Minecraft.getInstance().getSoundManager().play(roadRollerMixingSound);
    }
    public static void stopRoadRollerMixingSound(Entity entity) {
        if (roadRollerMixingSound != null) {
            Minecraft.getInstance().getSoundManager().stop(roadRollerMixingSound);
            roadRollerMixingSound = null;
        }
    }


    public static void preRenderCinderellaMask(CinderellaVisageDisplayEntity ent, double $$1, double $$2, double $$3, float $$4, PoseStack pose, MultiBufferSource $$6) {


        if (((TimeStop) ent.level()).inTimeStopRange(ent)) {
            $$4 = 0;
        }
        LivingEntity user = ent.getStandUser();
        if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersCinderella PCR) {
            if (PCR.floatingVisages == null) {
                PCR.floatingVisages = new ArrayList<>();
            }
            List<CinderellaVisageDisplayEntity> hurricaneSpecial2 = new ArrayList<>(PCR.floatingVisages) {
            };
            if (!hurricaneSpecial2.isEmpty()) {
                PCR.spinint = PCR.lastSpinInt + ($$4 * PCR.maxSpinint);
                int totalnumber = hurricaneSpecial2.size();
                double lerpX = (user.getX() * $$4) + (user.xOld * (1.0f - $$4));
                double lerpY = (user.getY() * $$4) + (user.yOld * (1.0f - $$4));
                double lerpZ = (user.getZ() * $$4) + (user.zOld * (1.0f - $$4));
                PCR.transformFloatingVisages(ent, totalnumber, lerpX,
                        lerpY, lerpZ, ent.getRenderSize());
            }
        }
    }

    public static void clickVampireSlot(int slot){
        C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_VAMPIRE_SKILL_BUY,slot);
        SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
        soundmanager.play(SimpleSoundInstance.forUI(ModSounds.VAMPIRE_DRAIN_EVENT, 1.0F));
    }

    public static @Nullable Connection getC2SConnection()
    {
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.player == null)
            return null;

        if (((IClientNetworking)client) == null){
            return null;
        }
        Connection integratedServerCon = ((IClientNetworking)client).roundabout$getServer();

        return (integratedServerCon != null ? integratedServerCon : client.player.connection.getConnection());
    }
    public static boolean renderBloodMeter(){
        return renderBloodTicks > 0;
    }

    public static void handleGeneralPackets(String message, Object... vargs) {
        Minecraft instance = Minecraft.getInstance();
        Player player = instance.player;

        instance.execute(() -> {
            if (player != null) {
                /**Mandom's time rewind flashes on people and makes their screen interpolate*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.Rewind.value)) {
                    StandUser user = ((StandUser)player);
                    StandPowers powers = user.roundabout$getStandPowers();
                    if (ConfigManager.getClientConfig().mandomRewindShowsVisualEffectsToNonMandomUsers ||
                            powers instanceof PowersMandom){
                        int ticks = powers.timeRewindOverlayTicks;
                        if (ticks <= -1){
                            powers.timeRewindOverlayTicks = 0;
                        }
                    }
                    if (ConfigManager.getClientConfig().mandomRewindAttemptsToSkipInterpolation) {
                        skipInterpolation = true;
                        skipInterpolationFixAccidentTicks = 14;
                    }
                }
                /**Generalized packet for resuming interpolation on all mobs*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.Interpolate.value)) {
                    skipInterpolation = false;
                }
                /**Mandom Clock Particle*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.Chrono.value)) {
                    int id = (int)vargs[0];
                    double x = (double)vargs[1];
                    double y = (double)vargs[2];
                    double z = (double)vargs[3];
                    SimpleParticleType clocktype = ModParticles.CLOCK;
                    if (player.getId() != id)
                        clocktype = ModParticles.BLUE_CLOCK;
                    Entity ent = player.level().getEntity(id);
                    if (ent != null) {

                        player.level()
                                .addParticle(
                                        clocktype,
                                        x,
                                        y+ent.getEyeHeight()*0.8,
                                        z,
                                        0,
                                        0,
                                        0
                                );
                    }
                }
                /**The penalty for killing or placing blocks to distort time and raise cooldown*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.MANDOM_PENALTY.value)) {
                    int altared = (int)vargs[0];
                    StandUser user = ((StandUser)player);
                    StandPowers powers = user.roundabout$getStandPowers();
                    if (powers instanceof PowersMandom PM){
                        PM.setTimeHasBeenAltered(altared);
                    }
                }
                /**Invis Psuedo Tracked Data*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.TRUE_INVISIBILITY.value)) {
                    int entityID = (int)vargs[0];
                    int altered = (int)vargs[1];
                    Entity ent = player.level().getEntity(entityID);
                    if (ent != null){
                        ((IEntityAndData)ent).roundabout$setTrueInvisibility(altered);

                    }
                }
                /**Daze Packet*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncDaze.value)) {
                    byte dazeTime = (byte)vargs[0];
                    ClientUtil.updateDazePacket(dazeTime);
                }

                /**Guard Sync Packet*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncGuard.value)) {
                    float guardPoints = (float)vargs[0];
                    boolean guardBroken = (boolean)vargs[1];
                    ((StandUser) player).roundabout$setGuardPoints(guardPoints);
                    ((StandUser)player).roundabout$setGuardBroken(guardBroken);
                }

                /**Barrage Clash S2C Packet*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.UpdateBarrageClash.value)) {
                    int clashOpID = (int)vargs[0];
                    float progress = (float)vargs[1];
                    ClientUtil.clashUpdatePacket(clashOpID, progress);
                }

                /**Read in Sent config*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SendConfig.value)) {
                    String config = (String)vargs[0];
                    ClientNetworking.initialize(config);
                }

                /**Read in Sent config*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.PlaySound.value)) {
                    int entId = (int) vargs[0];
                    byte soundID = (byte) vargs[1];
                    Entity User = player.level().getEntity(entId);
                    if (User instanceof LivingEntity){
                        ((StandUserClient)User).roundabout$clientQueSound(soundID);
                    }
                }
                /**Read in Sent config*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.StopSound.value)) {
                    int entId = (int) vargs[0];
                    byte soundID = (byte) vargs[1];
                    Entity User = player.level().getEntity(entId);
                    if (User instanceof LivingEntity){
                        ((StandUserClient)User).roundabout$clientQueSoundCanceling(soundID);
                    }
                }

                /**TS Teleport blip*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.Blip.value)) {
                    int data = (int) vargs[0];
                    byte activePower = (byte) vargs[1];
                    Vector3f vec = (Vector3f) vargs[2];
                    ClientUtil.handleBlipPacketS2C(data,activePower,vec);
                }

                /**Syncs cooldowns for skills*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncCooldown.value)) {
                    byte power = (byte) vargs[0];
                    int cooldown = (int) vargs[1];
                    ClientUtil.skillCDSyncPacket(power, cooldown);
                }
                /**Syncs cooldowns for skills, includes a maximum to update with*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncCooldownMax.value)) {
                    byte power = (byte) vargs[0];
                    int cooldown = (int) vargs[1];
                    int maxCooldown = (int) vargs[2];
                    ClientUtil.skillMaxCDSyncPacket(power, cooldown, maxCooldown);
                }
                /**Syncs the active power the stand is using*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncActivePower.value)) {
                    byte power = (byte) vargs[0];
                    MainUtil.syncActivePower(player,power);
                }
                /**Syncs the active power the fate is using*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncActivePowerFate.value)) {
                    byte power = (byte) vargs[0];
                    MainUtil.syncActivePowerFate(player,power);
                }
                /**Syncs the active power the powers is using*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncActivePowerPowers.value)) {
                    byte power = (byte) vargs[0];
                    MainUtil.syncActivePowerPowers(player,power);
                }

                /**Syncs the power inventory settings*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncPowerInventory.value)) {
                    int anchorPlace = (int) vargs[0];
                    float distanceOut = (float) vargs[1];
                    float idleOpacity = (float) vargs[2];
                    float combatOpacity = (float) vargs[3];
                    float enemyOpacity = (float) vargs[4];
                    int anchorPlaceAttack = (int) vargs[5];
                    ClientUtil.handlePowerInventoryOptionsPacketS2C(player,anchorPlace,distanceOut,idleOpacity,combatOpacity,
                            enemyOpacity,anchorPlaceAttack);
                }

                /**Syncs the active power the stand is using*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.IntPowerData.value)) {
                    byte activePower = (byte) vargs[0];
                    int data = (int) vargs[1];
                    ((StandUser) player).roundabout$getStandPowers().updatePowerInt(activePower,data);
                }

                /**Generic int that is sent to the client*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.IntToClient.value)) {
                    byte context = (byte) vargs[0];
                    int data = (int) vargs[1];
                    ClientUtil.handleIntPacketS2C(player,data,context);
                }
                /**Generic double int that is sent to the client*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.DoubleIntToClient.value)) {
                    byte context = (byte) vargs[0];
                    int data = (int) vargs[1];
                    int data2 = (int) vargs[2];
                    ClientUtil.handleDoubleIntPacketS2C(player,data,data2,context);
                }
                /**Generic byte that is sent to the client*/
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SimpleByteToClient.value)) {
                    byte context = (byte) vargs[0];
                    ClientUtil.handleSimpleBytePacketS2C(context);
                }


                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.ByteBundleToClient.value)) {
                    byte context = (byte) vargs[0];
                    byte firstByte = (byte) vargs[1];
                    byte secondByte = (byte) vargs[2];
                    ClientUtil.handleBundlePacketS2C(context,firstByte,secondByte);
                }

                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.AddTSEntity.value)) {
                    int entityID = (int) vargs[0];
                    double x = (double) vargs[1];
                    double y = (double) vargs[2];
                    double z = (double) vargs[3];
                    double range = (double) vargs[4];
                    int duration = (int) vargs[5];
                    int maxDuration = (int) vargs[6];
                    ClientUtil.handleTimeStoppingEntityPacket(entityID,x,y,z,range,duration,maxDuration);
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.RemoveTSEntity.value)) {
                    int entityID = (int) vargs[0];
                    ClientUtil.processTSRemovePacket(entityID);
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.AddPCEntity.value)) {
                    int entityID = (int) vargs[0];
                    double x = (double) vargs[1];
                    double y = (double) vargs[2];
                    double z = (double) vargs[3];
                    double range = (double) vargs[4];
                    byte ctext = (byte) vargs[5];
                    ClientUtil.handlePermaCastingEntityPacket(entityID,x,y,z,range,ctext);
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.RemovePCEntity.value)) {
                    int entityID = (int) vargs[0];
                    ClientUtil.handlePermaCastingRemovePacket(entityID);
                }


                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.ResumeTileEntityTS.value)) {
                    int x = (int) vargs[0];
                    int y = (int) vargs[1];
                    int z = (int) vargs[2];
                    ClientUtil.handleEntityResumeTsPacket(new Vec3i(x,y,z));
                }

                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SendNewDynamicWorld.value)) {
                    String name = (String) vargs[0];
                    ResourceKey<Level> LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, Roundabout.location(name));
                    dimensionSynch(LEVEL_KEY);
                }

                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.EjectPRunning.value)) {
                    if (((StandUser)player).roundabout$getStandPowers() instanceof PowersD4C d4c)
                    {
                        d4c.ejectParallelRunning();
                    }
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.HeelExtend.value)) {
                    int entityID = (int) vargs[0];
                    Entity ent = player.level().getEntity(entityID);
                    if (ent instanceof LivingEntity LE &&
                            ((StandUser)LE).roundabout$getStandPowers() instanceof PowersWalkingHeart PW){
                        PW.setHeelExtension(3);
                    }
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.RefreshAllCooldowns.value)) {
                    MainUtil.clearCooldowns(player);
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.AffirmAllCooldowns.value)) {
                    ((IPlayerEntity)player).rdbt$setCooldownQuery(true);
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.CreamUpdateTimer.value)) {
                    int sigmaTime = (int) vargs[0];
                    if (((StandUser)player).roundabout$getStandPowers() instanceof PowersCream PC) {
                        PC.setVoidTime(sigmaTime);
                    }
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.CreamUpdateTransformTimer.value)) {
                    int sigmaTime = (int) vargs[0];
                    if (((StandUser)player).roundabout$getStandPowers() instanceof PowersCream PC) {
                        PC.setTransformTimer(sigmaTime);
                    }
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.CreamUpdateTransformDirection.value)) {
                    int sigmaDirection = (int) vargs[0];
                    if (((StandUser)player).roundabout$getStandPowers() instanceof PowersCream PC) {
                        PC.setTransformDirection(sigmaDirection);
                    }
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.VampireMessage.value)) {
                    playSound(ModSounds.VAMPIRE_MESSAGE_EVENT,player,2,1);
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.UpdateVampireData.value)) {
                    VampireData vdata = ((IPlayerEntity)player).rdbt$getVampireData();
                    vdata.vampireLevel = (int) vargs[0];
                    vdata.bloodExp = (int) vargs[1];
                    vdata.animalExp = (int) vargs[2];
                    vdata.monsterEXP = (int) vargs[3];
                    vdata.npcExp = (int) vargs[4];
                    vdata.timeSinceAnimal = (int) vargs[5];
                    vdata.timeSinceMonster = (int) vargs[6];
                    vdata.timeSinceNpc = (int) vargs[7];

                    vdata.strengthLevel = (byte) vargs[8];
                    vdata.dexterityLevel = (byte) vargs[9];
                    vdata.resilienceLevel = (byte) vargs[10];

                    vdata.hypnotismLevel = (byte) vargs[11];
                    vdata.superHearingLevel = (byte) vargs[12];
                    vdata.bloodSpeedLevel = (byte) vargs[13];

                    vdata.graftingLevel = (byte) vargs[14];
                    vdata.fleshBudLevel = (byte) vargs[15];
                    vdata.daggerSplatterLevel = (byte) vargs[16];

                    vdata.jumpLevel = (byte) vargs[17];
                    vdata.ripperEyesLevel = (byte) vargs[18];
                    vdata.freezeLevel = (byte) vargs[19];
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.UpdateVampireData2.value)) {
                    VampireData vdata = ((IPlayerEntity)player).rdbt$getVampireData();
                    vdata.vampireLevel = (int) vargs[0];
                    vdata.bloodExp = (int) vargs[1];
                    vdata.animalExp = (int) vargs[2];
                    vdata.monsterEXP = (int) vargs[3];
                    vdata.npcExp = (int) vargs[4];
                    vdata.timeSinceAnimal = (int) vargs[5];
                    vdata.timeSinceMonster = (int) vargs[6];
                    vdata.timeSinceNpc = (int) vargs[7];
                    renderBloodTicks = 60;
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.UpdateVampireData3.value)) {
                    VampireData vdata = ((IPlayerEntity)player).rdbt$getVampireData();
                    vdata.timeSinceAnimal = (int) vargs[0];
                    vdata.timeSinceMonster = (int) vargs[1];
                    vdata.timeSinceNpc = (int) vargs[2];
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.GunRecoil.value)) {
                    String sigmaString = (String) vargs[0];
                    ClientUtil.applyClientRecoil(player, sigmaString);
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncPossessor.value)) {
                    int i = (int) vargs[0];
                    ((StandUser)player).roundabout$getPossessor().setTarget((LivingEntity) player.level().getEntity(i));
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.ShatterIce.value)) {
                    int i = (int) vargs[0];
                    Entity target = player.level().getEntity(i);
                    if (target instanceof LivingEntity LE) {
                        ((StandUser)LE).rdbt$setHideDeath(true);
                    }
                }
                if (message.equals(ServerToClientPackets.S2CPackets.MESSAGES.SyncAllies.value)) {;
                    String data = (String) vargs[0];
                    if(((StandUser) player).roundabout$getStandPowers() instanceof PowersGreenDay PGD){
                        PGD.allies = PGD.allyListParser(data);
                    }
                }
                // theoretical deregister dynamic worlds packet
                // String name = buf.readUtf();
                //        ResourceKey<Level> LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, Roundabout.location(name));
                //
                //        if (client.player != null) {
                //            client.player.connection.levels().remove(LEVEL_KEY);
                //        }
            }
        });
    }
    public static List<Component> getTooltipFromItem(Minecraft p_281881_, ItemStack p_282833_) {
        return p_282833_.getTooltipLines(p_281881_.player, p_281881_.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
    }
    public static boolean getDirectionRight(LivingEntity self){

        Direction rightAxis = Direction.DOWN;
        MobEffectInstance mi = Minecraft.getInstance().player.getEffect(ModEffects.GRAVITY_FLIP);
        if (mi != null) {
            if (mi.getAmplifier() == 0) {
                rightAxis = Direction.NORTH;
            }
            if (mi.getAmplifier() == 1) {
                rightAxis = Direction.SOUTH;
            }
            if (mi.getAmplifier() == 2) {
                rightAxis = Direction.EAST;
            }
            if (mi.getAmplifier() == 3) {
                rightAxis = Direction.WEST;
            }
            if (mi.getAmplifier() == 4) {
                rightAxis = Direction.UP;
            }
        }
        return ((IGravityEntity)self).roundabout$getGravityDirection() != rightAxis;
    }

    /**
     * A generalized packet for sending ints to the client. Context is what to do with the data int
     */
    public static void handleIntPacketS2C(Player player, int data, byte context) {
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
        } else if (context == PacketDataIndex.S2C_INT_VAMPIRE_SPEED){
            if (((IFatePlayer)player).rdbt$getFatePowers() instanceof VampiricFate VP){
                VP.setSpeedActivated(data);
            }
        } else if (context == PacketDataIndex.S2C_INT_STAND_MODE){
            ((StandUser) player).roundabout$getStandPowers().clientIntUpdated(data);
        } else if (context == PacketDataIndex.S2C_INT_FLESH_BUD){
            Entity target = player.level().getEntity(data);
            if (target != null && !target.isRemoved() && target.isAlive() && target.distanceTo(getPlayer()) < 30) {
                playSound(ModSounds.FLESH_BUD_EVENT,target,1,1);
            }
        } else if (context == PacketDataIndex.S2C_INT_COMBO_AMT){
            if (((IPowersPlayer) player).rdbt$getPowers() instanceof PunchingGeneralPowers pgp){
                pgp.setComboAmt(data);
            }
        } else if (context == PacketDataIndex.S2C_INT_COMBO_SEC_LEFT){
            if (((IPowersPlayer) player).rdbt$getPowers() instanceof PunchingGeneralPowers pgp){
                pgp.setComboExpireTicks(data);
            }
        } else if (context == PacketDataIndex.AESTHETICIAN_OPEN){
            setCinderellaUI2(true,data);
        } else if (context == PacketDataIndex.S2C_INT_RIPPER_EYES) {
            if (((IPowersPlayer)player).rdbt$getPowers() instanceof VampireGeneralPowers vgp){
                vgp.ripperEyesLeft = data;
            }
        }
    }
    public static void handleDoubleIntPacketS2C(Player player, int data, int data2, byte context) {
        if (context == PacketDataIndex.S2C_INT_FADE){
            Entity target = player.level().getEntity(data);
            if (target instanceof Player pl) {
                ((IPowersPlayer) pl).rdbt$getPowers().setFaded(data2);
            }
        } else if (context == PacketDataIndex.S2C_INT_FADE_UPDATE){
            Entity target = player.level().getEntity(data);
            if (target instanceof Player pl) {
                ((IPowersPlayer) pl).rdbt$getPowers().fadeOutInterpolation = 5;
                ((IPowersPlayer) pl).rdbt$getPowers().setFaded(data2);
            }
        } else if (context == PacketDataIndex.S2C_SNYC_ACTIVE_POWER) {
            Entity target = player.level().getEntity(data);
            if (target instanceof LivingEntity LE) {
                StandUser SU = (StandUser) LE;
                if (SU.roundabout$getStandPowers() != null) {
                    SU.roundabout$getStandPowers().setActivePower((byte) data2);
                    SU.roundabout$getStandPowers().setAttackTimeDuring(0);
                }
            }
        }
    }

    public static boolean canSeeStands(Player lp){
        return !(lp != null && (((StandUser)lp).roundabout$getStandDisc().isEmpty() &&
                !lp.isSpectator()) && ConfigManager.getClientConfig().generalSettings.onlyStandUsersCanSeeStands);
    }
    public static boolean checkIfClientCanSeePastLocations() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (((StandUser)player).roundabout$getStandPowers() instanceof PowersMandom PM && PM.activatedPastVision()){
                return true;
            }
        }
        return false;
    }
    public static boolean checkIfClientCanSeeInvisAchtung() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (((StandUser)player).roundabout$getStandPowers() instanceof PowersAchtungBaby PM && PM.invisibleVisionOn()){
                return true;
            }
        }
        return false;
    }
    public static boolean isFabulous(){

        OptionInstance<GraphicsStatus> $$2 = Minecraft.getInstance().options.graphicsMode();
        GraphicsStatus $$3 = (GraphicsStatus)$$2.get();
        return $$3.equals(GraphicsStatus.FABULOUS);
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

    public static boolean sneakToggleHeld = false;
    public static boolean isSneakToggleHeld(){
        return sneakToggleHeld;
    } public static void setSneakToggleHeld(boolean heldTime){
        sneakToggleHeld = heldTime;
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

    public static ResourceLocation saveBufferTexture = null;
    public static boolean hideInvis = false;
    public static boolean getHideInvis(){
        return hideInvis;
    }

    public static float getThrowFadePercent(Entity ent, float delta){
        float throwFade = 1f;
        delta = delta % 1;
        IEntityAndData entityAndData = ((IEntityAndData) ent);
        if (entityAndData.roundabout$getTrueInvisibility() > -1) {
            throwFade = throwFade * 0.4F;
        }
        if (ent instanceof Player pl){
            GeneralPowers gp = ((IPowersPlayer)pl).rdbt$getPowers();
            int interp = gp.fadeOutInterpolation;
            if (gp.isFaded()){
                throwFade = (float) (throwFade *
                        (1.0-
                                Math.min(((((float)interp)*0.2f) +(delta*(0.2f))),1)
                ));
            } else {
                if (interp > 0) {
                    throwFade = (float) (throwFade *
                            (0.0 +
                                    Math.max(((((float) interp) * 0.2f) + (delta*(0.2f))), 1)
                            ));
                }
            }

            byte posX = ((IPlayerEntity)pl).roundabout$GetPos2();
            if (posX == PlayerPosIndex.VANISH_PERSIST){
                throwFade = 0;
            } else if (posX == PlayerPosIndex.VANISH_START){
                if (!ConfigManager.getClientConfig().blinkWithCamo){
                    throwFade = 0;
                } else {
                    int tc = pl.tickCount % 8;
                    if (tc  == 0){
                        throwFade = 1 - (delta*0.5F);
                    }else if (tc  == 1){
                        throwFade = 0.5F - (delta*0.5F);
                    }else if (tc  == 2 || tc == 3){
                        throwFade = 0;
                    }else if (tc  == 4){
                        throwFade = 0 + (delta*0.5F);
                    }else if (tc  == 5){
                        throwFade = 0.5F +  (delta*0.5F);
                    }
                }
            }
        }

        return throwFade;
    }

    public static PlayerModel getPlayerModel(Entity entity){
        if (entity instanceof Player player){
            EntityRenderDispatcher dispatch = Minecraft.getInstance().getEntityRenderDispatcher();
            EntityRenderer<?> ER = dispatch.getRenderer(player);
            if (ER instanceof LivingEntityRenderer PR && PR.getModel() instanceof PlayerModel<?> pm) {
                return pm;
            }
        }
        return null;
    }

    public static void setThrowFadeToTheEther(float ether){
        throwFadeToTheEther = ether;
    }
    public static float getThrowFadeToTheEther(){
        return throwFadeToTheEther;
    }
    public static int getThrowFadeToTheEtherInt(){
        return Mth.floor(throwFadeToTheEther*255);
    }
    //How visible the next rendered model part will be
    public static float throwFadeToTheEther = 1f;

    //If this is the case, thin ice will be rendered over the next rendered model part
    public static int frozenLevel = 0;

    public static int getOutlineColor(Entity entity) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            FatePowers fatePowers = ((IFatePlayer)player).rdbt$getFatePowers();

            if (powers.getGoBeyondTarget() != null && powers.getGoBeyondTarget().is(entity)) {
                return 10978493;
            }




            if (powers.highlightsEntity(entity, player))
                return powers.highlightsEntityColor(entity,player);

            if (fatePowers.highlightsEntity(entity, player))
                return fatePowers.highlightsEntityColor(entity,player);

            if (MainUtil.isZapper(player,entity)){
                //15974080
                return 11559774;
            }

            if (standComp.roundabout$getStand() instanceof D4CEntity)
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

    public static boolean hasATimeStopSeeingStandAndCanBypass(){
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.timeStopSettings != null &&
                clientConfig.timeStopSettings.tsStandsSeeTSTeleportAndDontFreeze) {
            return hasATimeStopSeeingStand();
        }
        return false;
    }

    public static void playSound(SoundEvent event, Entity entity, float volume, float pitch){
        SoundInstance qSound = new EntityBoundSoundInstance(
                event,
                SoundSource.NEUTRAL,
                volume,
                pitch,
                entity,
                entity.level().random.nextLong()
        );
        Minecraft.getInstance().getSoundManager().play(qSound);
    }

    public static void tickHeartbeat(Entity entity){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (((IFatePlayer)player).rdbt$getFatePowers() instanceof VampiricFate vp){
                if (vp.isHearing()){
                    vp.tickHeartbeat(entity);
                }
            }
        }
    }

    public static boolean hasATimeStopSeeingStand(){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            StandUser user = ((StandUser) player);
            ItemStack stack = user.roundabout$getStandDisc();
            if (stack != null && !stack.isEmpty()){
                return (stack.is(ModItems.STAND_DISC_STAR_PLATINUM) || stack.is(ModItems.STAND_DISC_THE_WORLD)
                        || stack.is(ModItems.MAX_STAND_DISC_STAR_PLATINUM) || stack.is(ModItems.MAX_STAND_DISC_THE_WORLD));
            }
        }
        return false;
    }
    public static void synchToCamera(Entity ent){
        if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            ent.setYRot(camera.getYRot());
            ent.setXRot(camera.getXRot());
            ent.setYHeadRot(ent.getYRot());
        }
    }
    public static boolean getFirstPerson(){
        if (Minecraft.getInstance() != null && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            return true;
        }
        return false;
    }
    public static int wasFrozen = 0;
    public static SoftAndWetPlunderBubbleEntity popSounds = null;
    public static boolean getWasFrozen(){
        return wasFrozen != 0;
    }
    public static boolean getScreenFreeze(){
        if (hasATimeStopSeeingStandAndCanBypass())
            return false;
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
    public static void openModificationVisageUI(ItemStack visage, int slot){
        Minecraft.getInstance().setScreen(new ModificationVisageScreen(visage, slot));
    }
    public static void openHairspryUI(){
        Minecraft.getInstance().setScreen(new HairColorChangeScreen());
    }
    public static void openMemoryRecordScreen(boolean recording){
        Minecraft.getInstance().setScreen(new MemoryRecordScreen(recording));
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
    public static boolean heldDownHide = false;
    public static void hideIcons(Player player, Minecraft C, boolean keyIsDown, Options option) {
        if (ConfigManager.getClientConfig() == null || ConfigManager.getClientConfig().dynamicSettings == null)
            return;
        if (keyIsDown) {
            if (!heldDownHide) {
                ConfigManager.getClientConfig().dynamicSettings.hideGUI = !ConfigManager.getClientConfig().dynamicSettings.hideGUI;
                heldDownHide = true;
            }
        } else {
            heldDownHide = false;
        }
    }
    public static void openCorpseBag(ItemStack stack) {
        Minecraft client = Minecraft.getInstance();
        client.setScreen(new CorpseBagScreen(stack));
    }

    public static void handleIntPacketS2C(int data, byte context) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            handleIntPacketS2C(player, data, context);
        }
    }

    /**
     * A generalized packet for sending ints to the client. Context is what to do with the data int
     */
    public static void handleBlipPacketS2C(LocalPlayer player, int data, byte context, Vector3f vec) {
        if (hasATimeStopSeeingStandAndCanBypass())
            return;
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
        C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_RELLA_START,entid);
    }
    public static void setCinderellaUI2(boolean costs, int entid) {
        Minecraft mc = Minecraft.getInstance();
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
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(
                new FogInventoryScreen(
                        mc.player, mc.player.connection.enabledFeatures(), mc.options.operatorItemsTab().get()
                ));
    }



    public static void pushPoseAndCooperate(PoseStack stack, int caseNumber){
        //Roundabout.LOGGER.info("Case Push "+caseNumber);
        stack.pushPose();
    }
    public static void popPoseAndCooperate(PoseStack stack, int caseNumber){
        //Roundabout.LOGGER.info("Case Pop "+caseNumber);
        stack.popPose();
    }

    public static boolean roundabout$configButtonSelected = false;

    public static String[] splitIntoLine(String input, int maxCharInLine){

        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while(word.length() > maxCharInLine){
                output.append(word.substring(0, maxCharInLine-lineLen) + "\n");
                word = word.substring(maxCharInLine-lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");

            lineLen += word.length() + 1;
        }
        // output.split();
        // return output.toString();
        return output.toString().split("\n");
    }

    public static boolean getInvisibilityVision(){
        if (Minecraft.getInstance() != null) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null)
                return false;
            if (((StandUser) localPlayer).roundabout$getStandPowers() instanceof PowersAchtungBaby PA && PA.invisibleVisionOn()) {
                return true;
            }
        }
        return false;
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
    public static void dimensionSynch(ResourceKey<Level> LEVEL_KEY){
        Roundabout.LOGGER.info("Got packet for dimension {}", LEVEL_KEY.toString());
        if (Objects.equals(ModPacketHandler.PLATFORM_ACCESS.getPlatformName(), "Forge")) {
            if (ClientUtil.packetLocPlayCheck()) {
                ClientUtil.dimensionSynchForge(LEVEL_KEY);
            }
        } else {
            ClientUtil.dimensionSynchFabric(Minecraft.getInstance(),LEVEL_KEY);
        }
    }
    public static void dimensionSynchForge(ResourceKey<Level> LEVEL_KEY){
        if (ClientUtil.packetLocPlayCheck()) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer != null) {
                localPlayer.connection.levels().add(LEVEL_KEY);
                C2SPacketUtil.d4cDimensionHopRegistryPacket();
            } else {
                packetLocPlayCheck();
            }
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
    public static void skillMaxCDSyncPacket(byte power, int cooldown, int maxCooldown){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            StandPowers powers = ((StandUser) player).roundabout$getStandPowers();
            powers.setCooldownMax(power,cooldown,maxCooldown);
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
    public static void handleBundlePacketS2C(byte context, byte one, byte two){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            handleBundlePacketS2C(player,context,one,two);
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
        if (ClientUtil.getScreenFreeze()){
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
    public static void handleBundlePacketS2C(LocalPlayer player, byte context, byte byte1, byte byte2){
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

    public static void handlePowerInventoryOptionsPacketS2C(Player player, int anchorPlace, float distanceOut, float idleOpacity,
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
                int switchTicks = ClientNetworking.getAppropriateConfig().itemSettings.switchStandDiscLength;
                if (switchTicks > 0){
                    if (((StandUser) player).roundabout$getSealedTicks() < switchTicks) {
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
        } else if (context == PacketDataIndex.S2C_RESPAWN){
            Minecraft.getInstance().player.respawn();
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

    public static void applyClientRecoil(Player player, String string) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer localPlayer = mc.player;

        if (player == null) return;

        float recoilPitch = -10F;
        if (Objects.equals(string, "sniper")) {
            recoilPitch = -13F;
        }
        localPlayer.turn(0.0F, recoilPitch);
    }

    public static<T extends LivingEntity, M extends EntityModel<T>> void renderFirstPersonModelParts(Entity cameraEnt, float $$4, PoseStack stack, MultiBufferSource source, int light){

        if (cameraEnt instanceof Player play) {

            // vampire
            if (((IFatePlayer)cameraEnt).rdbt$getFatePowers() instanceof VampireFate vf){
                int poggers = vf.getProgressIntoAnimation();
                if (poggers >= 16 && poggers <= 22) {
                    stack.pushPose();
                    poggers -= 16;
                    Vec3 vec = cameraEnt.getEyePosition();

                    IPlayerEntity pl = ((IPlayerEntity) cameraEnt);
                    float r = pl.rdbt$getHairColorX();
                    float g = pl.rdbt$getHairColorY();
                    float b = pl.rdbt$getHairColorZ();


                    ItemStack visage = pl.roundabout$getMaskSlot();
                    if (visage != null && !visage.isEmpty() && visage.getItem() instanceof MaskItem ME) {
                        VisageData vd = ME.visageData;
                        if (vd != null && vd.isCharacterVisage()) {
                            r = ((float) vd.getHairColor().getX()) / 255;
                            g = ((float) vd.getHairColor().getY()) / 255;
                            b = ((float) vd.getHairColor().getZ()) / 255;
                        }
                    }


                    Vec3 gtranslation = new Vec3(0, -0.5, 0);
                    stack.translate(gtranslation.x, gtranslation.y, gtranslation.z);
                    stack.mulPose(Axis.ZP.rotationDegrees(180f));
                    stack.mulPose(Axis.XP.rotationDegrees(1));

                    ModStrayModels.VampireHairFlesh.render(cameraEnt, $$4, stack, source, light, r, g, b, 1);

                    stack.popPose();
                }
            }
            byte bt = ((IPlayerEntity) play).roundabout$GetPos2();
            // vampire again
            if (ClientUtil.rendersRipperEyes(play)) {
                stack.pushPose();
                Vec3 gtranslation = new Vec3(0, -0.1, 0);
                stack.translate(gtranslation.x, gtranslation.y, gtranslation.z);
                float opacity = 0.5F;
                stack.mulPose(Axis.ZP.rotationDegrees(180f));
                boolean yes = false;
                for (var i = 0; i< 20; i++) {
                    stack.pushPose();
                    if (yes) {
                        stack.scale(1.01F, 1.01F, 1.01F);
                    }
                    //gtranslation = RotationUtil.vecPlayerToWorld(gtranslation,gravityDirection);

                    ModStrayModels.ripperEyesPart.render(cameraEnt, cameraEnt.tickCount + $$4, stack, source,
                            LightTexture.FULL_BRIGHT,
                            1, 1, 1, opacity);
                    yes = !yes;
                    stack.popPose();
                    stack.translate(0,0,-0.5);
                }
                stack.popPose();
            } else if (bt == PlayerPosIndex.BARRAGE) {
                stack.pushPose();

                boolean isHurt = play.hurtTime > 0;
                float r = isHurt ? 1.0F : 1.0F;
                float g = isHurt ? 0.6F : 1.0F;
                float b = isHurt ? 0.6F : 1.0F;
                Direction gravityDirection = GravityAPI.getGravityDirection(cameraEnt);
                Vec3 gtranslation = new Vec3(0, -0.4, 0);

                //gtranslation = RotationUtil.vecPlayerToWorld(gtranslation,gravityDirection);
                stack.translate(gtranslation.x, gtranslation.y, gtranslation.z);

                float opacity = 0.5F;
                if (ConfigManager.getClientConfig() != null && ConfigManager.getClientConfig().opacitySettings != null) {
                    opacity = ConfigManager.getClientConfig().opacitySettings.opacityOfPlayerBarrageArms;
                }
                stack.mulPose(Axis.ZP.rotationDegrees(180f));
                stack.mulPose(Axis.XP.rotationDegrees(-22));
                ModStrayModels.barrageArmsPart.render(cameraEnt, cameraEnt.tickCount + $$4, stack, source, light,
                        r, g, b, opacity);
                stack.popPose();
            } else if (bt == PlayerPosIndex.HAIR_SPIKE_2 || bt == PlayerPosIndex.HAIR_SPIKE) {
                stack.pushPose();
                boolean isHurt = play.hurtTime > 0;

                IPlayerEntity pl = ((IPlayerEntity) cameraEnt);
                float r = pl.rdbt$getHairColorX();
                float g = pl.rdbt$getHairColorY();
                float b = pl.rdbt$getHairColorZ();


                ItemStack visage = pl.roundabout$getMaskSlot();
                if (visage != null && !visage.isEmpty() && visage.getItem() instanceof MaskItem ME) {
                    VisageData vd = ME.visageData;
                    if (vd != null && vd.isCharacterVisage()) {
                        r = ((float) vd.getHairColor().getX()) / 255;
                        g = ((float) vd.getHairColor().getY()) / 255;
                        b = ((float) vd.getHairColor().getZ()) / 255;
                    }
                }
                Vec3 gtranslation = new Vec3(0, 0.1, 0);
                stack.translate(gtranslation.x, gtranslation.y, gtranslation.z);

                stack.mulPose(Axis.ZP.rotationDegrees(180f));
                stack.mulPose(Axis.XP.rotationDegrees(-22));
                ModStrayModels.bodySpikePart.render(cameraEnt, cameraEnt.tickCount + $$4, stack, source, light,
                        r, g, b, 1);
                stack.popPose();
            }

            IPlayerEntity pl = ((IPlayerEntity) cameraEnt);

            boolean slimBoolean = false;

            EntityRenderer<?> renderer =
                    Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(play);
            if (renderer instanceof PlayerRenderer playerRenderer) {
                PlayerModel<?> model = playerRenderer.getModel();
                IPlayerModel ipm = (IPlayerModel) model;
                slimBoolean = ipm.roundabout$getSlim();
            }

            Item item = play.getUseItem().getItem();
            if (item instanceof FirearmItem) {
                stack.pushPose();

                FirstPersonArmsModel.player = play;
                FirstPersonArmsSlimModel.player = play;



                if (play.getCooldowns().isOnCooldown(play.getUseItem().getItem())) {
                    pl.roundabout$getItemAnimationActive().startIfStopped(cameraEnt.tickCount);
                    pl.roundabout$getItemAnimation().stop();
                } else {
                    pl.roundabout$getItemAnimationActive().stop();
                    pl.roundabout$getItemAnimation().startIfStopped(cameraEnt.tickCount);
                }


                if (!(item instanceof JackalRifleItem)) {
                    if (slimBoolean) {
                        ModStrayModels.FirstPersonArmsSlimModel.render(cameraEnt, cameraEnt.tickCount + $$4, stack, source, light);
                    } else {
                        ModStrayModels.FirstPersonArmsModel.render(cameraEnt, cameraEnt.tickCount + $$4, stack, source, light);
                    }
                }

                if (item instanceof SnubnoseRevolverItem) {
                    ModStrayModels.FirstPersonSnubnoseModel.render(cameraEnt, cameraEnt.tickCount+$$4, stack, source, light);
                } else if (item instanceof ColtRevolverItem) {
                    ModStrayModels.FirstPersonColtRevolverModel.render(cameraEnt, cameraEnt.tickCount+$$4, stack, source, light);
                } else if (item instanceof TommyGunItem) {
                    ModStrayModels.FirstPersonTommyGunModel.render(cameraEnt, cameraEnt.tickCount+$$4, stack, source, light);
                }


                stack.popPose();
            } else {
                pl.roundabout$getItemAnimation().stop();
                pl.roundabout$getItemAnimationActive().stop();
            }

            if (AnubisLayer.shouldRender(play) != null) {
                  ModStrayModels.ANUBIS.renderFirstPerson(stack,source,light,play,$$4);
            }

        }

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

    public static void handleBowlerHatFlySound(Entity entity) {
        Minecraft.getInstance().getSoundManager().play(new BowlerHatFlyingSound(ModSounds.BOWLER_HAT_FLY_SOUND_EVENT, SoundSource.PLAYERS, 1, 0, entity));
    }

    public static void applyJusticeFogBlockTextureOverlayInInventory(ItemStack $$0, ItemDisplayContext $$1, boolean $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, int $$6,
                                                                     ItemModelShaper shaper, BlockEntityWithoutLevelRenderer renderer, ItemRenderer itemRenderer){
        boolean $$8 = $$1 == ItemDisplayContext.GUI;
        if ($$8){
            Lighting.setupForFlatItems();
            BakedModel $$7 = shaper.getModelManager().getModel(ModItemModels.FOG_BLOCK_ICON);
            $$3.pushPose();

            $$7.getTransforms().getTransform($$1).apply($$2, $$3);
            $$3.translate(-0.5F, -0.5F, 0.5F);

            if (!$$7.isCustomRenderer()) {
                boolean $$10;
                $$10 = true;

                RenderType $$12 = ItemBlockRenderTypes.getRenderType($$0, $$10);
                VertexConsumer $$14;
                $$14 = ItemRenderer.getFoilBufferDirect($$4, $$12, true, $$0.hasFoil());

                ((IItemRenderer)itemRenderer).roundabout$renderModelLists($$7, $$0, $$5, $$6, $$3, $$14);
            } else {
                renderer.renderByItem($$0, $$1, $$3, $$4, $$5, $$6);
            }

            $$3.popPose();
        }
    }

    public static boolean forceEntityRendering(Entity entity){
        if (entity instanceof Player pl){
            if (((StandUser)pl).roundabout$hasStandOut()){
                return true;
            }
            if (((IPlayerEntity)pl).roundabout$GetPos2() == PlayerPosIndex.RIPPER_EYES_ACTIVE){
                return true;
            }
        }
        return false;
    }

    /**Simulating Blood Bar and whatnot*/
    public static void renderHungerStuff(GuiGraphics graphics, Player player, int width, int height, int rand,
                                         int foodlevel, int tickCount){

        if (FateTypes.hasBloodHunger(player)){
            for (int $$23 = 0; $$23 < 10; $$23++) {
                int $$24 = height;
                int $$25 = 238;
                int $$26 = 238;
                int sizey2 = 0;
                if (player.hasEffect(ModEffects.BLEED)) {
                    $$25 = 229;
                    $$26 = 229;
                    sizey2 = 0;
                }

                //if (player.getFoodData().getSaturationLevel() <= 0.0F && tickCount % (foodlevel * 3 + 1) == 0) {
                //    $$24 = height + (rand - 1);
                //}

                int $$27 = width - $$23 * 8 - 9;
                graphics.blit(StandIcons.JOJO_ICONS_2, $$27, $$24, $$26, 18, 9, 9);
                if ($$23 * 2 + 1 < foodlevel) {
                    graphics.blit(StandIcons.JOJO_ICONS_2, $$27, $$24, $$25, sizey2, 9, 9);
                }

                if ($$23 * 2 + 1 == foodlevel) {
                    graphics.blit(StandIcons.JOJO_ICONS_2, $$27, $$24, $$25, sizey2+9, 9, 9);
                }
            }
        }
    }
}
