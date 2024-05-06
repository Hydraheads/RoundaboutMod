package net.hydra.jojomod.networking.component;

import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.networking.MyComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/** This code is attached to every single
 * @see LivingEntity
 * and is to store the stand that the mob may or may not have.
 * It is also a cleaner method than a mixin.*/
public class StandUserData implements StandUserComponent, CommonTickingComponent {
    //StandUserComponent standUserData = (StandUserComponent) MyComponents.STAND_USER.get(player);
    private final LivingEntity User;
    @Nullable
    private StandEntity Stand;
    private boolean StandActive;

    /** StandID is used clientside only*/
    private int StandID = -1;
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

    private byte StopSound = -1;


    public void SetStopSound(byte stopSound) {
        this.StopSound = stopSound;
    }

    public StandUserData(LivingEntity entity) {
        this.User = entity;
    }
    public void tick() {
        //if (StandID > -1) {
        this.getStandPowers().tickPower();
        this.tickGuard();
        this.tickDaze();
        //}
    }

    public boolean isDazed(){
        return this.dazeTime > 0;
    } public void setDazed(byte dazeTime){
        this.dazeTime = dazeTime;
        this.sync();
    }

    /** Calling sync sends packets which update data on the client side.
     * @see #applySyncPacket */
    public void sync() {
        CanSync = true;
        MyComponents.STAND_USER.sync(this.User);
        CanSync = false;
    }

    public LivingEntity getUser() {
        return this.User;
    }

    public boolean getActive() {
        return this.StandActive;
    }
    /**If the player currently is stand attacking vs using items*/
    public boolean getMainhandOverride() {
        return this.StandActive;
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
     } public void breakGuard() {
        this.GuardBroken = true;
        this.sync();
    } public void damageGuard(float damage){
        float finalGuard = this.GuardPoints - damage;
        this.GuardCooldown = 10;
        if (finalGuard <= 0){
            this.GuardPoints = 0;
            this.breakGuard();
            this.sync();
        } else {
            this.GuardPoints = finalGuard;
            this.sync();
        }
    } public void fixGuard() {
        this.GuardPoints = this.maxGuardPoints;
        this.GuardBroken = false;
        this.sync();
    } public void regenGuard(float regen){
        float finalGuard = this.GuardPoints + regen;
        if (finalGuard >= this.maxGuardPoints){
            this.fixGuard();
        } else {
            this.GuardPoints = finalGuard;
            this.sync();
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
        if (this.dazeTime > 0){
            dazeTime--;
        }
    }

    public float getRayDistance(Entity entity, float range){
        return this.getStandPowers().getRayDistance(entity,range);
    }

    public void tryPower(int move, boolean forced){
        this.getStandPowers().tryPower(move,forced);
        this.sync();
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

    /** This code is here in case we want to restrict when the syncing happens.*/
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player){
        return CanSync;
    };

    /** Turns your stand "on". This updates the HUD, and is necessary in case the stand doesn't have a body.*/
    public void setActive(boolean active){
     this.StandActive = active;
        this.sync();
    }

    /** Sets a stand to a user, and a user to a stand.*/
    public void standMount(StandEntity StandSet){
        this.Stand = StandSet;
        StandSet.setMaster(User);
        this.sync();
    }

    /**Only sets a user's stand. Distinction may be important depending on when it is called.*/
    public void setStand(StandEntity StandSet){
        this.Stand = StandSet;
        this.sync();
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
                    ItemStack  itemStack = User.getActiveItem();
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
    public StandEntity getStand(){
        if (this.User.getWorld().isClient) {
            if ((this.Stand == null || this.Stand.isRemoved()) && this.StandID > -1) {
                this.Stand = (StandEntity) User.getWorld().getEntityById(this.StandID);
            }
        }
        return this.Stand;
    }
    public boolean hasStandOut() {
        return (Stand != null && Stand.isAlive() && !Stand.isRemoved());
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
    public void updateStandOutPosition(StandEntity passenger) {
        this.updateStandOutPosition(passenger, Entity::setPosition);
    }

    public void updateStandOutPosition(StandEntity passenger, Entity.PositionUpdater positionUpdater) {
        if (!(this.hasStandOut())) {
            return;
        }
        byte OT = passenger.getOffsetType();
        if (OffsetIndex.OffsetStyle(OT) != OffsetIndex.LOOSE_STYLE) {
            Vec3d grabPos = passenger.getStandOffsetVector(User);
            positionUpdater.accept(passenger, grabPos.x, grabPos.y, grabPos.z);

            passenger.setYaw(User.getHeadYaw()%360);
            passenger.setPitch(User.getPitch());
            passenger.setBodyYaw(User.getHeadYaw()%360);
            passenger.setHeadYaw(User.getHeadYaw()%360);
        }
    }

    /** This is where the server writes out the id of the user's stand, to send to the client as a packet.*/
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(this.StandActive);
        int stID; if (this.Stand == null){stID=-1;} else {stID = this.Stand.getId();}
        buf.writeInt(stID);
        buf.writeFloat(this.GuardPoints);
        buf.writeBoolean(this.GuardBroken);
        buf.writeByte(this.dazeTime);
        buf.writeByte(this.StopSound); this.StopSound = -1;
        StandPowers SP = this.getStandPowers();

        buf.writeInt(SP.getAttackTime());
        buf.writeInt(SP.getAttackTimeMax());
        buf.writeInt(SP.getAttackTimeDuring());
        buf.writeByte(SP.getActivePower());
        buf.writeByte(SP.getActivePowerPhase());
        buf.writeBoolean(SP.getIsAttacking());
    }

    /** This is where the client reads the entity ids sent by the server and puts them into code.
     * Basically, it's how the client learns the user's stand, and any other stand following them.*/
    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.StandActive = buf.readBoolean();
        int stID = buf.readInt();
        this.StandID = stID;
        this.Stand = (StandEntity) User.getWorld().getEntityById(stID);
        if (this.Stand != null){
            Stand.setMaster(User);
        }
        this.GuardPoints = buf.readFloat();
        this.GuardBroken = buf.readBoolean();
        this.dazeTime = buf.readByte();
        this.stopSounds(buf.readByte());
        StandPowers SP = this.getStandPowers();

        SP.setAttackTime(buf.readInt());
        SP.setAttackTimeMax(buf.readInt());
        SP.setAttackTimeDuring(buf.readInt());
        SP.setActivePower(buf.readByte());
        SP.setActivePowerPhase(buf.readByte());
        SP.setIsAttacking(buf.readBoolean());
    }

    public void stopSounds(byte soundNo){
        /**This is where we cancel sounds like barrage and barrage wind. Must change this.StopSound server side,
         * then send a sync packet*/
        if (soundNo != -1) {
            if (this.User.getWorld().isClient) {
                MinecraftClient.getInstance().getSoundManager().stopSounds(this.getStandPowers().getSoundID(soundNo), SoundCategory.PLAYERS);
            }
        }
    }

    public void onStandOutLookAround(StandEntity passenger) {
    }

    public void removeStandOut() {
        this.Stand = null;
        //this.emitGameEvent(GameEvent.ENTITY_DISMOUNT, passenger);
    }


    public void readFromNbt(NbtCompound tag) {

    }

    public void writeToNbt(NbtCompound tag) {

    }

}
