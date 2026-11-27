package net.hydra.jojomod.mixin;

import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.SilverChariotMountEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.event.SavedSecond;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.*;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.UUID;

@Mixin(value = Entity.class,priority = 100)
public abstract class EntityAndData implements IEntityAndData {

    @Shadow
    private AABB bb;
    @Unique
    private float sba$PrevTick = 0;

    @Unique
    private double sba$PrevX = 0;
    @Unique
    private double sba$PrevY = 0;
    @Unique
    private double sba$PrevZ = 0;

    @Unique
    public int sba$noGravityTicks = 0;
    @Unique
    public boolean sba$renderingExclusiveLayers = false;

    /***
     * Invisiblity functions for Achtung Baby. Note that only Living Entities use tracked/synched entitydata,
     * so regular entities use a function in IEntityAndData instead.
     */
    @Unique
    public int sba$trueInvisibility = -1;
    @Unique
    @Override
    public int sba$getTrueInvisibility(){
        if (((Entity)(Object)this) instanceof LivingEntity LE){
            return ((StandUser)LE).sba$getTrueInvis();
        }
        return sba$trueInvisibility;
    }

    @Unique
    public void sba$tickTrueInvisibility(){
        if (!this.level().isClientSide()){
            if (sba$getTrueInvisibility() > -1){
                sba$setTrueInvisibility(sba$getTrueInvisibility()-1);
            }
        }
    }


    @Unique
    public boolean rdbt$canBePickedUp=true;

    @Unique
    private float sba$lastDirectDamage = 0;

    @Override
    public Vec3 rdbt$getStuckSpeedMultiplier(){
        return stuckSpeedMultiplier;
    }


    @Override
    public void sba$setLastDamageTaken(float amount) {
        this.sba$lastDirectDamage = amount;
    }
    @Override
    public void sba$refreshBoardingCooldown() {
        this.boardingCooldown = 0;
    }

    @Override
    public float sba$getLastDamageTaken() {
        return this.sba$lastDirectDamage;
    }

    @Override
    @Unique
    public void rdbt$forceDeltaMovement(Vec3 $$0) {
        this.deltaMovement = $$0;
        this.hurtMarked = true;
        this.hasImpulse = true;
    }

    @Inject(
            method = "spawnAtLocation(Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At("RETURN")
    )
    private void sba$copyPlaneToDroppedItem(
            ItemStack stack,
            float yOffset,
            CallbackInfoReturnable<ItemEntity> cir
    ) {
        Entity self = (Entity)(Object)this;
        ItemEntity item = cir.getReturnValue();

        if (item != null) {
            PowerTypes.copyPlaneOfExisting(self, item);
        }
    }

    @Unique
    @Override
    public boolean rdbt$returnPickup(){
        return rdbt$canBePickedUp;
    }
    @Unique
    @Override
    public void rdbt$setReturnPickup(boolean ye){
        rdbt$canBePickedUp = ye;
    }

