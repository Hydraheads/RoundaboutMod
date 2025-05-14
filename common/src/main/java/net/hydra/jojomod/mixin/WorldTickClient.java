package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.SetBlockInstance;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class WorldTickClient extends Level implements IClientLevel {

    /** Called every tick on the Client. Checks if a mob has a stand out, and updates the position of the stand.
     * @see StandEntity#tickStandOut */

    @Shadow
    @Final
   EntityTickList tickingEntities;

    protected WorldTickClient(WritableLevelData $$0, ResourceKey<Level> $$1, RegistryAccess $$2, Holder<DimensionType> $$3, Supplier<ProfilerFiller> $$4, boolean $$5, boolean $$6, long $$7, int $$8) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8);
    }


    @Unique
    private void roundabout$standTickCheck(Entity entity){
        if (entity.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) livingEntity).roundabout$getStand() != null) {
                StandEntity stand = ((StandUser) livingEntity).roundabout$getStand();
                if (stand.getFollowing() != null && stand.getFollowing().getId() == livingEntity.getId()){

                    if (!(entity.getVehicle() != null && entity.getVehicle() == ((StandUser) entity).roundabout$getStand())) {
                        this.roundabout$tickStandIn(livingEntity, stand);
                    }
                }
            }
        }
    }

    @Unique
    private void roundabout$updateStandTS(Entity entity){
        if (entity.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) livingEntity).roundabout$getStand() != null) {
                StandEntity stand = ((StandUser) livingEntity).roundabout$getStand();
                if (stand.getFollowing() != null && stand.getFollowing().getId() == livingEntity.getId()){
                    if (!(stand.isRemoved() || stand.getUser() != entity)) {
                        roundabout$TickLivingEntityTS(stand);
                    }
                }
            }
        }
    }

    @Unique
    private void roundabout$tickStandIn(LivingEntity entity, StandEntity stand) {
        if (stand == null || stand.isRemoved()) {
            if (entity !=null) {
                ((StandUser) entity).roundabout$removeFollower(stand);
            }
            return;
        }
        if (stand.getFollowing() != null && entity != null && stand.getFollowing().getId() != entity.getId()) {
            ((StandUser) entity).roundabout$removeFollower(stand);
            return;
        }
        byte ot = stand.getOffsetType();
        ++stand.tickCount;


        Entity user = stand.getUser();
        Entity following = stand.getFollowing();
        if (!stand.level().isClientSide() || (user != null && !user.is(stand) && !user.isRemoved()) ||
                (following != null && !following.is(stand) && !following.isRemoved())) {
            stand.setOldPosAndRot();
            stand.tickStandOut();
        } else {
            stand.setOldPosAndRot();
            stand.tick();
        }
        for (Entity $$2 : stand.getPassengers()) {
            this.tickPassenger(stand, $$2);
        }
    }


    @Unique
    private void roundabout$TickStandTS(StandEntity stand){
        stand.setOldPosAndRot();
        stand.tickStandOut2();
    }
    @Unique
    private void roundabout$TickLivingEntityTS(LivingEntity livingEntity){
        if (livingEntity instanceof Player){
            Inventory inv = ((IPlayerEntity) livingEntity).roundabout$GetInventory();
            int idx = 0;
            for(NonNullList<ItemStack> nonnulllist : ((ZInventoryAccess)inv).roundabout$GetCompartments()) {
                for(int i = 0; i < nonnulllist.size(); ++i) {

                    if (!nonnulllist.get(i).isEmpty()) {
                        if (nonnulllist.get(i).getPopTime() > 0) {
                            nonnulllist.get(i).setPopTime(nonnulllist.get(i).getPopTime()-1);
                        }
                    }
                    idx++;
                }
            }
        }


        ((ILivingEntityAccess) livingEntity).roundabout$setAnimStepO(((ILivingEntityAccess) livingEntity).roundabout$getAnimStep());
        livingEntity.setOldPosAndRot();
        livingEntity.yBodyRotO = livingEntity.yBodyRot;
        livingEntity.yHeadRotO = livingEntity.yHeadRot;
        livingEntity.oAttackAnim = livingEntity.attackAnim;
        //livingEntity.lastLimbDistance = livingEntity.limbDistance;

        if (livingEntity instanceof StandEntity){
            livingEntity.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());

        } else {
            int LS = ((ILivingEntityAccess) livingEntity).roundabout$getLerpSteps();
            if (LS > 0) {
                double LX = livingEntity.getX() + (((ILivingEntityAccess) livingEntity).roundabout$getLerpX() - livingEntity.getX()) / (double) LS;
                double LY = livingEntity.getY() + (((ILivingEntityAccess) livingEntity).roundabout$getLerpY() - livingEntity.getY()) / (double) LS;
                double LZ = livingEntity.getZ() + (((ILivingEntityAccess) livingEntity).roundabout$getLerpZ() - livingEntity.getZ()) / (double) LS;
                ((ILivingEntityAccess) livingEntity).roundabout$setLerpSteps(LS - 1);
                livingEntity.setPos(LX, LY, LZ);
            }

            ((ILivingEntityAccess) livingEntity).roundabout$PushEntities();
        }
    }
    @Shadow
    private void tickPassenger(Entity $$0, Entity $$1) {
    }

    @Shadow @Final private Minecraft minecraft;

    @Unique
    public void roundabout$TSTickEntity(Entity $$0){
        if ($$0 instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) $$0;
            roundabout$TickLivingEntityTS(livingEntity);
            ((StandUser)livingEntity).roundabout$getStandPowers().timeTick();
            roundabout$updateStandTS(livingEntity);
            $$0.invulnerableTime = 0;
            ((LivingEntity) $$0).hurtTime = 0;
        } else {
            $$0.walkDistO = $$0.walkDist;

            $$0.xOld = $$0.getX();;
            $$0.yOld = $$0.getY();
            $$0.zOld = $$0.getZ();

            if ($$0 instanceof FishingHook){
                $$0.xo = $$0.getX();
                $$0.yo = $$0.getY();
                $$0.zo = $$0.getZ();
            } else if ($$0 instanceof Boat){
                $$0.xo = $$0.getX();
                $$0.yo = $$0.getY();
                $$0.zo = $$0.getZ();
                ((IBoatAccess)$$0).roundabout$TickLerp();
                $$0.lerpTo($$0.getX(),$$0.getY(),$$0.getZ(),$$0.getYRot(),$$0.getXRot(),3,false);
                $$0.walkDistO = $$0.walkDist;
                $$0.xRotO = $$0.getXRot();
                $$0.yRotO = $$0.getYRot();
            }
        }
        if ($$0 instanceof ItemEntity) {
            ((IItemEntityAccess)$$0).roundabout$TickPickupDelay();
        } else if ($$0 instanceof FishingHook){
            ((IFishingRodAccess)$$0).roundabout$UpdateRodInTS();
        }
    }

    /**Time Stop Code*/
    @Inject(method = "tickNonPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TickEntity2(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if ($$0 instanceof StandEntity SE) {
                if (SE.getFollowing() != null && ((StandUser)SE.getFollowing()).roundabout$getFollowers().contains(SE)){
                    ci.cancel();
                }
            }

            if ($$0 instanceof LivingEntity) {
                ((StandUser) $$0).roundabout$UniversalTick();
            }

            roundabout$StoreOldPositionsForTS($$0);
            if (((TimeStop) this).CanTimeStopEntity($$0)){
                roundabout$TSTickEntity($$0);
                for (Entity $$1 : $$0.getPassengers()) {
                    this.tickPassenger($$0, $$1);
                }
                ci.cancel();
            }
        }
    }

    /**Here is where we generate all the lovely Magician's Red flame particles when firestorm is active*/

    @Inject(method = "doAnimateTick(IIIILnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos$MutableBlockPos;)V", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$doAnimateTick(int $$0, int $$1, int $$2, int $$3, RandomSource $$4, Block $$5, BlockPos.MutableBlockPos $$6, CallbackInfo ci) {

        LivingEntity roundabout$spawnMRTicks = (((IPermaCasting)this).roundabout$inPermaCastRangeEntity(new Vec3i($$6.getX(),$$6.getY(),$$6.getZ()), PermanentZoneCastInstance.FIRESTORM));
        if (roundabout$spawnMRTicks != null && ((StandUser)roundabout$spawnMRTicks).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            BlockState $$10 = this.getBlockState($$6);
            if (!$$10.isCollisionShapeFullBlock(this, $$6)) {
                if (roundabout$canSpawn(this.random)) {
                    /**
                    float randomX = (float) ((Math.random()* 0.1) - 0.05F);
                    float randomY = (float) ((Math.random()* 0.1) - 0.05F);
                    float randomZ = (float) ((Math.random()* 0.1) - 0.05F);
                     **/
                    Vec3 pushVec = new Vec3((this.getSharedSpawnPos().getX()-$$6.getX()),
                            0,
                            this.getSharedSpawnPos().getZ()-$$6.getZ()).normalize();
                    /**
                    Vec3 pushVec = new Vec3((roundabout$spawnMRTicks.getX()-$$6.getX()),
                            (roundabout$spawnMRTicks.getY()-$$6.getY()),
                            (roundabout$spawnMRTicks.getZ()-$$6.getZ())).normalize();
                     **/
                    this.addParticle(
                            PMR.getFlameParticle(),
                            (double)$$6.getX() + this.random.nextDouble(),
                            (double)$$6.getY() + this.random.nextDouble(),
                            (double)$$6.getZ() + this.random.nextDouble(),
                            0.5*pushVec.x,
                            -0.3,
                            0.5*pushVec.z
                    );
                }
            }
        }
    }

    /**The rate of particles for magician's red embers while ankh is active*/
    @Unique
    public boolean roundabout$canSpawn(RandomSource $$0) {
        return $$0.nextFloat() <= ConfigManager.getClientConfig().particleSettings.magiciansRedFirestormEmbersRate;
    }

    /**Code to tick stands on the client*/
    @Inject(method = "tickNonPassenger", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$TickEntityX(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if ($$0 instanceof LivingEntity LE) {
                for (StandEntity SE : ((StandUser) $$0).roundabout$getFollowers()) {
                    this.roundabout$tickStandIn(LE, SE);
                }
            }
        }
    }

    /**Forcefully hold entities in place, but visually for the client*/
    @Unique
    public void roundabout$StoreOldPositionsForTS(Entity entity){
        ((IEntityAndData) entity).roundabout$setRoundaboutPrevX(entity.getX());
        ((IEntityAndData) entity).roundabout$setRoundaboutPrevY(entity.getY());
        ((IEntityAndData) entity).roundabout$setRoundaboutPrevZ(entity.getZ());
    }

    @Inject(method = "tickPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TickEntity5(Entity $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof StandEntity SE) {
            if (SE.getFollowing() != null){
                ci.cancel();
            }
        }
        if ($$1 instanceof LivingEntity) {
            ((StandUser) $$1).roundabout$UniversalTick();
        }

        roundabout$StoreOldPositionsForTS($$1);
        if ($$1.isRemoved() || $$1.getVehicle() != $$0) {
            $$1.stopRiding();
        } else if ($$1 instanceof Player || this.tickingEntities.contains($$1)) {
            if (((TimeStop) this).CanTimeStopEntity($$1)) {
                $$1.setDeltaMovement(Vec3.ZERO);
                roundabout$TSTickEntity($$1);
                if ($$1.isPassenger()) {
                    $$1.getVehicle().positionRider($$1);
                }
                for (Entity $$2 : $$1.getPassengers()) {
                    this.tickPassenger($$1, $$2);
                }
                ci.cancel();
            }
        }
    }

    /**When a stand is literally a passenger, allows it to still tick*/
    @Inject(method = "tickPassenger", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$TickEntity6(Entity $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof LivingEntity LE) {
            for (StandEntity SE : ((StandUser)$$1).roundabout$getFollowers()) {
                this.roundabout$tickStandIn(LE, SE);
            }
        }
    }
    @Unique
    private ImmutableList<SetBlockInstance> roundabout$setBlockInstance = ImmutableList.of();
    @Inject(method = "setBlock", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$setBlock(BlockPos $$0, BlockState $$1, int $$2, int $$3, CallbackInfoReturnable<Boolean> cir) {
        if (ClientUtil.getScreenFreeze()) {

            if (roundabout$setBlockInstance == null){
                roundabout$setBlockInstance = ImmutableList.of();
            }
            if (roundabout$setBlockInstance.isEmpty()) {
                roundabout$setBlockInstance = ImmutableList.of(new SetBlockInstance($$0,$$1,$$2,$$3));
            } else {
                List<SetBlockInstance> $$b = Lists.newArrayList(roundabout$setBlockInstance);
                $$b.add(new SetBlockInstance($$0,$$1,$$2,$$3));
                roundabout$setBlockInstance = ImmutableList.copyOf($$b);
            }
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "setServerVerifiedBlockState", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$setBlock2(BlockPos $$0, BlockState $$1, int $$2, CallbackInfo ci) {
        if (!this.blockStatePredictionHandler.updateKnownServerState($$0, $$1)) {
            if (ClientUtil.getScreenFreeze()) {

                if (roundabout$setBlockInstance == null) {
                    roundabout$setBlockInstance = ImmutableList.of();
                }
                if (roundabout$setBlockInstance.isEmpty()) {
                    roundabout$setBlockInstance = ImmutableList.of(new SetBlockInstance($$0, $$1, $$2, 512));
                } else {
                    List<SetBlockInstance> $$b = Lists.newArrayList(roundabout$setBlockInstance);
                    $$b.add(new SetBlockInstance($$0, $$1, $$2, 512));
                    roundabout$setBlockInstance = ImmutableList.copyOf($$b);
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void roundabout$tickInGeneral(BooleanSupplier $$0, CallbackInfo ci) {
        ((ILevelAccess)this).roundabout$tickPlunderBubbleRemoval();
        if (!ClientUtil.getScreenFreeze()) {

            if (roundabout$setBlockInstance == null){
                roundabout$setBlockInstance = ImmutableList.of();
            }
            if (!roundabout$setBlockInstance.isEmpty()){
                for (int i = roundabout$setBlockInstance.size() - 1; i >= 0; --i) {
                    SetBlockInstance sbi = roundabout$setBlockInstance.get(i);
                    setBlock(sbi.pos,sbi.state,sbi.integer,sbi.integer2);
                }
                roundabout$setBlockInstance = ImmutableList.of();
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void roundabout$tickInGeneralTail(BooleanSupplier $$0, CallbackInfo ci) {
        ((ILevelAccess)this).roundabout$tickPlunderBubbleRemoval();
    }

    @Inject(method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$playSeed(Player $$0, Entity $$1, Holder<SoundEvent> $$2, SoundSource $$3, float $$4, float $$5, long $$6, CallbackInfo ci) {
        if (ClientUtil.getScreenFreeze()){
            ci.cancel();
        } if(((ILevelAccess)this).roundabout$isSoundPlundered($$1.blockPosition())){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).roundabout$getSoundPlunderedBubble($$1.blockPosition());
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2.value(), $$3, $$4, $$5);
            }
            ci.cancel();
        } else if(((ILevelAccess)this).roundabout$isSoundPlunderedEntity($$1)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).roundabout$getSoundPlunderedBubbleEntity($$1);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2.value(), $$3, $$4, $$5);
            }
            ci.cancel();
        }
    }
    @Inject(method = "playSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZJ)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$playSeed(double $$0, double $$1, double $$2, SoundEvent $$3, SoundSource $$4, float $$5, float $$6, boolean $$7, long $$8, CallbackInfo ci) {
        BlockPos bpos = new BlockPos((int) $$0, (int) $$1, (int) $$2);
        if (ClientUtil.getScreenFreeze()){
            ci.cancel();
            return;
        } if(((ILevelAccess)this).roundabout$isSoundPlundered(bpos)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).roundabout$getSoundPlunderedBubble(bpos);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$3, $$4, $$5, $$6);
            }
            ci.cancel();
        }
    }
    @Inject(method = "playLocalSound", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$playLocalSound(double $$0, double $$1, double $$2, SoundEvent $$3, SoundSource $$4, float $$5, float $$6, boolean $$7, CallbackInfo ci) {
        BlockPos bpos = new BlockPos((int) $$0, (int) $$1, (int) $$2);
        if (ClientUtil.getScreenFreeze()){
            ci.cancel();
            return;
        } if(((ILevelAccess)this).roundabout$isSoundPlundered(bpos)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).roundabout$getSoundPlunderedBubble(bpos);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$3, $$4, $$5, $$6);
            }
            ci.cancel();
            return;
        } if (((TimeStop) this).inTimeStopRange(new Vec3i((int) $$0, (int) $$1, (int) $$2))){
            if (($$4 == SoundSource.WEATHER || $$4 == SoundSource.BLOCKS) && !$$3.getLocation().getPath().contains("break")) {
                ci.cancel();
                return;
            }
        }
    }
    @Inject(method = "playSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZJ)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$playSound(double $$0, double $$1, double $$2, SoundEvent $$3, SoundSource $$4, float $$5, float $$6, boolean $$7, long $$8, CallbackInfo ci) {
        BlockPos bpos = new BlockPos((int) $$0, (int) $$1, (int) $$2);
        if(((ILevelAccess)this).roundabout$isSoundPlundered(bpos)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).roundabout$getSoundPlunderedBubble(bpos);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$3, $$4, $$5, $$6);
            }
            ci.cancel();
            return;
        }
    }

    @Shadow
    @Final
    private ClientLevel.ClientLevelData clientLevelData;

    @Shadow
    public void setGameTime(long L){}

    @Shadow
    public void setDayTime(long L) {}

    /**When time ticks amidst a time change, use the actual time rater
     * than the custom time to tick the proper time*/
    @Inject(method = "tickTime", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TickTime(CallbackInfo ci) {
        if (((IClientLevelData) this.levelData).roundabout$getRoundaboutInterpolatingDaytime()) {
            this.setGameTime(this.levelData.getGameTime() + 1L);
            if (this.levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
                this.setDayTime(((IClientLevelData)this.levelData).roundabout$getRoundaboutDayTimeMinecraft() + 1L);
                ci.cancel();
            }
        }

        if (ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1){
            if (((TimeStop) this).inTimeStopRange(new Vec3i((int) 0, (int) 0, (int) 0))){
                ci.cancel();
            }
        }
    }

    @Shadow
    public int getSkyFlashTime() {
        return 0;
    }

    @Shadow public abstract boolean setBlock(BlockPos $$0, BlockState $$1, int $$2, int $$3);

    @Shadow @Final private BlockStatePredictionHandler blockStatePredictionHandler;

    @Unique
    @Override
    public float roundabout$getSkyLerp(){
        return roundabout$skyLerp;
    }
    @Unique
    @Override
    public float roundabout$getMaxSkyLerp(){
        return roundabout$maxSkyLerp;
    }

    @Unique
    public float roundabout$skyLerp = 0;
    @Unique
    public float roundabout$maxSkyLerp = 30;
    @Unique
    public Vec3 roundabout$fogSky = new Vec3(
            ConfigManager.getClientConfig().justiceFogBrightness,
            ConfigManager.getClientConfig().justiceFogBrightness,
            ConfigManager.getClientConfig().justiceFogBrightness);

    /*Fog color, fog sky color, fog sky brightness*/
    @Inject(method = "getSkyColor", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$getSkyColor(Vec3 cameraPos, float tickDelta, CallbackInfoReturnable<Vec3> cir) {
        if (minecraft.player != null) {

            /**
            if (((IPlayerEntity)minecraft.player).roundabout$getBlinded()){
                cir.setReturnValue(new Vec3(-40,-40,-40));
                return;
            }
             **/

            if (roundabout$skyLerp >= 1) {
                float lerp = tickDelta;
                if (!((IPermaCasting) minecraft.player.level()).roundabout$inPermaCastFogRange(minecraft.player)) {
                    lerp *= -1;
                }
                if (roundabout$skyLerp >= roundabout$maxSkyLerp) {
                    lerp = 0;
                }

                float f = this.getTimeOfDay(tickDelta);
                Vec3 vec3 = cameraPos.subtract(2.0D, 2.0D, 2.0D).scale(0.25D);
                BiomeManager biomemanager = this.getBiomeManager();
                Vec3 vec31 = CubicSampler.gaussianSampleVec3(vec3, (p_194161_, p_194162_, p_194163_) -> {
                    return Vec3.fromRGB24(biomemanager.getNoiseBiomeAtQuart(p_194161_, p_194162_, p_194163_).value().getSkyColor());
                });

                float f1 = Mth.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
                f1 = Mth.clamp(f1, 0.0F, 1.0F);
                float f2 = (float) vec31.x * f1;
                float f3 = (float) vec31.y * f1;
                float f4 = (float) vec31.z * f1;

                float f5 = this.getRainLevel(tickDelta);
                if (f5 > 0.0F) {
                    float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
                    float f7 = 1.0F - f5 * 0.75F;
                    f2 = f2 * f7 + f6 * (1.0F - f7);
                    f3 = f3 * f7 + f6 * (1.0F - f7);
                    f4 = f4 * f7 + f6 * (1.0F - f7);
                }

                float f9 = this.getThunderLevel(tickDelta);
                if (f9 > 0.0F) {
                    float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
                    float f8 = 1.0F - f9 * 0.75F;
                    f2 = f2 * f8 + f10 * (1.0F - f8);
                    f3 = f3 * f8 + f10 * (1.0F - f8);
                    f4 = f4 * f8 + f10 * (1.0F - f8);
                }

                int i = this.getSkyFlashTime();
                if (i > 0) {
                    float f11 = (float) i - tickDelta;
                    if (f11 > 1.0F) {
                        f11 = 1.0F;
                    }

                    f11 *= 0.45F;
                    f2 = f2 * (1.0F - f11) + 0.8F * f11;
                    f3 = f3 * (1.0F - f11) + 0.8F * f11;
                    f4 = f4 * (1.0F - f11) + 1.0F * f11;
                }

                Vec3 interp = new Vec3((double) f2, (double) f3, (double) f4)
                        .lerp(roundabout$fogSky, ((((roundabout$skyLerp + lerp) / roundabout$maxSkyLerp)) / 100) * 0.95);

                cir.setReturnValue(interp);
            }
        }
    }

    @Inject(method = "tickEntities", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TickEntity3(CallbackInfo ci) {


        if (minecraft.player != null){
            if (((IPermaCasting)minecraft.player.level()).roundabout$inPermaCastFogRange(minecraft.player)){
                Roundabout.worldInFog = 1;
            } else {
                Roundabout.worldInFog = 0;
            }

            if (((IPermaCasting)minecraft.player.level()).roundabout$inPermaCastFogRange(minecraft.player)){
                if (roundabout$skyLerp < roundabout$maxSkyLerp){
                    roundabout$skyLerp++;
                }
            } else {
                if (roundabout$skyLerp > 0){
                    roundabout$skyLerp--;
                }
            }

            if (((TimeStop) this).isTimeStoppingEntity(minecraft.player)) {
                ((StandUser) minecraft.player).roundabout$getStandPowers().timeTickStopPower();
            }
        }
        this.tickingEntities.forEach($$0x -> {
            if ($$0x instanceof StandEntity standEntity) {
                if (standEntity.getFollowing() != null && !standEntity.getFollowing().isRemoved()){
                    LivingEntity LE = standEntity.getFollowing();
                    if (!((StandUser)LE).roundabout$hasFollower(standEntity)){
                        ((StandUser)LE).roundabout$addFollower(standEntity);
                    }

                } else {
                    roundabout$tickStandIn(null,standEntity);
                }
            }
        });
    }

    @Inject(method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void roundabout$StopParticles(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange(new Vec3i((int) x, (int) y, (int) z))){
            ci.cancel();
        }
    }

}
