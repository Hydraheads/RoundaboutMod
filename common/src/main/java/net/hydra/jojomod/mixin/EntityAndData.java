package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
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
    public @Nullable ItemStack roundabout$RenderChest;
    @Unique
    public @Nullable ItemStack roundabout$RenderLegs;
    @Unique
    public @Nullable ItemStack roundabout$RenderBoots;
    @Unique
    public @Nullable ItemStack roundabout$RenderHead;
    @Unique
    public @Nullable ItemStack roundabout$RenderMainHand;
    @Unique
    public @Nullable ItemStack roundabout$RenderOffHand;
    @Unique
    public int roundabout$noGravityTicks = 0;

    public void roundabout$setRoundaboutRenderChest(@Nullable ItemStack chest){
        this.roundabout$RenderChest = chest;
    }
    public void roundabout$setRoundaboutRenderLegs(@Nullable ItemStack legs){
        this.roundabout$RenderLegs = legs;
    }
    public void roundabout$setRoundaboutRenderBoots(@Nullable ItemStack boots){
        this.roundabout$RenderBoots = boots;
    }
    public void roundabout$setRoundaboutRenderHead(@Nullable ItemStack head){
        this.roundabout$RenderHead = head;
    }
    public void roundabout$setRoundaboutRenderMainHand(@Nullable ItemStack mainhand){
        this.roundabout$RenderMainHand = mainhand;
    }
    public void roundabout$setRoundaboutRenderOffHand(@Nullable ItemStack offhand){
        this.roundabout$RenderOffHand = offhand;
    }

    @Override
    @Unique
    public void roundabout$setNoAAB(){
        bb = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderChest(){
        return this.roundabout$RenderChest;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderLegs(){
        return this.roundabout$RenderLegs;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderBoots(){
        return this.roundabout$RenderBoots;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderHead(){
        return this.roundabout$RenderHead;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderMainHand(){
        return this.roundabout$RenderMainHand;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderOffHand(){
        return this.roundabout$RenderOffHand;
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
                     }
                 }
            }
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

    @Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$tick(CallbackInfo ci) {
        roundabout$tickQVec();
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
        if (!roundabout$qknockback2params.equals(Vec3.ZERO)){
            if (roundabout$qknockback2params.distanceTo(this.getPosition(0)) < 50) {
                if (((Entity) (Object) this) instanceof LivingEntity le) {
                    le.teleportTo(roundabout$qknockback2params.x, roundabout$qknockback2params.y, roundabout$qknockback2params.z);
                } else {
                    this.moveTo(roundabout$qknockback2params.x, roundabout$qknockback2params.y, roundabout$qknockback2params.z);
                }
                roundabout$qknockback2params = Vec3.ZERO;
            }
        }
        if (!roundabout$qknockback.equals(Vec3.ZERO)){
            MainUtil.takeUnresistableKnockbackWithYBias(((Entity)(Object)this), roundabout$qknockbackparams.x,
                    roundabout$qknockback.x,
                    roundabout$qknockback.y,
                    roundabout$qknockback.z,
                    (float)roundabout$qknockbackparams.y);
            roundabout$setQVec(Vec3.ZERO);
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


}
