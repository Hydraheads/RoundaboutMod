package net.hydra.jojomod.networking.component;

import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.networking.MyComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
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


    public StandUserData(LivingEntity entity) {
        this.User = entity;
    }
    public void tick() {
        //if (StandID > -1) {
            this.getStandPowers().tickPower();
        //}
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
    public void setPowerAttack(){
        this.getStandPowers().setPowerAttack();
        this.sync();
    }
    public boolean isGuarding(){
        return this.getStandPowers().isGuarding();
    }
    public void setPowerGuard(){
        this.getStandPowers().setPowerGuard();
        this.sync();
    }
    public void setPowerNone(){
        this.getStandPowers().setPowerNone();
        this.sync();
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
        if ((!this.getActive() && !forced) || (forced && this.getActive())) {
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
        int OT = passenger.getOffsetType();
        if (OT == 0 || OT == 1) {
            Vec3d grabPos = passenger.getStandOffsetVector(User);
            positionUpdater.accept(passenger, grabPos.x, grabPos.y, grabPos.z);
        }
        if (OT == 0 || OT == 1) {
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
        StandPowers SP = this.getStandPowers();

        SP.setAttackTime(buf.readInt());
        SP.setAttackTimeMax(buf.readInt());
        SP.setAttackTimeDuring(buf.readInt());
        SP.setActivePower(buf.readByte());
        SP.setActivePowerPhase(buf.readByte());
        SP.setIsAttacking(buf.readBoolean());
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
