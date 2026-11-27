package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.SetBlockInstance;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.mixin.access.AccessInventory;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.util.config.ConfigManager;
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
     * @see FollowingStandEntity#tickStandOut */

    @Shadow
    @Final
   EntityTickList tickingEntities;

    protected WorldTickClient(WritableLevelData $$0, ResourceKey<Level> $$1, RegistryAccess $$2, Holder<DimensionType> $$3, Supplier<ProfilerFiller> $$4, boolean $$5, boolean $$6, long $$7, int $$8) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8);
    }


    @Unique
    private void sba$standTickCheck(Entity entity){
        if (entity.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) livingEntity).sba$getStand() != null) {
                StandEntity stand = ((StandUser) livingEntity).sba$getStand();
                if (stand instanceof FollowingStandEntity FE && FE.getFollowing() != null && FE.getFollowing().getId() == livingEntity.getId()){

                    if (!(entity.getVehicle() != null && entity.getVehicle() == ((StandUser) entity).sba$getStand())) {
                        this.sba$tickStandIn(livingEntity, FE);
                    }
                }
            }
        }
    }

    @Unique
    private void sba$updateStandTS(Entity entity){
        if (entity.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) livingEntity).sba$getStand() instanceof FollowingStandEntity FE) {
                StandEntity stand = ((StandUser) livingEntity).sba$getStand();
                if (FE.getFollowing() != null && FE.getFollowing().getId() == livingEntity.getId()
                        && !FE.getFollowing().isRemoved()){
                    if (!(stand.isRemoved() || stand.getUser() != entity)) {
                        sba$TickLivingEntityTS(stand);
                    }
                }
            }
        }
    }

    @Unique
    private void sba$tickStandIn(LivingEntity entity, FollowingStandEntity stand) {
        if (stand == null || stand.isRemoved()) {
            if (entity !=null) {
                ((StandUser) entity).sba$removeFollower(stand);
            }
            return;
        }
        if (stand.getFollowing() != null && entity != null && stand.getFollowing().getId() != entity.getId()) {
            ((StandUser) entity).sba$removeFollower(stand);
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
    private void sba$TickLivingEntityTS(LivingEntity livingEntity){
        if (livingEntity instanceof Player){
            Inventory inv = ((IPlayerEntity) livingEntity).sba$GetInventory();
            int idx = 0;
            for(NonNullList<ItemStack> nonnulllist : ((AccessInventory)inv).sba$GetCompartments()) {
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

        //if (!livingEntity.isPassenger())
            ((ILivingEntityAccess) livingEntity).sba$setAnimStepO(((ILivingEntityAccess) livingEntity).sba$getAnimStep());
            livingEntity.setOldPosAndRot();
        //if (!livingEntity.isPassenger()){
            livingEntity.yBodyRotO = livingEntity.yBodyRot;
            livingEntity.yHeadRotO = livingEntity.yHeadRot;
            livingEntity.oAttackAnim = livingEntity.attackAnim;
        //}
        //livingEntity.lastLimbDistance = livingEntity.limbDistance;

        if (livingEntity instanceof StandEntity){
            livingEntity.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());

        } else {
            int LS = ((ILivingEntityAccess) livingEntity).sba$getLerpSteps();
            if (LS > 0) {
                //if (!livingEntity.isPassenger())
                    //{
                        double LX = livingEntity.getX() + (((ILivingEntityAccess) livingEntity).sba$getLerpX() - livingEntity.getX()) / (double) LS;
                        double LY = livingEntity.getY() + (((ILivingEntityAccess) livingEntity).sba$getLerpY() - livingEntity.getY()) / (double) LS;
                        double LZ = livingEntity.getZ() + (((ILivingEntityAccess) livingEntity).sba$getLerpZ() - livingEntity.getZ()) / (double) LS;
                        ((ILivingEntityAccess) livingEntity).sba$setLerpSteps(LS - 1);
                        livingEntity.setPos(LX, LY, LZ);
                    //}
            }

            ((ILivingEntityAccess) livingEntity).sba$PushEntities();
        }
    }
    @Shadow
    private void tickPassenger(Entity $$0, Entity $$1) {
    }

    @Shadow @Final private Minecraft minecraft;

    @Unique
    public void sba$TSTickEntity(Entity $$0){
        if ($$0 instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) $$0;
            sba$TickLivingEntityTS(livingEntity);
            ((StandUser)livingEntity).sba$getStandPowers().timeTick();
            sba$updateStandTS(livingEntity);
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
                ((IBoatAccess)$$0).sba$TickLerp();
                $$0.lerpTo($$0.getX(),$$0.getY(),$$0.getZ(),$$0.getYRot(),$$0.getXRot(),3,false);
                $$0.walkDistO = $$0.walkDist;
                $$0.xRotO = $$0.getXRot();
                $$0.yRotO = $$0.getYRot();
            }
        }
        if ($$0 instanceof ItemEntity) {
            ((IItemEntityTimeStopAccess)$$0).sba$TickPickupDelay();
        } else if ($$0 instanceof FishingHook){
            ((IFishingRodAccess)$$0).sba$UpdateRodInTS();
        }
    }

    /**Time Stop Code*/
    @Inject(method = "tickNonPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void sba$TickEntity2(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if ($$0 instanceof FollowingStandEntity SE) {
                if (SE.getFollowing() != null && !SE.getFollowing().isRemoved() && ((StandUser)SE.getFollowing()).sba$getFollowers().contains(SE)){
                    ci.cancel();
                }
            }

            if ($$0 instanceof LivingEntity) {
                ((StandUser) $$0).sba$UniversalTick();
            }

            sba$StoreOldPositionsForTS($$0);
            if (((TimeStop) this).CanTimeStopEntity($$0)){
                sba$TSTickEntity($$0);
                for (Entity $$1 : $$0.getPassengers()) {
                    this.tickPassenger($$0, $$1);
                }
                ci.cancel();
            }
        }
    }

    /**Here is where we generate all the lovely Magician's Red flame particles when firestorm is active*/

    @Inject(method = "doAnimateTick(IIIILnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos$MutableBlockPos;)V", at = @At(value = "TAIL"), cancellable = true)
    private void sba$doAnimateTick(int $$0, int $$1, int $$2, int $$3, RandomSource $$4, Block $$5, BlockPos.MutableBlockPos $$6, CallbackInfo ci) {

        LivingEntity sba$spawnMRTicks = (((IPermaCasting)this).sba$inPermaCastRangeEntity(new Vec3i($$6.getX(),$$6.getY(),$$6.getZ()), PermanentZoneCastInstance.FIRESTORM));
        if (sba$spawnMRTicks != null && ((StandUser)sba$spawnMRTicks).sba$getStandPowers() instanceof PowersMagiciansRed PMR){
            BlockState $$10 = this.getBlockState($$6);
            if (!$$10.isCollisionShapeFullBlock(this, $$6)) {
                if (sba$canSpawn(this.random)) {
                    /**
                    float randomX = (float) ((Math.random()* 0.1) - 0.05F);
                    float randomY = (float) ((Math.random()* 0.1) - 0.05F);
                    float randomZ = (float) ((Math.random()* 0.1) - 0.05F);
                     **/
                    Vec3 pushVec = new Vec3((this.getSharedSpawnPos().getX()-$$6.getX()),
                            0,
                            this.getSharedSpawnPos().getZ()-$$6.getZ()).normalize();
                    /**
                    Vec3 pushVec = new Vec3((sba$spawnMRTicks.getX()-$$6.getX()),
                            (sba$spawnMRTicks.getY()-$$6.getY()),
                            (sba$spawnMRTicks.getZ()-$$6.getZ())).normalize();
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
    public boolean sba$canSpawn(RandomSource $$0) {
        return $$0.nextFloat() <= ConfigManager.getClientConfig().particleSettings.magiciansRedFirestormEmbersRate;
    }

    /**Code to tick stands on the client*/
    @Inject(method = "tickNonPassenger", at = @At(value = "TAIL"), cancellable = true)
    private void sba$TickEntityX(Entity $$0, CallbackInfo ci) {
        if (!$$0.isRemoved()) {
            if ($$0 instanceof LivingEntity LE) {
                for (FollowingStandEntity SE : ((StandUser) $$0).sba$getFollowers()) {
                    this.sba$tickStandIn(LE, SE);
                }
            }
        }
    }

    /**Forcefully hold entities in place, but visually for the client*/
    @Unique
    public void sba$StoreOldPositionsForTS(Entity entity){
        ((IEntityAndData) entity).sba$setSBAPrevX(entity.getX());
        ((IEntityAndData) entity).sba$setSBAPrevY(entity.getY());
        ((IEntityAndData) entity).sba$setSBAPrevZ(entity.getZ());
    }

    @Inject(method = "tickPassenger", at = @At(value = "HEAD"), cancellable = true)
    private void sba$TickEntity5(Entity $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof FollowingStandEntity SE) {
            if (SE.getFollowing() != null && !SE.getFollowing().isRemoved()){
                ci.cancel();
            }
        }
        if ($$1 instanceof LivingEntity) {
            ((StandUser) $$1).sba$UniversalTick();
        }

        sba$StoreOldPositionsForTS($$1);
        if ($$1.isRemoved() || $$1.getVehicle() != $$0) {
            $$1.stopRiding();
        } else if ($$1 instanceof Player || this.tickingEntities.contains($$1)) {
            if (((TimeStop) this).CanTimeStopEntity($$1)) {
                $$1.setDeltaMovement(Vec3.ZERO);
                sba$TSTickEntity($$1);
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
    private void sba$TickEntity6(Entity $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof LivingEntity LE) {
            for (FollowingStandEntity SE : ((StandUser)$$1).sba$getFollowers()) {
                this.sba$tickStandIn(LE, SE);
            }
        }
    }
    @Unique
    private ImmutableList<SetBlockInstance> sba$setBlockInstance = ImmutableList.of();
    @Inject(method = "setBlock", at = @At(value = "HEAD"), cancellable = true)
    private void sba$setBlock(BlockPos $$0, BlockState $$1, int $$2, int $$3, CallbackInfoReturnable<Boolean> cir) {
        if (ClientUtil.getScreenFreeze()) {

            if (sba$setBlockInstance == null){
                sba$setBlockInstance = ImmutableList.of();
            }
            if (sba$setBlockInstance.isEmpty()) {
                sba$setBlockInstance = ImmutableList.of(new SetBlockInstance($$0,$$1,$$2,$$3));
            } else {
                List<SetBlockInstance> $$b = Lists.newArrayList(sba$setBlockInstance);
                $$b.add(new SetBlockInstance($$0,$$1,$$2,$$3));
                sba$setBlockInstance = ImmutableList.copyOf($$b);
            }
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "setServerVerifiedBlockState", at = @At(value = "HEAD"), cancellable = true)
    private void sba$setBlock2(BlockPos $$0, BlockState $$1, int $$2, CallbackInfo ci) {
        if (!this.blockStatePredictionHandler.updateKnownServerState($$0, $$1)) {
            if (ClientUtil.getScreenFreeze()) {

                if (sba$setBlockInstance == null) {
                    sba$setBlockInstance = ImmutableList.of();
                }
                if (sba$setBlockInstance.isEmpty()) {
                    sba$setBlockInstance = ImmutableList.of(new SetBlockInstance($$0, $$1, $$2, 512));
                } else {
                    List<SetBlockInstance> $$b = Lists.newArrayList(sba$setBlockInstance);
                    $$b.add(new SetBlockInstance($$0, $$1, $$2, 512));
                    sba$setBlockInstance = ImmutableList.copyOf($$b);
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void sba$tickInGeneral(BooleanSupplier $$0, CallbackInfo ci) {
        ((ILevelAccess)this).sba$tickPlunderBubbleRemoval();
        if (!ClientUtil.getScreenFreeze()) {

            if (sba$setBlockInstance == null){
                sba$setBlockInstance = ImmutableList.of();
            }
            if (!sba$setBlockInstance.isEmpty()){
                for (int i = sba$setBlockInstance.size() - 1; i >= 0; --i) {
                    SetBlockInstance sbi = sba$setBlockInstance.get(i);
                    setBlock(sbi.pos,sbi.state,sbi.integer,sbi.integer2);
                }
                sba$setBlockInstance = ImmutableList.of();
            }
        }

        ((TimeStop)this).tickAllTimeStops();
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void sba$tickInGeneralTail(BooleanSupplier $$0, CallbackInfo ci) {
        ((ILevelAccess)this).sba$tickPlunderBubbleRemoval();
    }

    @Inject(method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V", at = @At(value = "HEAD"), cancellable = true)
    private void sba$playSeed(Player $$0, Entity $$1, Holder<SoundEvent> $$2, SoundSource $$3, float $$4, float $$5, long $$6, CallbackInfo ci) {
        if (ClientUtil.getScreenFreeze()){
            ci.cancel();
            return;
        } if(((ILevelAccess)this).sba$isSoundPlundered($$1.blockPosition())){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).sba$getSoundPlunderedBubble($$1.blockPosition());
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2.value(), $$3, $$4, $$5);
            }
            ci.cancel();
            return;
        } else if(((ILevelAccess)this).sba$isSoundPlunderedEntity($$1)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).sba$getSoundPlunderedBubbleEntity($$1);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$2.value(), $$3, $$4, $$5);
            }
            ci.cancel();
            return;
        }

        if (!PowerTypes.isErasingTime(ClientUtil.getPlayer()) &&
                (PowerTypes.isInADifferentExistence($$1,ClientUtil.getPlayer()) ||
        PowerTypes.isInADifferentExistence($$0,ClientUtil.getPlayer()))
        ){
            ci.cancel();
            return;
        }
    }
    @Inject(method = "playSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZJ)V", at = @At(value = "HEAD"), cancellable = true)
    private void sba$playSeed(double $$0, double $$1, double $$2, SoundEvent $$3, SoundSource $$4, float $$5, float $$6, boolean $$7, long $$8, CallbackInfo ci) {
        BlockPos bpos = new BlockPos((int) $$0, (int) $$1, (int) $$2);
        if (ClientUtil.getScreenFreeze()){
            ci.cancel();
            return;
        } if(((ILevelAccess)this).sba$isSoundPlundered(bpos)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).sba$getSoundPlunderedBubble(bpos);
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$3, $$4, $$5, $$6);
            }
            ci.cancel();
            return;
        }
        if (!PowerTypes.isErasingTime(ClientUtil.getPlayer()) &&
                PowerTypes.isExistentiallyElsewhere(ClientUtil.getPlayer())){
            ci.cancel();
            return;
        }
    }
    @Inject(method = "playLocalSound", at = @At(value = "HEAD"), cancellable = true)
    private void sba$playLocalSound(double $$0, double $$1, double $$2, SoundEvent $$3, SoundSource $$4, float $$5, float $$6, boolean $$7, CallbackInfo ci) {
        BlockPos bpos = new BlockPos((int) $$0, (int) $$1, (int) $$2);
        if (ClientUtil.getScreenFreeze()){
            ci.cancel();
            return;
        } if(((ILevelAccess)this).sba$isSoundPlundered(bpos)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).sba$getSoundPlunderedBubble(bpos);
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
    private void sba$playSound(double $$0, double $$1, double $$2, SoundEvent $$3, SoundSource $$4, float $$5, float $$6, boolean $$7, long $$8, CallbackInfo ci) {
        BlockPos bpos = new BlockPos((int) $$0, (int) $$1, (int) $$2);
        if(((ILevelAccess)this).sba$isSoundPlundered(bpos)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this).sba$getSoundPlunderedBubble(bpos);
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
    private void sba$TickTime(CallbackInfo ci) {
        if (((IDayInterpolationClientLevelData) this.levelData).sba$getSBAInterpolatingDaytime()) {
            this.setGameTime(this.levelData.getGameTime() + 1L);
            if (this.levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
                this.setDayTime(((IDayInterpolationClientLevelData)this.levelData).sba$getSBADayTimeMinecraft() + 1L);
            }
            ci.cancel();
            return;
        }

        if (ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1
                && dimension() == Level.OVERWORLD){
            if (((TimeStop) this).inTimeStopRange(new Vec3i((int) 0, (int) 0, (int) 0))){
                this.setGameTime(this.levelData.getGameTime() + 1L);
                ci.cancel();
                return;
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
    public float sba$getSkyLerp(){
        return sba$skyLerp;
    }
    @Unique
    @Override
    public float sba$getMaxSkyLerp(){
        return sba$maxSkyLerp;
    }

    @Unique
    public float sba$skyLerp = 0;
    @Unique
    public float sba$maxSkyLerp = 30;
    @Unique
    public Vec3 sba$fogSky = new Vec3(
            ConfigManager.getClientConfig().justiceFogBrightness,
            ConfigManager.getClientConfig().justiceFogBrightness,
            ConfigManager.getClientConfig().justiceFogBrightness);

    /*Fog color, fog sky color, fog sky brightness*/
    @Inject(method = "getSkyColor", at = @At(value = "HEAD"), cancellable = true)
    private void sba$getSkyColor(Vec3 cameraPos, float tickDelta, CallbackInfoReturnable<Vec3> cir) {
        if (minecraft.player != null) {

            /**
            if (((IPlayerEntity)minecraft.player).sba$getBlinded()){
                cir.setReturnValue(new Vec3(-40,-40,-40));
                return;
            }
             **/

            if (sba$skyLerp >= 1) {
                float lerp = tickDelta;
                if (!((IPermaCasting) minecraft.player.level()).sba$inPermaCastFogRange(minecraft.player)) {
                    lerp *= -1;
                }
                if (sba$skyLerp >= sba$maxSkyLerp) {
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
                        .lerp(sba$fogSky, ((((sba$skyLerp + lerp) / sba$maxSkyLerp)) / 100) * 0.95);

                cir.setReturnValue(interp);
            }
        }
    }

    @Inject(method = "tickEntities", at = @At(value = "HEAD"), cancellable = true)
    private void sba$TickEntity3(CallbackInfo ci) {


        if (minecraft.player != null){
            if (((IPermaCasting)minecraft.player.level()).sba$inPermaCastFogRange(minecraft.player)){
                SBA.worldInFog = 1;
            } else {
                SBA.worldInFog = 0;
            }

            if (((IPermaCasting)minecraft.player.level()).sba$inPermaCastFogRange(minecraft.player)){
                if (sba$skyLerp < sba$maxSkyLerp){
                    sba$skyLerp++;
                }
            } else {
                if (sba$skyLerp > 0){
                    sba$skyLerp--;
                }
            }

            if (((TimeStop) this).isTimeStoppingEntity(minecraft.player)) {
                ((StandUser) minecraft.player).sba$getStandPowers().timeTickStopPower();
            }
        }
        this.tickingEntities.forEach($$0x -> {
            if ($$0x instanceof FollowingStandEntity standEntity) {
                if (standEntity.getFollowing() != null && !standEntity.getFollowing().isRemoved()){
                    if (!standEntity.getFollowing().isRemoved()) {
                        LivingEntity LE = standEntity.getFollowing();
                        if (!((StandUser) LE).sba$hasFollower(standEntity)) {
                            ((StandUser) LE).sba$addFollower(standEntity);
                        }
                    } else {
                        sba$tickStandIn(null,standEntity);
                    }
                }
            }
        });
    }

    @Inject(method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void sba$StopParticles(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if (((TimeStop) this).inTimeStopRange(new Vec3i((int) x, (int) y, (int) z))){
            ci.cancel();
        }
    }

}
