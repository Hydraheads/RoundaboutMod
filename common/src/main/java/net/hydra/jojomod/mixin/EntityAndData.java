package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.SavedSecond;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersAchtungBaby;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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

@Mixin(value = Entity.class,priority = 100)
public abstract class EntityAndData implements IEntityAndData {

    @Shadow
    private AABB bb;
    @Unique
    private float roundabout$PrevTick = 0;

    @Unique
    private double roundabout$PrevX = 0;
    @Unique
    private double roundabout$PrevY = 0;
    @Unique
    private double roundabout$PrevZ = 0;

    @Unique
    public int roundabout$noGravityTicks = 0;
    @Unique
    public boolean roundabout$renderingExclusiveLayers = false;

    /***
     * Invisiblity functions for Achtung Baby. Note that only Living Entities use tracked/synched entitydata,
     * so regular entities use a function in IEntityAndData instead.
     */
    @Unique
    public int roundabout$trueInvisibility = -1;
    @Unique
    @Override
    public int roundabout$getTrueInvisibility(){
        if (((Entity)(Object)this) instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getTrueInvis();
        }
        return roundabout$trueInvisibility;
    }

    @Unique
    public void roundabout$tickTrueInvisibility(){
        if (!this.level().isClientSide()){
            if (roundabout$getTrueInvisibility() > -1){
                roundabout$setTrueInvisibility(roundabout$getTrueInvisibility()-1);
            }
        }
    }


    @Unique
    public boolean rdbt$canBePickedUp=true;

    @Unique
    @Override
    public boolean rdbt$returnPickup(){
        return rdbt$canBePickedUp;
    }

