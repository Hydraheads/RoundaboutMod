package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class StandUserEntity implements StandUser {
    /**If you are stand guarding, this controls you blocking enemy atttacks.
     * For the damage against stand guard, and sfx, see PlayerEntity mixin
     * damageShield
     */

    private final LivingEntity User = ((LivingEntity)(Object) this);
    @Nullable
    private StandEntity Stand;

    /** StandID is used clientside only*/

    private static final EntityDataAccessor<Integer> STAND_ID = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> STAND_ACTIVE = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);
    private boolean CanSync;
    private StandPowers Powers;

    /** Guard variables for stand blocking**/
    public final float maxGuardPoints = 15F;
    private float GuardPoints = maxGuardPoints;
    private boolean GuardBroken = false;
    private int GuardCooldown = 0;



    /** These variables control if someone is dazed, stunned, frozen, or controlled.**/

    /* dazeTime: how many ticks left of daze. Inflicted by stand barrage,
     * daze lets you scroll items and look around, but it takes away
     * your movement, item usage, and stand ability usage. You also
     * have no gravity while dazed**/

    private byte dazeTime = 0;


    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tickRoundabout(CallbackInfo ci) {
        //if (StandID > -1) {
        this.getStandPowers().tickPower();
        this.tickGuard();
        this.tickDaze();
        //}
    }



    /**returns if the mob has a stand. For now, returns if stand is active, but in the future will be more
     * complicated**/
    public boolean isStandUser(){
        return this.getActive();
    }

    public boolean isDazed(){
        return this.dazeTime > 0;
    }
    public void setDazeTime(byte dazeTime){
        this.dazeTime = dazeTime;
    }
    public void setDazed(byte dazeTime){
        this.dazeTime = dazeTime;
        this.syncDaze();
    }

    public boolean getActive() {
        return ((LivingEntity) (Object) this).getEntityData().get(STAND_ACTIVE);
    }
    /**If the player currently is stand attacking vs using items*/
    public boolean getMainhandOverride() {
        return this.getActive();
    }
    public float getMaxGuardPoints(){
        return this.maxGuardPoints;
    }
    public float getGuardCooldown(){
        return this.GuardCooldown;
    }
    public float getGuardPoints(){
        return this.GuardPoints;
    } public void setGuardPoints(float GuardPoints){
        this.GuardPoints = GuardPoints;
    }
    public boolean getGuardBroken(){
        return this.GuardBroken;
    }
    public void breakGuard() {
        this.GuardBroken = true;
        this.syncGuard();
    } public void setGuardBroken(boolean guardBroken){
        this.GuardBroken = guardBroken;
    }
    public void damageGuard(float damage){
        float finalGuard = this.GuardPoints - damage;
        this.GuardCooldown = 10;
        if (finalGuard <= 0){
            this.GuardPoints = 0;
            this.breakGuard();
            this.syncGuard();
        } else {
            this.GuardPoints = finalGuard;
            this.syncGuard();
        }
    } public void fixGuard() {
        this.GuardPoints = this.maxGuardPoints;
        this.GuardBroken = false;
        this.syncGuard();
    } public void regenGuard(float regen){
        float finalGuard = this.GuardPoints + regen;
        if (finalGuard >= this.maxGuardPoints){
            this.fixGuard();
        } else {
            this.GuardPoints = finalGuard;
            this.syncGuard();
        }
    } public void tickGuard(){
        if (this.GuardPoints < this.maxGuardPoints) {
            if (this.GuardBroken){
                float guardRegen = maxGuardPoints / 100;
                this.regenGuard(guardRegen);
            } else if (!this.isGuarding() && this.shieldNotDisabled()){
                float guardRegen = maxGuardPoints / 200;
                this.regenGuard(guardRegen);
            }
            if (this.isGuarding() && !shieldNotDisabled()){
                this.setAttackTimeDuring(0);
            }
        }
        if (this.GuardCooldown > 0){this.GuardCooldown--;}
    } public void tickDaze(){
        if (!this.User.level().isClientSide) {
            if (this.dazeTime > 0) {
                ((LivingEntity)(Object)this).stopUsingItem();
                dazeTime--;
                if (dazeTime <= 0){
                    this.getStandPowers().animateStand((byte) 0);
                    this.syncDaze();
                }
            }
        }
    }

    public void syncGuard(){
        if (((LivingEntity) (Object) this) instanceof Player && !((LivingEntity) (Object) this).level().isClientSide){
            ModPacketHandler.PACKET_ACCESS.StandGuardPointPacket(((ServerPlayer) (Object) this),this.getGuardPoints(),this.getGuardBroken());
        }
    }

    public void syncDaze(){
        if (((LivingEntity) (Object) this) instanceof Player && !((LivingEntity) (Object) this).level().isClientSide){
            ModPacketHandler.PACKET_ACCESS.DazeTimePacket(((ServerPlayer) (Object) this),this.dazeTime);
        }
    }

    public float getRayDistance(Entity entity, float range){
        return this.getStandPowers().getRayDistance(entity,range);
    }

    public void tryPower(int move, boolean forced){
        if (!this.isClashing() || move == PowerIndex.NONE) {
            this.getStandPowers().tryPower(move, forced);
            this.getStandPowers().syncCooldowns();
        }
    }



    public boolean canAttack(){
        return this.getStandPowers().canAttack();
    }
    public int getSummonCD2(){
        return this.getStandPowers().getSummonCD2();
    }
    public Entity getTargetEntity(Entity User, float distMax){
        return this.getStandPowers().getTargetEntity(User, distMax);
    }
    public boolean getSummonCD(){
        return this.getStandPowers().getSummonCD();
    } public void setSummonCD(int summonCD){
        this.getStandPowers().setSummonCD(summonCD);
    }
    public byte getActivePower(){
        return this.getStandPowers().getActivePower();
    }
    public LivingEntity getPowerUser(){
        return this.getStandPowers().getSelf();
    }
    public int getAttackTimeMax(){
        return this.getStandPowers().getAttackTimeMax();
    }
    public int getAttackTime(){
        return this.getStandPowers().getAttackTime();
    }
    public int getAttackTimeDuring(){
        return this.getStandPowers().getAttackTimeDuring();
    }
    public boolean getInterruptCD(){
        return this.getStandPowers().getInterruptCD();
    }
    public byte getActivePowerPhase(){
        return this.getStandPowers().getActivePowerPhase();
    }public byte getActivePowerPhaseMax(){
        return this.getStandPowers().getActivePowerPhaseMax();
    }
    public float getStandReach(){
        return this.getStandPowers().getStandReach();
    }
    public boolean isGuarding(){
        return this.getStandPowers().isGuarding();
    }
    public boolean isBarraging(){
        return this.getStandPowers().isBarraging();
    }
    public boolean isClashing(){
        return this.getStandPowers().isClashing();
    }
    public boolean isGuardingEffectively(){
        if (this.GuardBroken){return false;}
        return this.isGuardingEffectively2();
    }
    public boolean isGuardingEffectively2(){
        return (this.shieldNotDisabled() && this.getStandPowers().isGuarding() && this.getStandPowers().getAttackTimeDuring() >= 3);
    }

    public boolean shieldNotDisabled(){
        return !(this.User instanceof Player) || !(((Player) this.User).getCooldowns().getCooldownPercent(Items.SHIELD, 0f) > 0);

    }
    public float getDistanceOut(Entity entity, float range, boolean offset){
        return this.getStandPowers().getDistanceOut(entity,range,offset);
    }
    public void setAttackTimeDuring(int attackTimeDuring){
        this.getStandPowers().setAttackTimeDuring(attackTimeDuring);
    } public void setInterruptCD(int interruptCD){
        this.getStandPowers().setInterruptCD(interruptCD);
    }

    public StandPowers getStandPowers() {
        if (this.Powers == null) {
            this.Powers = new PowersTheWorld(User);
        }
        return this.Powers;
    }

    public void setStandPowers(StandPowers standPowers){
        this.Powers = standPowers;
    }


    /** Turns your stand "on". This updates the HUD, and is necessary in case the stand doesn't have a body.*/
    public void setActive(boolean active){
        ((LivingEntity) (Object) this).getEntityData().set(STAND_ACTIVE, active);
    }

    /** Sets a stand to a user, and a user to a stand.*/
    public void standMount(StandEntity StandSet){
        this.setStand(StandSet);
        StandSet.setMaster(User);
    }

    /**Only sets a user's stand. Distinction may be important depending on when it is called.*/
    public void setStand(StandEntity StandSet){
        this.Stand = StandSet;
        ((LivingEntity) (Object) this).getEntityData().set(STAND_ID, StandSet.getId());
    }


    /** Code that brings out a user's stand, based on the stand's summon sounds and conditions. */
    public void summonStand(Level theWorld, boolean forced, boolean sound){
        boolean active;
        if (!this.getActive() || forced) {
            //world.getEntity
            StandEntity stand = ModEntities.THE_WORLD.create(User.level());
            if (stand != null) {
                InteractionHand hand = User.getUsedItemHand();
                if (hand == InteractionHand.OFF_HAND) {
                    ItemStack itemStack = User.getUseItem();
                    Item item = itemStack.getItem();
                    if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                        User.releaseUsingItem();
                    }
                }
                Vec3 spos = stand.getStandOffsetVector(User);
                stand.absMoveTo(spos.x(), spos.y(), spos.z());

                theWorld.addFreshEntity(stand);

                if (sound) {
                    this.getStandPowers().playSummonSound();
                }

                this.standMount(stand);
            }

            active=true;
        } else {
            this.tryPower(PowerIndex.NONE,true);
            active=false;
        }
        this.setActive(active);
    }

    /** Returns the stand of a User, and makes necessary checks to reload the stand on a client
     * if the client does not have the stand loaded*/

    @Nullable
    public StandEntity getStand() {
        if (((LivingEntity) (Object) this).level().isClientSide) {
            return (StandEntity) ((LivingEntity) (Object) this).level().getEntity(((LivingEntity) (Object) this).getEntityData().get(STAND_ID));
        } else {
            return this.Stand;
        }
    }
    public boolean hasStandOut() {
        StandEntity standOut = this.getStand();
        return (standOut != null && standOut.isAlive() && !standOut.isRemoved());
    }


    /** Set Direction input. This is part of stand rendering as leaning.
     * @see StandEntity#setMoveForward */
    public void setDI(byte forward, byte strafe){
        //RoundaboutMod.LOGGER.info("MF:"+ forward);
        if (Stand != null){
            if (!User.isShiftKeyDown() && User.isSprinting()){
                forward*=2;}
            Stand.setMoveForward(forward);
        }
    }

    /** Retooled vanilla riding code to update the location of a stand every tick relative to the entity it
     * is the user of.
     * @see StandEntity#getAnchorPlace */
    public void updateStandOutPosition(StandEntity stand) {
        this.updateStandOutPosition(stand, Entity::setPos);
    }

    public void updateStandOutPosition(StandEntity stand, Entity.MoveFunction positionUpdater) {
        if (!(this.hasStandOut())) {
            return;
        }
        byte OT = stand.getOffsetType();
        if (OffsetIndex.OffsetStyle(OT) != OffsetIndex.LOOSE_STYLE) {

            Vec3 grabPos = stand.getStandOffsetVector(User);
            positionUpdater.accept(stand, grabPos.x, grabPos.y, grabPos.z);

            stand.setYRot(User.getYHeadRot()%360);
            stand.setXRot(User.getXRot());
            stand.setYBodyRot(User.getYHeadRot()%360);
            stand.setYHeadRot(User.getYHeadRot()%360);
        } else {
            Roundabout.LOGGER.info("4 "+(stand.position()));
            positionUpdater.accept(stand, stand.getX(), stand.getY(), stand.getZ());
        }
    }

    public void onStandOutLookAround(StandEntity passenger) {
    }

    public void removeStandOut() {
        this.Stand = null;
        ((LivingEntity) (Object) this).getEntityData().set(STAND_ID, -1);
        //this.emitGameEvent(GameEvent.ENTITY_DISMOUNT, passenger);
    }


    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void initDataTrackerRoundabout(CallbackInfo ci) {
        ((LivingEntity)(Object)this).getEntityData().define(STAND_ID, -1);
        ((LivingEntity)(Object)this).getEntityData().define(STAND_ACTIVE, false);
    }

    @Inject(method = "handleEntityEvent", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutHandleStatus(byte $$0, CallbackInfo ci) {
        if ($$0 == 29){
            if (this.isGuardingEffectively2()) {
                if (!this.getGuardBroken()) {
                    ((Entity) (Object) this).playSound(ModSounds.STAND_GUARD_SOUND_EVENT, 0.8f, 0.9f + ((Entity) (Object) this).level().random.nextFloat() * 0.3f);
                } else {
                    ((Entity) (Object) this).playSound(SoundEvents.SHIELD_BREAK, 1f, 1.5f);
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "isBlocking", at = @At(value = "HEAD"), cancellable = true)
    private void isBlockingRoundabout(CallbackInfoReturnable<Boolean> ci) {
        if (this.isGuarding()){
            ci.setReturnValue(this.isGuardingEffectively());
        }
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutApplyArmorToDamage(DamageSource $$0, float $$1,CallbackInfoReturnable<Float> ci){
        if ($$0.is(ModDamageTypes.STAND)) {
            ci.setReturnValue($$1);
        }
    }

    /**Here, we cancel barrage if it has not "wound up" and the user is hit*/
    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutDamage(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> ci) {
        if (this.isBarraging() &&
                $$0.getDirectEntity() != null) {
            this.tryPower(PowerIndex.GUARD,true);
        }
    }

    /**Prevent you from hearing every hit in a rush*/
    @Inject(method = "playHurtSound", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutPlayHurtSound(DamageSource $$0, CallbackInfo ci) {
        if (this.isDazed()) {
            ci.cancel();
        }
    }

    /**This Should prevent repeated crossbow charging on barrage*/
    @Inject(method = "updatingUsingItem", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTickActiveItemStack(CallbackInfo ci) {
        if (this.isDazed()) {
            ci.cancel();
        }
    }


    /**Part of Registering Stand Guarding as a form of Blocking*/
    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V", shift = At.Shift.BEFORE))
    private void RoundaboutDamage2(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        if (this.isGuarding()) {
            if (!source.is(DamageTypeTags.BYPASSES_COOLDOWN) && this.getGuardCooldown() > 0) {
                return;
            }
            this.damageGuard(amount);
        }
    }

    /**Entities who are caught in a barrage stop moving from their own volition in the x and z directions.*/
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "HEAD"))
    private Vec3 RoundaboutTravel(Vec3 $$0) {
        if (this.isDazed()) {
            return new Vec3(0,0,0);
        } else {
            return $$0;
        }
    }

    /**This code stops a barrage target from losing velocity, preventing lag spikes from causing them to drop.*/
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("STORE"))
    private double RoundaboutTravel2(double $$1) {
        if (this.isDazed()) {
            return 0;
        }
        return $$1;
    }

    /**This code prevents you from swimming upwards while barrage clashing*/
    @Inject(method = "jumpInLiquid", at = @At(value = "HEAD"), cancellable = true)
    protected void swimUpward(TagKey<Fluid> $$0, CallbackInfo ci) {
        if (this.isClashing()) {
            ci.cancel();
        }
    }

}
