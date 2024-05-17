package net.hydra.jojomod.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements StandUser {
    /**If you are stand guarding, this controls you blocking enemy atttacks.
     * For the damage against stand guard, and sfx, see PlayerEntity mixin
     * damageShield
     */

    private final LivingEntity User = ((LivingEntity)(Object) this);
    @Nullable
    private StandEntity Stand;

    /** StandID is used clientside only*/

    private static final TrackedData<Integer> STAND_ID = DataTracker.registerData(LivingEntity.class,
            TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> STAND_ACTIVE = DataTracker.registerData(LivingEntity.class,
            TrackedDataHandlerRegistry.BOOLEAN);
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
        RoundaboutMod.LOGGER.info("Dazed Set.");
        this.dazeTime = dazeTime;
        this.syncDaze();
    }

    public boolean getActive() {
        return ((LivingEntity) (Object) this).getDataTracker().get(STAND_ACTIVE);
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
                float guardRegen = maxGuardPoints / 140;
                this.regenGuard(guardRegen);
            } else if (!this.isGuarding()){
                float guardRegen = maxGuardPoints / 200;
                this.regenGuard(guardRegen);
            }
        }
        if (this.GuardCooldown > 0){this.GuardCooldown--;}
    } public void tickDaze(){
        if (!this.User.getWorld().isClient) {
            if (this.dazeTime > 0) {
                ((LivingEntity)(Object)this).clearActiveItem();
                dazeTime--;
                if (dazeTime <= 0){
                    this.syncDaze();
                }
            }
        }
    }

    public void syncGuard(){
        if (((LivingEntity) (Object) this) instanceof PlayerEntity && !((LivingEntity) (Object) this).getWorld().isClient){
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeFloat(this.getGuardPoints());
            buffer.writeBoolean(this.getGuardBroken());
            ServerPlayNetworking.send(((ServerPlayerEntity) (Object) this),ModMessages.STAND_GUARD_POINT_ID, buffer);
        }
    }

    public void syncDaze(){
        if (((LivingEntity) (Object) this) instanceof PlayerEntity && !((LivingEntity) (Object) this).getWorld().isClient){
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeByte(this.dazeTime);
            ServerPlayNetworking.send(((ServerPlayerEntity) (Object) this),ModMessages.DAZE_ID, buffer);
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
        return !(this.User instanceof PlayerEntity) || !(((PlayerEntity) this.User).getItemCooldownManager().getCooldownProgress(Items.SHIELD, 0f) > 0);

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
            this.Powers = new StandPowers(User);
        }
        return this.Powers;
    }

    public void setStandPowers(StandPowers standPowers){
        this.Powers = standPowers;
    }


    /** Turns your stand "on". This updates the HUD, and is necessary in case the stand doesn't have a body.*/
    public void setActive(boolean active){
        ((LivingEntity) (Object) this).getDataTracker().set(STAND_ACTIVE, active);
    }

    /** Sets a stand to a user, and a user to a stand.*/
    public void standMount(StandEntity StandSet){
        this.setStand(StandSet);
        StandSet.setMaster(User);
    }

    /**Only sets a user's stand. Distinction may be important depending on when it is called.*/
    public void setStand(StandEntity StandSet){
        this.Stand = StandSet;
        ((LivingEntity) (Object) this).getDataTracker().set(STAND_ID, StandSet.getId());
    }


    /** Code that brings out a user's stand, based on the stand's summon sounds and conditions. */
    public void summonStand(World theWorld, boolean forced, boolean sound){
        boolean active;
        if (!this.getActive() || forced) {
            //world.getEntity
            StandEntity stand = ModEntities.THE_WORLD.create(User.getWorld());
            if (stand != null) {
                Hand hand = User.getActiveHand();
                if (hand == Hand.OFF_HAND) {
                    ItemStack itemStack = User.getActiveItem();
                    Item item = itemStack.getItem();
                    if (item.getUseAction(itemStack) == UseAction.BLOCK) {
                        User.stopUsingItem();
                    }
                }
                Vec3d spos = stand.getStandOffsetVector(User);
                stand.updatePosition(spos.getX(), spos.getY(), spos.getZ());

                theWorld.spawnEntity(stand);

                if (sound) {
                    stand.playSummonSound();
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
        if (((LivingEntity) (Object) this).getWorld().isClient) {
            return (StandEntity) ((LivingEntity) (Object) this).getWorld().getEntityById(((LivingEntity) (Object) this).getDataTracker().get(STAND_ID));
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
            if (!User.isSneaking() && User.isSprinting()){
                forward*=2;}
            Stand.setMoveForward(forward);
        }
    }

    /** Retooled vanilla riding code to update the location of a stand every tick relative to the entity it
     * is the user of.
     * @see StandEntity#getAnchorPlace */
    public void updateStandOutPosition(StandEntity stand) {
        this.updateStandOutPosition(stand, Entity::setPosition);
    }

    public void updateStandOutPosition(StandEntity stand, Entity.PositionUpdater positionUpdater) {
        if (!(this.hasStandOut())) {
            return;
        }
        byte OT = stand.getOffsetType();
        if (OffsetIndex.OffsetStyle(OT) != OffsetIndex.LOOSE_STYLE) {

            Vec3d grabPos = stand.getStandOffsetVector(User);
            positionUpdater.accept(stand, grabPos.x, grabPos.y, grabPos.z);

            stand.setYaw(User.getHeadYaw()%360);
            stand.setPitch(User.getPitch());
            stand.setBodyYaw(User.getHeadYaw()%360);
            stand.setHeadYaw(User.getHeadYaw()%360);
        }
    }

    public void onStandOutLookAround(StandEntity passenger) {
    }

    public void removeStandOut() {
        this.Stand = null;
        ((LivingEntity) (Object) this).getDataTracker().set(STAND_ID, -1);
        //this.emitGameEvent(GameEvent.ENTITY_DISMOUNT, passenger);
    }


    @Inject(method = "initDataTracker", at = @At(value = "TAIL"))
    private void initDataTrackerRoundabout(CallbackInfo ci) {
        ((LivingEntity)(Object)this).getDataTracker().startTracking(STAND_ID, -1);
        ((LivingEntity)(Object)this).getDataTracker().startTracking(STAND_ACTIVE, false);
    }

    @Inject(method = "handleStatus", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutHandleStatus(byte status, CallbackInfo ci) {
        if (status == 29){
            if (this.isGuardingEffectively2()) {
                if (!this.getGuardBroken()) {
                    ((Entity) (Object) this).playSound(ModSounds.STAND_GUARD_SOUND_EVENT, 0.8f, 0.9f + ((Entity) (Object) this).getWorld().random.nextFloat() * 0.3f);
                } else {
                    ((Entity) (Object) this).playSound(SoundEvents.ITEM_SHIELD_BREAK, 1f, 1.5f);
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

    @Inject(method = "applyArmorToDamage", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutApplyArmorToDamage(DamageSource source, float amount,CallbackInfoReturnable<Float> ci){
        if (source.isOf(ModDamageTypes.STAND)) {
            ci.setReturnValue(amount);
        }
    }

    /**Here, we cancel barrage if it has not "wound up" and the user is hit*/
    @Inject(method = "damage", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        if (this.isBarraging() &&
                source.getSource() != null) {
            this.tryPower(PowerIndex.GUARD,true);
        }
    }

    /**Prevent you from hearing every hit in a rush*/
    @Inject(method = "playHurtSound", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutPlayHurtSound(DamageSource source, CallbackInfo ci) {
        if (this.isDazed()) {
            ci.cancel();
        }
    }

    /**This Should prevent repeated crossbow charging on barrage*/
    @Inject(method = "tickActiveItemStack", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTickActiveItemStack(CallbackInfo ci) {
        if (this.isDazed()) {
            ci.cancel();
        }
    }


    /**Part of Registering Stand Guarding as a form of Blocking*/
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V", shift = At.Shift.BEFORE))
    private void RoundaboutDamage2(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        if (this.isGuarding()) {
            if (!source.isIn(DamageTypeTags.BYPASSES_COOLDOWN) && this.getGuardCooldown() > 0) {
                return;
            }
            this.damageGuard(amount);
        }
    }

    /**Entities who are caught in a barrage stop moving from their own volition in the x and z directions.*/
    @ModifyVariable(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "HEAD"))
    private Vec3d RoundaboutTravel(Vec3d movementInput) {
        if (this.isDazed()) {
            return new Vec3d(0,0,0);
        } else {
            return movementInput;
        }
    }

    /**This code stops a barrage target from losing velocity, preventing lag spikes from causing them to drop.*/
    @ModifyVariable(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At("STORE"))
    private double RoundaboutTravel2(double d) {
        if (this.isDazed()) {
            return 0;
        }
        return d;
    }

    /**This code prevents you from swimming upwards while barrage clashing*/
    @Inject(method = "swimUpward", at = @At(value = "HEAD"), cancellable = true)
    protected void swimUpward(TagKey<Fluid> fluid, CallbackInfo ci) {
        if (this.isClashing()) {
            ci.cancel();
        }
    }
}
