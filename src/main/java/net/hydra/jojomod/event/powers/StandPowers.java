package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StandPowers {
    private final LivingEntity self;

    /**The time that passed since using the last attack. It counts up, so that a visual meter can display cooldowns.
    * It is also used to */
    private int attackTime = -1;

    /**The time within an attack. This matters, because if you desummon a stand the attack time doesnt reset */
    private int attackTimeDuring = -1;

    /**The time until the generic ability cooldown passes.
    This exists so you have downtime that non-stand users can get it and attack you during.*/
    private int attackTimeMax = -1;

    /**The id of the move being used. Ex: 1 = punch*/
    private int activePower = 0;

    /**The phase of the move being used, primarily to keep track of which punch you are on in a punch string.*/
    private int activePowerPhase = 0;

    /**Once a move finishes, this turns off in order to prevent a loop of infinite attacks should the move roll over.*/
    private boolean isAttacking = false;

    /**This is when the punch combo goes on cooldown. Default is 3 hit combo.*/
    private final int activePowerPhaseMax = 3;

    public StandPowers(LivingEntity self) {
        this.self = self;
    }
    public LivingEntity getSelf(){
        return this.self;
    }
    public int getAttackTime(){
        return this.attackTime;
    }
    public int getAttackTimeDuring(){
        return this.attackTimeDuring;
    }
    public int getActivePower(){
        return this.activePower;
    }
    public int getActivePowerPhase(){
        return this.activePowerPhase;
    }
    public int getActivePowerPhaseMax(){
        return this.activePowerPhaseMax;
    }

    public void setAttackTime(int attackTime){
        this.attackTime = attackTime;
    }
    public void setAttackTimeDuring(int attackTimeDuring){
        this.attackTimeDuring = attackTimeDuring;
    }
    public void setAttackTimeMax(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public int getAttackTimeMax(){
        return this.attackTimeMax;
    }
    public boolean getIsAttacking(){
        return this.isAttacking;
    }
    public void setIsAttacking(boolean isAttacking){
        this.isAttacking = isAttacking;
    }
    public void setMaxAttackTime(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public void setActivePower(int activeMove){
        this.activePower = activeMove;
    }
    public void setActivePowerPhase(int activePowerPhase){
        this.activePowerPhase = activePowerPhase;
    }

    public void switchActiveMove(int activeMove){
        this.setActivePower(activeMove);
        this.setAttackTime(0);
    }

    public void tickPower(){
        if (this.activePower != PowerIndex.NONE) {
            if (this.attackTimeDuring != -1) {
                this.attackTimeDuring++;
                if (this.attackTimeDuring == -1) {
                    poseStand(0);
                } else {
                    if (this.hasStandActive(this.self)) {
                        if (this.activePower == PowerIndex.ATTACK && this.isAttacking) {
                            //RoundaboutMod.LOGGER.info("attack4");
                            this.updateAttack();
                        } else {
                            this.updateUniqueMoves();
                        }
                    } else {
                        this.setAttackTimeDuring(-1);
                    }
                }
            }
            this.attackTime++;
            if (this.attackTime > this.attackTimeMax){
                this.setActivePowerPhase(0);
            }
        }
    }
    public void updateAttack(){
        if (this.attackTimeDuring > this.attackTimeMax) {
            this.setAttackTimeDuring(-1);
            poseStand(0);
            this.setPowerNone();
        } else {
            if (this.attackTimeDuring == 7) {
                this.standPunch();
            }
        }
    }

    public void poseStand(int r){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            stand.setOffsetType(r);
        }
    }

    public StandEntity getStandEntity(LivingEntity User){
        StandUserComponent standUserData = MyComponents.STAND_USER.get((LivingEntity) User);
        return standUserData.getStand();
    } public boolean hasStandEntity(LivingEntity User){
        StandUserComponent standUserData = MyComponents.STAND_USER.get((LivingEntity) User);
        return standUserData.hasStandOut();
    } public boolean hasStandActive(LivingEntity User){
        StandUserComponent standUserData = MyComponents.STAND_USER.get((LivingEntity) User);
        return standUserData.getActive();
    }

    float standReach = 5;

    public void standPunch(){
        float pow;
        float knockbackStrength;
        float halfReach = (float) (standReach*0.5);
        if (this.activePowerPhase == 3) {
            /*The last hit in a string has more power and knockback if you commit to it*/
            pow = 7;
            knockbackStrength = 2F;
        } else {
            pow=5;
            knockbackStrength = 0.5F;
        }

        /*By setting this to -10, there is a delay between the stand retracting*/
        this.attackTimeDuring = -10;
        this.isAttacking = false;
        SoundEvent SE;
        if (this.activePowerPhase >= this.activePowerPhaseMax){ SE = ModSounds.PUNCH_2_SOUND_EVENT; }
        else { SE = ModSounds.PUNCH_1_SOUND_EVENT;}
        this.self.getWorld().playSound(null, this.self.getBlockPos(), SE, SoundCategory.PLAYERS, 10F, 1F);

        Vec3d pointVec = DamageHandler.getRayPoint(self, halfReach);
        if (!self.getWorld().isClient()){
            ((ServerWorld) self.getWorld()).spawnParticles(ParticleTypes.EXPLOSION,pointVec.x, pointVec.y, pointVec.z, 1,0.0, 0.0, 0.0,1);
        }

        /*First, attempts to hit what you are looking at*/
        Entity targetEntity = this.rayCastEntity(this.self,this.standReach);
        if (targetEntity != null){
            StandDamageEntityAttack(targetEntity, pow, knockbackStrength);
        /*If that fails, attempts to hit the nearest entity in a spherical radius in front of you*/
        } else {
            StandAttackHitboxNear(StandGrabHitbox(DamageHandler.genHitbox(self, pointVec.x, pointVec.y, pointVec.z, halfReach, halfReach, halfReach),pointVec.x, pointVec.y - this.self.getEyeHeight(this.self.getPose()), pointVec.z, halfReach),pow,knockbackStrength);
        }
    }

    /** This code grabs an entity in front of you at the specified range, raycasting is used*/
    public Entity rayCastEntity(LivingEntity User, float reach){
        MinecraftClient mc = MinecraftClient.getInstance();
        float tickDelta = mc.getLastFrameDuration();
        Vec3d vec3d = User.getCameraPosVec(tickDelta);

        Vec3d vec3d2 = User.getRotationVec(1.0f);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
        float f = 1.0f;
        Box box = new Box(vec3d.x+reach, vec3d.y+reach, vec3d.z+reach, vec3d.x-reach, vec3d.y-reach, vec3d.z-reach);

        EntityHitResult entityHitResult = ProjectileUtil.raycast(User, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit() && !entity.isInvulnerable(), reach*reach);
        if (entityHitResult != null) {
            return entityHitResult.getEntity();
        }
        return null;
    }

    public List<Entity> StandGrabHitbox(List<Entity> entities, double x, double y, double z, float maxDistance){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
            for (Entity value : entities) {
                if (!value.isLiving() || (this.self.hasVehicle() && this.self.getVehicle().getUuid() == value.getUuid())
                || MathHelper.sqrt((float) value.squaredDistanceTo(x,y,z)) > maxDistance){
                    hitEntities.remove(value);
                }
            }
        return hitEntities;
    }
    public boolean StandAttackHitbox(List<Entity> entities, float pow, float knockbackStrength){
        boolean hitSomething = false;
        float nearestDistance = -1;
        Entity nearestMob;
        if (entities != null){
            for (Entity value : entities) {
                if (this.StandDamageEntityAttack(value,pow, knockbackStrength)){
                    hitSomething = true;
                }
            }
        }
        return hitSomething;
    }

    public boolean StandDamageEntityAttack(Entity target, float pow, float knockbackStrength){
        if (DamageHandler.StandDamageEntity(target,pow)){
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).takeKnockback(knockbackStrength * 0.5f, MathHelper.sin(this.self.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(this.self.getYaw() * ((float) Math.PI / 180)));
            }
            return true;
        }
        return false;
    }

    public boolean StandAttackHitboxNear(List<Entity> entities, float pow, float knockbackStrength){
        boolean hitSomething = false;
        float nearestDistance = -1;
        Entity nearestMob = null;

        if (entities != null){
            for (Entity value : entities) {
                if (!value.isInvulnerable() && value.getUuid() != this.self.getUuid()){
                    float distanceTo = value.distanceTo(this.self);
                    if ((nearestDistance < 0 || distanceTo < nearestDistance)){
                        nearestDistance = distanceTo;
                        nearestMob = value;
                    }
                }
            }
        }

        if (nearestMob != null) {
            if (this.StandDamageEntityAttack(nearestMob,pow, knockbackStrength)){
                hitSomething = true;
            }
        }
        return hitSomething;
    }

    public void updateUniqueMoves(){
    }

    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    public void tryPower(int move, boolean forced){
        if (this.activePower == PowerIndex.NONE || forced){
        if (move == PowerIndex.ATTACK){
            this.setPowerAttack();
        }}
    }

    private void setPowerNone(){
        this.attackTimeMax = 0;
        this.switchActiveMove(PowerIndex.NONE);
    }

    public void setPowerAttack(){
        if (this.attackTimeDuring <= -1) {
            if (this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) {
                if (this.activePowerPhase >= this.activePowerPhaseMax){
                    this.activePowerPhase = 1;
                } else {
                    this.activePowerPhase++;
                    if (this.activePowerPhase == this.activePowerPhaseMax) {
                        this.attackTimeMax= 40;
                    } else {
                        this.attackTimeMax= 30;
                    }

                }
                this.attackTimeDuring = 0;
                this.isAttacking = true;
                this.switchActiveMove(PowerIndex.ATTACK);

                poseStand(1);
            }
        }
    }
}