    @Inject(method = "saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",shift = At.Shift.AFTER))
    public void sba$addAdditionalSaveDataX(CompoundTag $$0, CallbackInfoReturnable<CompoundTag> cir){
        MainUtil.saveEntityData($$0,this);
    }

    @Inject(method = "load(Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",shift = At.Shift.AFTER))
    public void sba$readAdditionalSaveDataX(CompoundTag $$0, CallbackInfo ci){
       MainUtil.loadEntityData($$0,this);
    }


    @Unique
    public Entity sba$castEntity(){
        return ((Entity)(Object)this);
    }


    @Unique
    @Override
    public void sba$setTrueInvisibility(int invis){
        if (((Entity)(Object)this) instanceof LivingEntity LE){
            ((StandUser)LE).sba$setTrueInvis(invis);

            if (!this.level().isClientSide()) {
                if (ClientNetworking.getAppropriateConfig().achtungSettings.invisibilityPotionAsWell) {
                    if (invis <= -1) {
                        LE.removeEffect(MobEffects.INVISIBILITY);
                    } else {
                        LE.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 10, 0, false, false), null);
                    }
                }
            }
        } else {
            sba$trueInvisibility = invis;
            if (!this.level().isClientSide()) {
                MainUtil.spreadRadialClientPacket(((Entity) (Object) this), 120, false,
                        ServerToClientPackets.S2CPackets.MESSAGES.TRUE_INVISIBILITY.value,
                        getId(), invis
                );
            }
        }
    }


    @Unique
    @Override
    public int sba$getTrueInvisibilityManhattan(){
        return sba$trueInvisibilityManhattan;
    }

    @Unique
    public Vec3 rdbt$lastPos = Vec3.ZERO;
    @Unique
    public int sba$trueInvisibilityManhattan = -1;
    @Unique
    public void sba$tickTrueInvisibilityManhattan(){
        Vec3 position =  getPosition(1);
        if (rdbt$lastPos != null && rdbt$lastPos.distanceToSqr(position) > 0.001F){
            sba$trueInvisibilityManhattan = 80;
        } else {
            sba$trueInvisibilityManhattan--;
        }
        if (((Entity) (Object) this).isEyeInFluid(FluidTags.WATER) || ((Entity)(Object) this).isEyeInFluid(FluidTags.LAVA)) {
            sba$trueInvisibilityManhattan = 0;
        }
        rdbt$lastPos = position;
    }


    /** Heavens Door and bites the dust related*/

    @Unique
    public Vec3 initialDayPosition = null;


    SavedSecond initialDaySecond = null;;

    @Unique
    @Override
    public void sba$setInitialDaySec() {
        initialDaySecond = SavedSecond.saveEntitySecond((Entity) (Object) this);
    }

    @Unique
    public SavedSecond sba$getInitialDaySec() { return initialDaySecond; }

    /**Mandom Time Queue, not sure if it will have any other use*/
    @Unique
    public ArrayDeque<SavedSecond> sba$secondQue = new ArrayDeque<>();
    @Unique
    public void sba$addSecondToQueue(SavedSecond newSecond) {
        sba$secondQue.addFirst(newSecond);
        if (sba$secondQue.size() > 6) {
            sba$secondQue.removeLast();
        } else {
            sba$secondQue.getLast().hasHadParticle = false;
        }
    }
    @Unique
    public void sba$addSecondToQueue() {
        if (!level.isClientSide()) {
            /**Setting for performance (just in case)*/
            if (ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindOnlySavesAndLoadsOnPlayers &&
                    !(((Entity)(Object)this) instanceof Player))
                return;

            /**Every 20 ticks save a second on the entity for mandom rewinding*/
            if (sba$secondQue.isEmpty() || this.tickCount % 20 == 0) {
                if (!((TimeStop)level()).inTimeStopRange(((Entity) (Object)this))) {
                    sba$addSecondToQueue(SavedSecond.saveEntitySecond((Entity) (Object) this));
                }
            }
        }
    }


    @Unique
    public ArrayDeque<SavedSecond> sba$getSecondQue(){
        return sba$secondQue;
    }

    @Unique
    public SavedSecond sba$getLastSavedSecond(){
        if (!sba$secondQue.isEmpty()){
            return sba$secondQue.getLast();
        }
        return null;
    }


    @Unique
    public void sba$resetSecondQueue(){
        sba$secondQue = new ArrayDeque<>();
    }


    @Unique
    public void sba$setExclusiveLayers(boolean exclusive){
        this.sba$renderingExclusiveLayers = exclusive;
    }
    @Unique
    public boolean sba$getExclusiveLayers(){
        return this.sba$renderingExclusiveLayers;
    }

    @Override
    @Unique
    public void sba$setNoAAB(){
        bb = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
    public void sba$setSBAPrevX(double sbaPrevX){
        this.sba$PrevX = sbaPrevX;
    }
    public void sba$setSBAPrevY(double sbaPrevY){
        this.sba$PrevY = sbaPrevY;
    }
    public void sba$setSBAPrevZ(double sbaPrevZ){
        this.sba$PrevZ = sbaPrevZ;
    }
    public double sba$getSBAPrevX(){
        return this.sba$PrevX;
    }
    public double sba$getSBAPrevY(){
        return this.sba$PrevY;
    }
    public double sba$getSBAPrevZ(){
        return this.sba$PrevZ;
    }
    @Override
    @Unique
    public int sba$getNoGravTicks(){
        return this.sba$noGravityTicks;
    }
    @Override
    @Unique
    public void sba$setNoGravTicks(int ticks){
        this.sba$noGravityTicks = ticks;
    }

    @Override
    @Unique
    public Entity sba$getVehicle(){
        return this.vehicle;
    }
    @Override
    @Unique
    public void sba$setVehicle(Entity ride){
        this.vehicle = ride;
    }
    @Shadow
    private int remainingFireTicks;

    @Shadow
    @Final
    public double getX() {
        return 0;
    }

    @Shadow
    @Final
    public double getY() {
        return 0;
    }
    @Shadow
    @Final
    public double getZ() {
        return 0;
    }

    @Unique
    public boolean sba$shadow = true;
    @Unique
    @Override
    public boolean sba$getShadow(){
        return sba$shadow;
    }
    @Unique
    @Override
    public void sba$setShadow(boolean shadow){
        sba$shadow = shadow;
    }

    /**Highlight color with Justice piloting*/
    /**See inputevents for allowing the mob to glow*/
    @Inject(method = "getTeamColor()I", at = @At("HEAD"), cancellable = true)
    public void sba$getTeamColor(CallbackInfoReturnable<Integer> cir){
        if (this.level.isClientSide()){
            int raga = ClientUtil.getOutlineColor(((Entity)(Object)this));
            if (raga != -1){
                cir.setReturnValue(raga);
            }
        }

    }
    @Inject(method = "canCollideWith", at = @At("HEAD"), cancellable = true)
    public void sba$isInvisible(Entity ent, CallbackInfoReturnable<Boolean> cir){
        if (PowerTypes.isInADifferentExistence(((Entity)(Object) this),ent)){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "lavaHurt", at = @At("HEAD"), cancellable = true)
    public void sba$lavaHurt(CallbackInfo ci) {
        if (PowerTypes.isErasingTime(((Entity)(Object) this))){
            ci.cancel();
        }
    }
    public boolean rdbt$getSharedFlag(int flag){
        return getSharedFlag(flag);
    }
        @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    public void sba$isInvisible(CallbackInfoReturnable<Boolean> cir){
        Entity ent = (Entity) (Object) this;
        if (PowerTypes.isExistentiallyElsewhere(ent)){
            if ((this.level().isClientSide() || PowerTypes.isErasingTime(ent)) && !(this.level().isClientSide() &&
                    !PowerTypes.isInADifferentExistence(ent,ClientUtil.getPlayer()))){
                if (!(this.level().isClientSide() && (ClientUtil.isPlayer(ent)
                || (ent instanceof KingCrimsonCloneEntity kcc && ClientUtil.isPlayer(kcc.getPlayer()) &&
                        ConfigManager.getClientConfig().generalSettings.canSeeFatedSelf)
                        ||
                        (ent instanceof StandEntity se &&
                                se.getUser() instanceof KingCrimsonCloneEntity kcc2 && ClientUtil.isPlayer(kcc2.getPlayer()) &&
                        ConfigManager.getClientConfig().generalSettings.canSeeFatedSelf)
                        ))) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }

        if (sba$getTrueInvisibility() > -1 && !(level().isClientSide() && ClientUtil.checkIfClientCanSeeMobsForWindVision())){
            if (this.level().isClientSide()){
                if (ClientUtil.isPlayer((Entity)(Object)this)){
                    if (ClientUtil.getFirstPerson()){
                        cir.setReturnValue(false);
                        return;
                    }
                }
            }
            cir.setReturnValue(true);
            return;
        } else if (((Entity)(Object)this) instanceof Player pl && ((IPowersPlayer)pl).rdbt$getPowers().isFaded()){
            if (!this.level().isClientSide()){
                cir.setReturnValue(true);
                return;
            }
        }

        if (((Entity)(Object)this) instanceof Player pl){
            if (!this.level().isClientSide()){
                byte posX = ((IPlayerEntity)pl).sba$GetPos2();
                if (posX == PlayerPosIndex.VANISH_PERSIST || posX == PlayerPosIndex.VANISH_START) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }


        if (((Entity)(Object)this) instanceof LivingEntity LE
                && ((StandUser)LE).sba$getMetallicaInvisibility() > -1) {
            if (this.level().isClientSide()){

                if (ClientUtil.isPlayer((Entity)(Object)this)){

                    if (ClientUtil.getFirstPerson()){
                        cir.setReturnValue(false);
                        return;
                    }
                }
                double dist = ClientUtil.getCameradDistance((Entity)(Object)this);
                float alpha = PowersMetallica.getMetallicaInvisibilityAlpha((LivingEntity)(Object)this, dist);

                if (alpha > 0.05f) {
                    cir.setReturnValue(false);
                    return;
                }
            }
            cir.setReturnValue(true);
        }

        if ((level().isClientSide() && ClientUtil.checkIfClientCanSeeMobsForWindVision())){
            if(sba$getTrueInvisibilityManhattan() > 0){
                if (this.level().isClientSide()) {
                    cir.setReturnValue(false);
                    return;
                }
            }
            else{
               cir.setReturnValue(true);
                return;
            }
            cir.setReturnValue(true);
        }
    }


    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    public void sba$isInvisibleTo(Player pl, CallbackInfoReturnable<Boolean> cir){
        if (sba$getTrueInvisibility() > -1){
            if (pl != null && ((StandUser)pl).sba$getStandPowers() instanceof PowersAchtungBaby PA
                    && PA.invisibleVisionOn()){
                cir.setReturnValue(false);
            }
            return;
        }
        if (((Entity)(Object)this) instanceof LivingEntity LE
                && ((StandUser)LE).sba$getMetallicaInvisibility() > -1) {
            return;
        }
    }


    @Inject(method = "moveRelative", at = @At("HEAD"), cancellable = true)
    private void sba$moveRelative(float $$0, Vec3 $$1, CallbackInfo ci){

        if(((ILevelAccess)this.level()).sba$isFrictionPlundered(this.blockPosition()) ||
                ((ILevelAccess)this.level()).sba$isFrictionPlunderedEntity(((Entity)(Object)this))
        ){
            if (this.onGround() && ((Entity)(Object)this) instanceof LivingEntity LE) {
                if (!((StandUser)LE).sba$frictionSave().equals(Vec3.ZERO)) {
                    ci.cancel();
                }
            }
        }
    }
    @Inject(method = "walkingStepSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("HEAD"), cancellable = true)
    public void sba$walkingStepSound(BlockPos $$0, BlockState $$1, CallbackInfo ci){
        if(((ILevelAccess)this.level()).sba$isFrictionPlundered($$0) ||
                ((ILevelAccess)this.level()).sba$isFrictionPlunderedEntity(((Entity)(Object)this))){
            ci.cancel();
        } else if (((Entity)(Object)this) instanceof LivingEntity LE &&
                ((StandUser)LE).sba$getStandPowers() instanceof PowersWhiteAlbum PW
                && PW.hasSkatesActivated()){
            ci.cancel();
        }
    }

    @Inject(method = "getNameTagOffsetY", at = @At("HEAD"), cancellable = true)
    public void sba$getNameTagOffsetY(CallbackInfoReturnable<Float> cir){
        if (((Entity)(Object)this) instanceof Player PE){
            ItemStack stack = ((IPlayerEntity) PE).sba$getMaskSlot();
            if (stack !=null && !stack.isEmpty() && stack.getItem() instanceof MaskItem ME){

                VisageData vd = ME.visageData.generateVisageData(PE);
                cir.setReturnValue(this.getBbHeight() + vd.getNametagHeight());
            }
        }
    }
    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void sba$Turn(double $$0, double $$1, CallbackInfo ci){
        Entity thisEnt = ((Entity) (Object) this);
        if (((TimeStop) thisEnt.level()).CanTimeStopEntity(((Entity) (Object) this))){
            ci.cancel();
        } else if (thisEnt instanceof Player PE){
            StandUser sus = ((StandUser)PE);
            StandPowers powers = sus.sba$getStandPowers();
            if (powers.isPiloting()){
                Entity pilot = powers.getPilotingStand();
                if (pilot != null){
                    pilot.turn($$0,$$1);
                    ci.cancel();
                }
            }
        }
    }


    @Shadow
    public Level level() {
        return null;
    }
    @Shadow
    @Final
    public boolean isRemoved() {
        return false;
    }
    @Inject(method = "changeDimension", at = @At(value = "HEAD"), cancellable = true)
    private void sba$changeDim(ServerLevel $$0, CallbackInfoReturnable<Boolean> ci) {
        if (((Entity)(Object)this) instanceof LivingEntity LE){
            if (this.level() instanceof ServerLevel && !this.isRemoved()) {
                if (((StandUser)this).sba$getStand() != null){
                    StandEntity stand = ((StandUser)this).sba$getStand();
                    if (!stand.getHeldItem().isEmpty()) {

                        if (stand.canAcquireHeldItem) {
                            double $$3 = stand.getEyeY() - 0.3F;
                            ItemEntity $$4 = new ItemEntity(this.level(), stand.getX(), $$3, stand.getZ(), stand.getHeldItem().copy());
                            $$4.setPickUpDelay(40);
                            $$4.setThrower(stand.getUUID());
                            stand.level().addFreshEntity($$4);
                            stand.setHeldItem(ItemStack.EMPTY);
                        }
                        if (!stand.getPassengers().isEmpty()){
                            stand.ejectPassengers();
                        }
                    }
                    if(stand instanceof ManhattanTransferEntity ME){
                        if(!ME.getHeldItemManhattan().isEmpty()){
                            ME.shootHattan();
                            ME.hasItem = false;
                            ME.setHeldItemManhattan(ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }
    @Inject(method = "restoreFrom", at = @At(value = "TAIL"), cancellable = true)
    private void sba$restoreFrom (Entity $$0, CallbackInfo ci){
        if (((Entity) (Object) this) instanceof LivingEntity LE && $$0 instanceof LivingEntity LE2) {
        }
    }
    @Override
    public float sba$getPreTSTick() {
        return this.sba$PrevTick;
    }

    @Override
    public void sba$setPreTSTick(float frameTime) {
        sba$PrevTick = frameTime;
    }
    @Override
    public void sba$resetPreTSTick() {
        sba$PrevTick = 0;
    }

    @Override
    public float sba$getStepHeight() {
        return this.maxUpStep;
    }

    public  AABB rdbt$getPoseBox(Pose pose){
        return getBoundingBoxForPose(pose);
    }
    /**In a timestop, fire doesn't tick*/
    @Inject(method = "setRemainingFireTicks", at = @At("HEAD"), cancellable = true)
    protected void sba$SetFireTicks(int $$0, CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            ci.cancel();
        }
    }
    @Inject(method = "clearFire", at = @At("HEAD"), cancellable = true)
    protected void sba$ClearFire(CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            this.remainingFireTicks = 0;
        }
    }
    @Inject(method = "isControlledByLocalInstance", at = @At("HEAD"), cancellable = true)
    protected void sba$IsControlledByLocalInstance(CallbackInfoReturnable<Boolean> cir){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity LE && ((TimeStop)entity.level()).CanTimeStopEntity(entity)){
            cir.setReturnValue(this.isEffectiveAi());
        }
    }

    @Shadow
    public boolean isShiftKeyDown() {
        return false;
    }
    @Shadow
    public abstract void setShiftKeyDown(boolean bl);
    @Inject(method = "canRide", at = @At("HEAD"), cancellable = true)
    protected void sba$canRide(Entity $$0, CallbackInfoReturnable<Boolean> cir){
        if ($$0 instanceof StandEntity && (!(((Entity)(Object)this) instanceof LivingEntity) || ((TimeStop) ((Entity) (Object) this).level()).CanTimeStopEntity(((Entity) (Object) this)))){
            cir.setReturnValue(!this.isShiftKeyDown());
        }
    }
    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z", at = @At("HEAD"), cancellable = true)
    protected void sba$startRiding(Entity $$0, boolean $$1, CallbackInfoReturnable<Boolean> cir){
        if (
                ((Entity)(Object)this) instanceof LivingEntity LE
                        && ((StandUser)LE).sba$getStand() != null
                        && ((StandUser)LE).sba$getStand() instanceof SilverChariotEntity silverChariotEntity
                        && silverChariotEntity.isSelfCarryModeActive()
                        && $$0 instanceof SilverChariotMountEntity scme
        ) {
            // cir.setReturnValue(true);
        } else if (((Entity)(Object)this) instanceof LivingEntity LE
                && ((StandUser)LE).sba$getStand() != null
                && ($$0.getRootVehicle().is(((StandUser)LE).sba$getStand()) || $$0.getRootVehicle().hasPassenger(((StandUser)LE).sba$getStand()))
                // && !($$0 instanceof SilverChariotMountEntity sc)
        ){
            cir.setReturnValue(false);
        }
    }


    @SuppressWarnings("deprecation")
    @Inject(method = "spawnSprintParticle()V", at = @At("HEAD"), cancellable = true)
    protected void sba$spawnSprintParticle(CallbackInfo ci){
        Entity thirs = ((Entity)(Object)this);
        if (PowerTypes.isExistentiallyElsewhere(thirs) &&
                !(level().isClientSide() && !PowerTypes.isErasingTime(thirs) &&
                        !PowerTypes.isInADifferentExistence(thirs, ClientUtil.getPlayer()))){
            ci.cancel();
            return;
        }
        if (thirs instanceof LivingEntity LE) {
            if (((StandUser) this).sba$getStandPowers().cancelSprintParticles()) {
                ci.cancel();
                return;
            }
            if(((StandUser)this).sba$getGrabberSabbath() != 0){
                ci.cancel();
                return;
            }
            if (thirs instanceof Player pl) {
                if (((IPowersPlayer) pl).rdbt$getPowers().cancelSprintParticles()) {
                    ci.cancel();
                    return;
                }
            }
        }
        BlockPos $$0 = getOnPosLegacy();
        BlockState $$1 = this.level().getBlockState($$0);
        if ($$1.getBlock() instanceof FogBlock){
            ci.cancel();
        }
    }
    @Inject(method = "playSwimSound", at = @At("HEAD"), cancellable = true, require = 0)
    private void rdbt$noSwimSound(float volume, CallbackInfo ci) {
        Entity thirs = ((Entity)(Object)this);
        if (PowerTypes.isErasingTime(thirs)){
            ci.cancel();
        }
    }
    @Inject(method = "isSteppingCarefully", at = @At("HEAD"), cancellable = true, require = 0)
    private void rdbt$isSteppingCarefully(CallbackInfoReturnable<Boolean> cir) {
        if (PowerTypes.isExistentiallyElsewhere((Entity)(Object)this) &&
                !PowerTypes.canInteractInExistence((Entity)(Object)this)) {
            cir.setReturnValue(true);
        }
    }
    @Inject(method = "waterSwimSound", at = @At("HEAD"), cancellable = true, require = 0)
    private void rdbt$waterSwimSound(CallbackInfo ci) {
        Entity thirs = ((Entity)(Object)this);
        if (PowerTypes.isErasingTime(thirs)){
            ci.cancel();
        }
    }
    @Inject(method = "doWaterSplashEffect", at = @At(value = "HEAD"), cancellable = true, require = 0)
    protected void sba$doWaterSplashEffect(CallbackInfo ci) {
        Entity thirs = ((Entity)(Object)this);
        if (PowerTypes.isExistentiallyElsewhere(thirs) &&
                !(level().isClientSide() && !PowerTypes.isErasingTime(thirs) &&
                        !PowerTypes.isInADifferentExistence(thirs, ClientUtil.getPlayer()))){
            ci.cancel();
        }
    }
    @Inject(method = "checkFallDamage", at = @At(value = "HEAD"), cancellable = true, require = 0)
    protected void sba$checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3, CallbackInfo ci) {
        Entity thisEnt = ((Entity) (Object) this);
        if (PowerTypes.isExistentiallyElsewhere(thisEnt)){
            if (!PowerTypes.isErasingTime(thisEnt)) {
                    if ($$1) {
                        if (this.fallDistance > 0.0F) {
                            $$2.getBlock().fallOn(this.level(), $$2, $$3, thisEnt, this.fallDistance);
                            if (PowerTypes.canInteractInExistence((Entity) (Object) this)) {
                                this.level()
                                        .gameEvent(
                                                GameEvent.HIT_GROUND,
                                                this.position,
                                                GameEvent.Context.of(thisEnt, this.mainSupportingBlockPos.<BlockState>map($$0x -> this.level().getBlockState($$0x)).orElse($$2))
                                        );
                            }
                        }

                        this.resetFallDistance();
                    } else if ($$0 < 0.0) {
                        this.fallDistance -= (float) $$0;
                    }
            }
            ci.cancel();
        }
    }

        @Inject(method = "isIgnoringBlockTriggers", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$isIgnoringBlockTriggers(CallbackInfoReturnable<Boolean> cir) {
        Entity thisEnt = ((Entity) (Object) this);
        if (PowerTypes.isExistentiallyElsewhere(thisEnt)){
            if (!PowerTypes.canInteractInExistence((Entity) (Object) this)) {
                cir.setReturnValue(true);
            }
        }
    }
    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"),cancellable = true)
    protected void sba$push(Entity entity, CallbackInfo ci) {
        if (PowerTypes.isInADifferentExistence(entity,(((Entity) (Object)this)))){
            ci.cancel();
            return;
        }
        Entity thisEnt = ((Entity) (Object) this);
        if (thisEnt instanceof LivingEntity lv){
            if (((StandUser)lv).sba$getStandPowers().onCollide(entity)){
                ci.cancel();
                return;
            }
        } if (entity instanceof LivingEntity lv){
            if (((StandUser)lv).sba$getStandPowers().onCollide(entity)){
                ci.cancel();
                return;
            }
        }
        if (entity instanceof LivingEntity le && ((StandUser) le).sba$getStandPowers().cancelCollision(((Entity)(Object)this))) {
            ci.cancel();
        }
    }

    @Shadow
    private Vec3 deltaMovement;

    @Shadow public abstract void moveTo(BlockPos $$0, float $$1, float $$2);

    @Shadow public abstract void moveTo(double $$0, double $$1, double $$2);

    @Shadow public abstract Vec3 getPosition(float $$0);

    @Shadow private Level level;
    @Unique
    private Vec3 sba$DeltaBuildupTS = new Vec3(0,0,0);

    @Unique
    public Vec3 sba$getSBADeltaBuildupTS(){
        return this.sba$DeltaBuildupTS;
    }

    @Unique
    public void sba$setSBADeltaBuildupTS(Vec3 vec3){
        if (vec3 != null) {
            this.sba$DeltaBuildupTS = vec3;
        }
    }
    @Inject(method = "setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
    protected void sba$SetDeltaMovement(Vec3 vec3, CallbackInfo ci){
        if (((TimeStop) ((Entity) (Object) this).level()).CanTimeStopEntity(((Entity) (Object) this))){
            if (vec3.distanceTo(new Vec3(0,0,0)) > (sba$DeltaBuildupTS.distanceTo(new Vec3(0,0,0)) - 0.35)) {
                this.sba$DeltaBuildupTS = vec3;
            }
            ci.cancel();
        }
    }

    @Unique
    public boolean sba$jamBreath = false;
    @Unique
    public void sba$setSBAJamBreath(boolean sbaJamBreath){
        this.sba$jamBreath = sbaJamBreath;
    }
    @Unique
    public boolean sba$getSBAJamBreath(){
        return this.sba$jamBreath;
    }

    @Inject(method = "setAirSupply", at = @At("HEAD"), cancellable = true)
    public void sba$SetAirSupply(int $$0, CallbackInfo ci) {
        if (sba$jamBreath){
            ci.cancel();
        }
    }



    @Shadow
    @Final
    public final float getEyeHeight() {
        return 0;
    }
    @Shadow
    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    @Shadow @javax.annotation.Nullable private Entity vehicle;

    @Shadow @Deprecated public abstract BlockPos getOnPosLegacy();

    @Shadow public abstract float getBbHeight();

    @Shadow public abstract boolean isEffectiveAi();

    @Shadow public abstract boolean onGround();

    @Shadow public abstract BlockPos blockPosition();

    @Shadow public int tickCount;


    @Shadow public abstract int getId();

    @Shadow @Final protected SynchedEntityData entityData;

    @Shadow public abstract SynchedEntityData getEntityData();

    @Shadow public abstract boolean isInLava();

    @Shadow private float maxUpStep;

    @Shadow
    public boolean hurtMarked;

    @Shadow
    public boolean hasImpulse;

    @Shadow
    protected abstract boolean getSharedFlag(int i);

    @Shadow
    public abstract void resetFallDistance();

    @Shadow
    public float fallDistance;

    @Shadow
    private Vec3 position;

    @Shadow
    public Optional<BlockPos> mainSupportingBlockPos;

    @Shadow
    protected abstract AABB getBoundingBoxForPose(Pose pose);

    @Shadow
    public abstract Vec3 position();

    @Shadow
    public abstract void discard();

    @Shadow
    protected int boardingCooldown;
    @Shadow
    protected Vec3 stuckSpeedMultiplier;
    @Unique
    private int rdbt$inForeignWorld = 0;
    @Unique
    private int rdbt$ticksUntilGone = 0;
    @Unique
    private byte rdbt$nativeTo = 0;
    @Unique
    private byte rdbt$originWorld = 0;

    @Override
    @Unique
    public int rdbt$getForeignWorldTicks(){
        return rdbt$inForeignWorld;
    }
    @Override
    @Unique
    public int rdbt$getTicksUntilGone(){
        return rdbt$ticksUntilGone;
    }
    @Override
    @Unique
    public void rdbt$setTicksUntilGone(int lol){
        rdbt$ticksUntilGone = lol;
    }
    @Override
    @Unique
    public byte rdbt$getNativeTo(){
        return rdbt$nativeTo;
    }
    @Override
    @Unique
    public void rdbt$setNativeTo(byte lol){
        rdbt$nativeTo = lol;
    }
    @Override
    @Unique
    public byte rdbt$getOriginWorld(){
        return rdbt$originWorld;
    }
    @Override
    @Unique
    public void rdbt$setOriginWorld(byte lol){
        rdbt$originWorld = lol;
    }

    @Unique
    private int rdbt$altCheckCooldown = 0;
    @Unique
    private int rdbt$delayTime = 0;

    @Unique
    public int rdbt$getAltCheckCooldown() {
        return rdbt$altCheckCooldown;
    }
    @Unique
    private Entity rdbt$nearAlt = null;

    @Unique
    @Override
    public Entity rdbt$getNearAlt(){
        return rdbt$nearAlt;
    }

    @Unique
    public void rdbt$setAltCheckCooldown(int ticks) {
        rdbt$altCheckCooldown = ticks;
    }
    @Unique
    public UUID rdbt$nativeCopy = null;

    @Unique
    public UUID rdbt$getNativeCopy(){
        return rdbt$nativeCopy;
    }
    @Unique
    public void rdbt$setNativeCopy(UUID uuidd){
        rdbt$nativeCopy = uuidd;
    }
    @Override
    @Unique
    public void rdbt$setForeignWorldTicks(int lol){
        rdbt$inForeignWorld = lol;
        if (((Entity)(Object)this) instanceof ServerPlayer player){
            S2CPacketUtil.sendGenericIntToClientPacket(player, PacketDataIndex.S2C_MERGE_TIME_INT,lol);
        }
    }
    private void rdbt$tickAltProximity() {
        if (!(level() instanceof ServerLevel sl)) {
            return;
        }

        Entity self = (Entity) (Object) this;

        // Only entities originating from another world
        if (PowerTypes.originatedFromOurWorld(self)) {
            rdbt$nearAlt = null;
            return;
        }

        // ------------------------------------------------
        // ALREADY TRACKING A PARALLEL ENTITY
        // ------------------------------------------------

        if (rdbt$nearAlt != null) {
            rdbt$delayTime++;
            Entity alt = rdbt$nearAlt;

            // Cheap checks only.
            // No entity hitbox query.
            if (!alt.isAlive()
                    || alt.level() != self.level()
                    || !MainUtil.canActuallyHitInvolved2(self,alt)
                    || PowerTypes.isInADifferentExistenceNoTE(self,alt)
                    || (alt instanceof LivingEntity lv && ((StandUser)lv).sba$getStandPowers() instanceof PowersD4C && !PowersD4C.debugCollision)
                    || self.distanceTo(alt) >= 9) {

                rdbt$nearAlt = null;

                // Don't immediately perform another expensive search.
                rdbt$altCheckCooldown = 40;
                return;
            }

            // They're still close enough.
                PowerTypes.tickIsNearAlt(self, alt,rdbt$delayTime);
            if (self.isAlive() && alt.isAlive()) {
                PowerTypes.tickIsNearAlt(alt, self,rdbt$delayTime);
            }
            return;
        }
        rdbt$delayTime = 0;
        // ------------------------------------------------
        // SEARCHING FOR A TARGET
        // ------------------------------------------------

        if (rdbt$altCheckCooldown > 0) {
            rdbt$altCheckCooldown--;
            return;
        }

        // 40-100 ticks = 2-5 seconds
        rdbt$altCheckCooldown =
                20 + (int)(Math.random() * 15);

        Entity alt = PowerTypes.findNearbyParallelCopy(self);

        if (alt != null && !PowerTypes.isInADifferentExistenceNoTE(self,alt)) {
            rdbt$nearAlt = alt;

            MainUtil.playSoundIfPossible(self,self.level(),null, self.blockPosition(),
                    ModSounds.D4C_FUSION_START_EVENT, SoundSource.PLAYERS, 3.0F, 1F+((float)(Math.random()*0.05F)));

            // Immediately run the effect rather than
            // waiting until the next tick.
            PowerTypes.tickIsNearAlt(self, alt,rdbt$delayTime);
            if (self.isAlive() && alt.isAlive()) {
                PowerTypes.tickIsNearAlt(alt, self,rdbt$delayTime);
            }
        }
    }


    @Override
    @Unique
    public void sba$universalTick(){
        sba$addSecondToQueue();
        sba$tickTrueInvisibility();
        sba$tickTrueInvisibilityManhattan();

        rdbt$tickAltProximity();

        if (!level().isClientSide() && !isRemoved()) {
            Entity thrs = ((Entity) (Object) this);
            byte world = PowerTypes.getPlaneOfExisting(thrs);
                if (!(thrs instanceof FollowingStandEntity fs && fs.getFollowing() != null)
                        && !(thrs instanceof StandEntity se && se.getUser() != null)) {
                    if (rdbt$nativeTo != 0 && rdbt$nativeTo == world){
                        rdbt$ticksUntilGone--;
                        if (rdbt$ticksUntilGone <= 0){
                            discard();
                        }
                    } else {
                        if (rdbt$nativeTo != 0) {
                            rdbt$nativeTo = 0;
                        }
                        if (world != 0) {
                            int existTime = PowerTypes.getForeignWorldMaxTime(world);
                            if (existTime != -1) {
                                int clmp = Mth.clamp(rdbt$inForeignWorld +
                                                PowersD4C.getDeductionTicks(thrs, position().distanceTo(
                                                        ((IGravityEntity) thrs).rdbt$getExistPlaneStartPoint())
                                                ),
                                        0, existTime);
                                rdbt$setForeignWorldTicks(clmp);
                                if (rdbt$inForeignWorld >= existTime) {
                                    if (PowerTypes.isInD4CWorld(thrs)) {
                                        if (!PowersD4C.ejectFromNearestEntity(thrs)) {
                                            if (!PowersD4C.ejectFromOGSpot(thrs)) {
                                            }
                                        }
                                    }
                                    PowerTypes.setPlaneOfExisting(thrs, (byte) 0);
                                }
                            }
                        } else {
                            if (rdbt$inForeignWorld != 0) {
                                rdbt$setForeignWorldTicks(0);
                            }
                        }
                    }
            }
        }
    }



    @Unique
    private int lastDay = -1;

    @Unique
    @Override
    public void sba$initialDayCheck(int day) {
        Entity self = (Entity)(Object)this;
        if (lastDay != day && SavedSecond.canTeleportTo(self.level(), self.getPosition(1), self)) {
            sba$setInitialDaySec();
            lastDay = day;
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void sba$tickH(CallbackInfo ci) {
        sba$universalTick();
    }
    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void sba$tick(CallbackInfo ci) {
        sba$tickQVec();
    }
    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$playSoundEnt(SoundEvent soundEvent, float f, float g,CallbackInfo ci) {
        Entity thrs = ((Entity) (Object)this);
        if(((ILevelAccess)this.level()).sba$isSoundPlunderedEntity(thrs)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this.level()).sba$getSoundPlunderedBubbleEntity(((Entity) (Object)this));
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds(soundEvent, this.getSoundSource(), f, g);
            }
            ci.cancel();
            return;
        }
        if (level().isClientSide()){
            if (PowerTypes.isInADifferentExistenceNoTE(thrs,ClientUtil.getPlayer())){
                ci.cancel();
                return;
            } else if (PowerTypes.isExistentiallyElsewhere(thrs)){
                ClientUtil.playSoundWithInfo(thrs.level(),
                        thrs.getX(),
                        thrs.getY(),
                        thrs.getZ(),
                        soundEvent,
                        this.getSoundSource(),
                        f,
                        g);
            }
        } else {
            if (PowerTypes.isExistentiallyElsewhere(thrs)){
                if (thrs.level() instanceof ServerLevel sl) {
                    if (soundEvent != null) {
                        ResourceLocation soundId = soundEvent.getLocation();
                        if (soundId != null) {
                            String str = this.getSoundSource().name();
                            if (str != null) {
                                for (ServerPlayer playerInList :
                                        sl.getServer().getPlayerList().getPlayers()) {

                                    double range = soundEvent.getRange(g);
                                    double rangeSqr = range * range;
                                    if (playerInList.distanceToSqr(thrs) > rangeSqr) {
                                        continue;
                                    }

                                    if (PowerTypes.isInADifferentExistenceNoTE(
                                            thrs,
                                            playerInList)) {
                                        continue;
                                    }

                                    S2CPacketUtil.sendSafeSound(
                                            playerInList,
                                            thrs.getX(),
                                            thrs.getY(),
                                            thrs.getZ(),
                                            soundId.toString(),
                                            str,
                                            f,
                                            g
                                    );
                                }
                            }
                        }
                    }
                }
                ci.cancel();
                return;
            }
        }
    }



    @Inject(method = "thunderHit", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$thunderHit(CallbackInfo ci) {
        if (((Entity)(Object)this) instanceof Player PE){
            StandUser user = ((StandUser)PE);
            ItemStack stack = user.sba$getStandDisc();
            if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_THE_WORLD)){
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                if (!ipe.sba$getUnlockedBonusSkin()){
                    ci.cancel();
                    if (!level.isClientSide()) {
                        ipe.sba$setUnlockedBonusSkin(true);
                        level().playSound(null, getX(), getY(),
                                getZ(), ModSounds.UNLOCK_SKIN_EVENT, this.getSoundSource(), 2.0F, 1.0F);
                        ((ServerLevel) this.level()).sendParticles(ParticleTypes.END_ROD, this.getX(),
                                this.getY()+this.getEyeHeight(), this.getZ(),
                                10, 0.5, 0.5, 0.5, 0.2);
                        user.sba$setStandSkin(TheWorldEntity.OVER_HEAVEN);
                        ((ServerPlayer) ipe).displayClientMessage(
                                Component.translatable("unlock_skin.sba.the_world.over_heaven"), true);
                        user.sba$summonStand(level, true, false);
                    }
                }
            }
        }
    }
    @Unique
    @Override
    public void sba$tickQVec(){
        if (!this.level.isClientSide()) {
            Vec3 vec = new Vec3(sba$qknockback2params.x, sba$qknockback2params.y, sba$qknockback2params.z);
            if (!vec.equals(Vec3.ZERO) && vec.distanceTo(Vec3.ZERO) > 5) {
                if (vec.distanceTo(this.getPosition(1)) < 50) {
                    if (((Entity) (Object) this) instanceof LivingEntity le) {
                        le.teleportTo(vec.x, vec.y, vec.z);
                    } else {
                        this.moveTo(vec.x, vec.y, vec.z);
                    }
                    sba$qknockback2params = Vec3.ZERO;
                }
            }
            Vec3 vecx = new Vec3(sba$qknockback.x, sba$qknockback.y, sba$qknockback.z);
            if (!vecx.equals(Vec3.ZERO)) {
                MainUtil.takeUnresistableKnockbackWithYBias2(((Entity) (Object) this), sba$qknockbackparams.x,
                        vecx.x,
                        vecx.y,
                        vecx.z,
                        (float) sba$qknockbackparams.y,
                        (float) sba$qknockbackparams.z
                        );
                sba$setQVec(Vec3.ZERO);
            }
        }
    }
    @Unique
    private Vec3 sba$qknockback = Vec3.ZERO;
    @Unique
    private Vec3 sba$qknockbackparams = Vec3.ZERO;
    @Unique
    private Vec3 sba$qknockback2params = Vec3.ZERO;
    @Unique
    @Override
    public void sba$setQVec(Vec3 ec){
        sba$qknockback = ec;
    }
    @Unique
    @Override
    public void sba$setQVecParams(Vec3 ec){
        sba$qknockbackparams = ec;
    }
    @Unique
    @Override
    public void sba$setQVec2Params(Vec3 ec){
        sba$qknockback2params = ec;
    }
    @Unique
    @Override
    public void sba$setDeltaMovementRaw(Vec3 ec){
        this.deltaMovement = ec;
    }


}