    @Inject(method = "saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",shift = At.Shift.AFTER))
    public void roundabout$addAdditionalSaveDataX(CompoundTag $$0, CallbackInfoReturnable<CompoundTag> cir){
        $$0.putBoolean("canMobGrab",rdbt$canBePickedUp);
    }

    @Inject(method = "load(Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",shift = At.Shift.AFTER))
    public void roundabout$readAdditionalSaveDataX(CompoundTag $$0, CallbackInfo ci){
        if ($$0.contains("canMobGrab")) {
            rdbt$canBePickedUp = $$0.getBoolean("canMobGrab");
        }

    }


    @Unique
    public Entity roundabout$castEntity(){
        return ((Entity)(Object)this);
    }


    @Unique
    @Override
    public void roundabout$setTrueInvisibility(int invis){
        if (((Entity)(Object)this) instanceof LivingEntity LE){
            ((StandUser)LE).roundabout$setTrueInvis(invis);

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
            roundabout$trueInvisibility = invis;
            if (!this.level().isClientSide()) {
                MainUtil.spreadRadialClientPacket(((Entity) (Object) this), 120, false,
                        ServerToClientPackets.S2CPackets.MESSAGES.TRUE_INVISIBILITY.value,
                        getId(), invis
                );
            }
        }
    }


    /**Mandom Time Queue, not sure if it will have any other use*/
    @Unique
    public ArrayDeque<SavedSecond> roundabout$secondQue = new ArrayDeque<>();
    @Unique
    public void roundabout$addSecondToQueue(SavedSecond newSecond) {
        roundabout$secondQue.addFirst(newSecond);
        if (roundabout$secondQue.size() > 6) {
            roundabout$secondQue.removeLast();
        } else {
            roundabout$secondQue.getLast().hasHadParticle = false;
        }
    }
    @Unique
    public void roundabout$addSecondToQueue() {
        if (!level.isClientSide()) {
            /**Setting for performance (just in case)*/
            if (ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindOnlySavesAndLoadsOnPlayers &&
                    !(((Entity)(Object)this) instanceof Player))
                return;

            /**Every 20 ticks save a second on the entity for mandom rewinding*/
            if (roundabout$secondQue.isEmpty() || this.tickCount % 20 == 0) {
                if (!((TimeStop)level()).inTimeStopRange(((Entity) (Object)this))) {
                    roundabout$addSecondToQueue(SavedSecond.saveEntitySecond((Entity) (Object) this));
                }
            }
        }
    }


    @Unique
    public ArrayDeque<SavedSecond> roundabout$getSecondQue(){
        return roundabout$secondQue;
    }

    @Unique
    public SavedSecond roundabout$getLastSavedSecond(){
        if (!roundabout$secondQue.isEmpty()){
            return roundabout$secondQue.getLast();
        }
        return null;
    }
    @Unique
    public void roundabout$resetSecondQueue(){
         roundabout$secondQue = new ArrayDeque<>();
    }


    @Unique
    public void roundabout$setExclusiveLayers(boolean exclusive){
        this.roundabout$renderingExclusiveLayers = exclusive;
    }
    @Unique
    public boolean roundabout$getExclusiveLayers(){
        return this.roundabout$renderingExclusiveLayers;
    }

    @Override
    @Unique
    public void roundabout$setNoAAB(){
        bb = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
    public void roundabout$setRoundaboutPrevX(double roundaboutPrevX){
        this.roundabout$PrevX = roundaboutPrevX;
    }
    public void roundabout$setRoundaboutPrevY(double roundaboutPrevY){
        this.roundabout$PrevY = roundaboutPrevY;
    }
    public void roundabout$setRoundaboutPrevZ(double roundaboutPrevZ){
        this.roundabout$PrevZ = roundaboutPrevZ;
    }
    public double roundabout$getRoundaboutPrevX(){
        return this.roundabout$PrevX;
    }
    public double roundabout$getRoundaboutPrevY(){
        return this.roundabout$PrevY;
    }
    public double roundabout$getRoundaboutPrevZ(){
        return this.roundabout$PrevZ;
    }
    @Override
    @Unique
    public int roundabout$getNoGravTicks(){
        return this.roundabout$noGravityTicks;
    }
    @Override
    @Unique
    public void roundabout$setNoGravTicks(int ticks){
        this.roundabout$noGravityTicks = ticks;
    }

    @Override
    @Unique
    public Entity roundabout$getVehicle(){
        return this.vehicle;
    }
    @Override
    @Unique
    public void roundabout$setVehicle(Entity ride){
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
    public boolean roundabout$shadow = true;
    @Unique
    @Override
    public boolean roundabout$getShadow(){
        return roundabout$shadow;
    }
    @Unique
    @Override
    public void roundabout$setShadow(boolean shadow){
        roundabout$shadow = shadow;
    }

    /**Highlight color with Justice piloting*/
    /**See inputevents for allowing the mob to glow*/
    @Inject(method = "getTeamColor()I", at = @At("HEAD"), cancellable = true)
    public void roundabout$getTeamColor(CallbackInfoReturnable<Integer> cir){
        if (this.level.isClientSide()){
            int raga = ClientUtil.getOutlineColor(((Entity)(Object)this));
            if (raga != -1){
                cir.setReturnValue(raga);
            }
        }

    }
    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    public void roundabout$isInvisible(CallbackInfoReturnable<Boolean> cir){
        if (roundabout$getTrueInvisibility() > -1){
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
        }
    }


    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    public void roundabout$isInvisibleTo(Player pl, CallbackInfoReturnable<Boolean> cir){
        if (roundabout$getTrueInvisibility() > -1){
            if (pl != null && ((StandUser)pl).roundabout$getStandPowers() instanceof PowersAchtungBaby PA
            && PA.invisibleVisionOn()){
                cir.setReturnValue(false);
            }
            return;
        }
    }


    @Inject(method = "moveRelative", at = @At("HEAD"), cancellable = true)
    private void roundabout$moveRelative(float $$0, Vec3 $$1, CallbackInfo ci){

        if(((ILevelAccess)this.level()).roundabout$isFrictionPlundered(this.blockPosition()) ||
                ((ILevelAccess)this.level()).roundabout$isFrictionPlunderedEntity(((Entity)(Object)this))
        ){
            if (this.onGround() && ((Entity)(Object)this) instanceof LivingEntity LE) {
                if (!((StandUser)LE).roundabout$frictionSave().equals(Vec3.ZERO)) {
                        ci.cancel();
                }
            }
        }
    }
    @Inject(method = "walkingStepSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("HEAD"), cancellable = true)
    public void roundabout$walkingStepSound(BlockPos $$0, BlockState $$1, CallbackInfo ci){
        if(((ILevelAccess)this.level()).roundabout$isFrictionPlundered($$0) ||
        ((ILevelAccess)this.level()).roundabout$isFrictionPlunderedEntity(((Entity)(Object)this))){
            ci.cancel();
        }
    }

    @Inject(method = "getNameTagOffsetY", at = @At("HEAD"), cancellable = true)
    public void roundabout$getNameTagOffsetY(CallbackInfoReturnable<Float> cir){
        if (((Entity)(Object)this) instanceof Player PE){
            ItemStack stack = ((IPlayerEntity) PE).roundabout$getMaskSlot();
            if (stack !=null && !stack.isEmpty() && stack.getItem() instanceof MaskItem ME){
                cir.setReturnValue(this.getBbHeight() + ME.visageData.getNametagHeight());
            }
        }
    }
    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void roundabout$Turn(double $$0, double $$1, CallbackInfo ci){
        Entity thisEnt = ((Entity) (Object) this);
        if (((TimeStop) thisEnt.level()).CanTimeStopEntity(((Entity) (Object) this))){
            ci.cancel();
        } else if (thisEnt instanceof Player PE){
            StandUser sus = ((StandUser)PE);
            StandPowers powers = sus.roundabout$getStandPowers();
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
    private void roundabout$changeDim(ServerLevel $$0, CallbackInfoReturnable<Boolean> ci) {
        if (((Entity)(Object)this) instanceof LivingEntity LE){
            if (this.level() instanceof ServerLevel && !this.isRemoved()) {
                 if (((StandUser)this).roundabout$getStand() != null){
                     StandEntity stand = ((StandUser)this).roundabout$getStand();
                     if (!stand.getHeldItem().isEmpty()) {

                         if (stand.canAcquireHeldItem) {
                             double $$3 = stand.getEyeY() - 0.3F;
                             ItemEntity $$4 = new ItemEntity(this.level(), stand.getX(), $$3, stand.getZ(), stand.getHeldItem().copy());
                             $$4.setPickUpDelay(40);
                             $$4.setThrower(stand.getUUID());
                             this.level().addFreshEntity($$4);
                             stand.setHeldItem(ItemStack.EMPTY);
                         }
                         if (!stand.getPassengers().isEmpty()){
                             stand.ejectPassengers();
                         }
                     }
                 }
            }
        }
    }
    @Inject(method = "restoreFrom", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$restoreFrom (Entity $$0, CallbackInfo ci){
        if (((Entity) (Object) this) instanceof LivingEntity LE && $$0 instanceof LivingEntity LE2) {
        }
    }
    @Override
    public float roundabout$getPreTSTick() {
        return this.roundabout$PrevTick;
    }

    @Override
    public void roundabout$setPreTSTick(float frameTime) {
        roundabout$PrevTick = frameTime;
    }
    @Override
    public void roundabout$resetPreTSTick() {
        roundabout$PrevTick = 0;
    }

    @Override
    public float roundabout$getStepHeight() {
        return this.maxUpStep;
    }

    /**In a timestop, fire doesn't tick*/
    @Inject(method = "setRemainingFireTicks", at = @At("HEAD"), cancellable = true)
    protected void roundabout$SetFireTicks(int $$0, CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            ci.cancel();
        }
    }
    @Inject(method = "clearFire", at = @At("HEAD"), cancellable = true)
    protected void roundabout$ClearFire(CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            this.remainingFireTicks = 0;
        }
    }
    @Inject(method = "isControlledByLocalInstance", at = @At("HEAD"), cancellable = true)
    protected void roundabout$IsControlledByLocalInstance(CallbackInfoReturnable<Boolean> cir){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity LE && ((TimeStop)entity.level()).CanTimeStopEntity(entity)){
            cir.setReturnValue(this.isEffectiveAi());
        }
    }

    @Shadow
    public boolean isShiftKeyDown() {
        return false;
    }
    @Inject(method = "canRide", at = @At("HEAD"), cancellable = true)
    protected void roundabout$canRide(Entity $$0, CallbackInfoReturnable<Boolean> cir){
        if ($$0 instanceof StandEntity && (!(((Entity)(Object)this) instanceof LivingEntity) || ((TimeStop) ((Entity) (Object) this).level()).CanTimeStopEntity(((Entity) (Object) this)))){
            cir.setReturnValue(!this.isShiftKeyDown());
        }
    }
    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z", at = @At("HEAD"), cancellable = true)
    protected void roundabout$startRiding(Entity $$0, boolean $$1, CallbackInfoReturnable<Boolean> cir){
        if (((Entity)(Object)this) instanceof LivingEntity LE
                && ((StandUser)LE).roundabout$getStand() != null
                && ($$0.getRootVehicle().is(((StandUser)LE).roundabout$getStand()) || $$0.getRootVehicle().hasPassenger(((StandUser)LE).roundabout$getStand()))){
            cir.setReturnValue(false);
        }
    }


    @SuppressWarnings("deprecation")
    @Inject(method = "spawnSprintParticle()V", at = @At("HEAD"), cancellable = true)
    protected void roundabout$spawnSprintParticle(CallbackInfo ci){
        BlockPos $$0 = getOnPosLegacy();
        BlockState $$1 = this.level().getBlockState($$0);
        if ($$1.getBlock() instanceof FogBlock){
            ci.cancel();
        }
    }

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"),cancellable = true)
    protected void roundabout$push(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity le && ((StandUser) le).roundabout$getStandPowers().cancelCollision(((Entity)(Object)this))) {
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
    private Vec3 roundabout$DeltaBuildupTS = new Vec3(0,0,0);

    @Unique
    public Vec3 roundabout$getRoundaboutDeltaBuildupTS(){
        return this.roundabout$DeltaBuildupTS;
    }

    @Unique
    public void roundabout$setRoundaboutDeltaBuildupTS(Vec3 vec3){
        if (vec3 != null) {
            this.roundabout$DeltaBuildupTS = vec3;
        }
    }
    @Inject(method = "setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
    protected void roundabout$SetDeltaMovement(Vec3 vec3, CallbackInfo ci){
        if (((TimeStop) ((Entity) (Object) this).level()).CanTimeStopEntity(((Entity) (Object) this))){
            if (vec3.distanceTo(new Vec3(0,0,0)) > (roundabout$DeltaBuildupTS.distanceTo(new Vec3(0,0,0)) - 0.35)) {
                this.roundabout$DeltaBuildupTS = vec3;
            }
            ci.cancel();
        }
    }

    @Unique
    public boolean roundabout$jamBreath = false;
    @Unique
    public void roundabout$setRoundaboutJamBreath(boolean roundaboutJamBreath){
        this.roundabout$jamBreath = roundaboutJamBreath;
    }
    @Unique
    public boolean roundabout$getRoundaboutJamBreath(){
        return this.roundabout$jamBreath;
    }

    @Inject(method = "setAirSupply", at = @At("HEAD"), cancellable = true)
    public void roundabout$SetAirSupply(int $$0, CallbackInfo ci) {
        if (roundabout$jamBreath){
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

    @Override
    @Unique
    public void roundabout$universalTick(){
        roundabout$addSecondToQueue();
        roundabout$tickTrueInvisibility();
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tickH(CallbackInfo ci) {
        roundabout$universalTick();
    }
    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void roundabout$tick(CallbackInfo ci) {
        roundabout$tickQVec();
    }
    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$playSound(SoundEvent $$0, float $$1, float $$2,CallbackInfo ci) {
        if(((ILevelAccess)this.level()).roundabout$isSoundPlunderedEntity(((Entity) (Object)this))){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this.level()).roundabout$getSoundPlunderedBubbleEntity(((Entity) (Object)this));
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds($$0, this.getSoundSource(), $$1, $$2);
            }
            ci.cancel();
        }
    }
    @Inject(method = "thunderHit", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$thunderHit(CallbackInfo ci) {
        if (((Entity)(Object)this) instanceof Player PE){
            StandUser user = ((StandUser)PE);
            ItemStack stack = user.roundabout$getStandDisc();
            if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_THE_WORLD)){
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                if (!ipe.roundabout$getUnlockedBonusSkin()){
                    ci.cancel();
                    if (!level.isClientSide()) {
                        ipe.roundabout$setUnlockedBonusSkin(true);
                        level().playSound(null, getX(), getY(),
                                getZ(), ModSounds.UNLOCK_SKIN_EVENT, this.getSoundSource(), 2.0F, 1.0F);
                        ((ServerLevel) this.level()).sendParticles(ParticleTypes.END_ROD, this.getX(),
                                this.getY()+this.getEyeHeight(), this.getZ(),
                                10, 0.5, 0.5, 0.5, 0.2);
                        user.roundabout$setStandSkin(TheWorldEntity.OVER_HEAVEN);
                        ((ServerPlayer) ipe).displayClientMessage(
                                Component.translatable("unlock_skin.roundabout.the_world.over_heaven"), true);
                        user.roundabout$summonStand(level, true, false);
                    }
                }
            }
        }
    }
    @Unique
    @Override
    public void roundabout$tickQVec(){
        if (!this.level.isClientSide()) {
            Vec3 vec = new Vec3(roundabout$qknockback2params.x, roundabout$qknockback2params.y, roundabout$qknockback2params.z);
            if (!vec.equals(Vec3.ZERO) && vec.distanceTo(Vec3.ZERO) > 5) {
                if (vec.distanceTo(this.getPosition(1)) < 50) {
                    if (((Entity) (Object) this) instanceof LivingEntity le) {
                        le.teleportTo(vec.x, vec.y, vec.z);
                    } else {
                        this.moveTo(vec.x, vec.y, vec.z);
                    }
                    roundabout$qknockback2params = Vec3.ZERO;
                }
            }
            Vec3 vecx = new Vec3(roundabout$qknockback.x, roundabout$qknockback.y, roundabout$qknockback.z);
            if (!vecx.equals(Vec3.ZERO)) {
                MainUtil.takeUnresistableKnockbackWithYBias(((Entity) (Object) this), roundabout$qknockbackparams.x,
                        vecx.x,
                        vecx.y,
                        vecx.z,
                        (float) roundabout$qknockbackparams.y);
                roundabout$setQVec(Vec3.ZERO);
            }
        }
    }
    @Unique
    private Vec3 roundabout$qknockback = Vec3.ZERO;
    @Unique
    private Vec3 roundabout$qknockbackparams = Vec3.ZERO;
    @Unique
    private Vec3 roundabout$qknockback2params = Vec3.ZERO;
    @Unique
    @Override
    public void roundabout$setQVec(Vec3 ec){
        roundabout$qknockback = ec;
    }
    @Unique
    @Override
    public void roundabout$setQVecParams(Vec3 ec){
        roundabout$qknockbackparams = ec;
    }
    @Unique
    @Override
    public void roundabout$setQVec2Params(Vec3 ec){
        roundabout$qknockback2params = ec;
    }
    @Unique
    @Override
    public void roundabout$setDeltaMovementRaw(Vec3 ec){
        this.deltaMovement = ec;
    }


}
