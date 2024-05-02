package net.hydra.jojomod.event.powers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Vector3d;

import java.util.ArrayList;
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
    private byte activePower = 0;

    /**The phase of the move being used, primarily to keep track of which punch you are on in a punch string.*/
    private byte activePowerPhase = 0;

    /**Once a move finishes, this turns off in order to prevent a loop of infinite attacks should the move roll over.*/
    private boolean isAttacking = false;

    /**This is when the punch combo goes on cooldown. Default is 3 hit combo.*/
    private final byte activePowerPhaseMax = 3;

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
    public byte getActivePower(){
        return this.activePower;
    }
    public byte getActivePowerPhase(){
        return this.activePowerPhase;
    }
    public byte getActivePowerPhaseMax(){
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
    public float getStandReach(){
        return this.standReach;
    }
    public void setIsAttacking(boolean isAttacking){
        this.isAttacking = isAttacking;
    }
    public void setMaxAttackTime(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public void setActivePower(byte activeMove){
        this.activePower = activeMove;
    }
    public void setActivePowerPhase(byte activePowerPhase){
        this.activePowerPhase = activePowerPhase;
    }

    /**The cooldown for summoning. It is mostly clientside and doesn't have to be synced*/
    private int summonCD = 0;
    public boolean getSummonCD(){
        return this.summonCD <= 0;
    } public void setSummonCD(int summonCD){
        this.summonCD = summonCD;
    } public int getSummonCD2(){
        return this.summonCD;
    }

    /**This value prevents you from resummoning/blocking to cheese the 3 hit combo's last hit faster*/
    public int interruptCD = 0;
    public boolean getInterruptCD(){
        return this.interruptCD <= 0;
    }
    public void setInterruptCD(int interruptCD){
        this.interruptCD = interruptCD;
    }


    public void tickPower(){
            if (this.attackTimeDuring != -1) {
                this.attackTimeDuring++;
                if (this.attackTimeDuring == -1) {
                    poseStand((byte) 0);
                } else {
                    if (this.hasStandActive(this.self) && !this.self.isUsingItem()) {
                        if (this.activePower == PowerIndex.ATTACK && this.isAttacking) {
                            //RoundaboutMod.LOGGER.info("attack4");
                            this.updateAttack();
                        } else {
                            this.updateUniqueMoves();
                        }
                    } else {
                        resetAttackState();
                    }
                }
            }
            this.attackTime++;
            if (this.attackTime > this.attackTimeMax){
                this.setActivePowerPhase((byte) 0);
            }
            if (this.interruptCD > 0){
                this.interruptCD--;
            }
        if (this.summonCD > 0){
            this.summonCD--;
        }
    }
    public void updateAttack(){
        if (this.attackTimeDuring > this.attackTimeMax) {
            this.setAttackTimeDuring(-1);
            poseStand((byte) 0);
            this.attackTimeMax = 0;
            this.setPowerNone();
        } else {
            if (this.attackTimeDuring == 7) {
                this.standPunch();
            }
        }
    }

    public void resetAttackState(){
        this.interruptCD = 3;
        this.setAttackTimeDuring(-1);
        poseStand((byte) 0);
    }

    public void poseStand(byte r){
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
        /*By setting this to -10, there is a delay between the stand retracting*/
        this.attackTimeDuring = -10;
        this.isAttacking = false;

        if (this.self instanceof PlayerEntity){
            if (this.self.getWorld().isClient()) {
                if (isPacketPlayer()){
                    Entity targetEntity = getTargetEntity(this.self, -1);
                    int id;
                    if (targetEntity != null) {
                        id = targetEntity.getId();
                    } else {
                        id = -1;
                    }
                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeInt(id);
                    ClientPlayNetworking.send(ModMessages.STAND_PUNCH_PACKET, buffer);
                }
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            punchImpact(targetEntity);
        }

    }

    /**This function ensures the client sending attack packets is ONLY the player using the attack, prevents double attacking*/
    public boolean isPacketPlayer(){
        MinecraftClient mc = MinecraftClient.getInstance();
        return mc.player != null && mc.player.getId() == this.self.getId();
    }
    //((ServerWorld) this.self.getWorld()).spawnParticles(ParticleTypes.EXPLOSION,pointVec.x, pointVec.y, pointVec.z,
    //        1,0.0, 0.0, 0.0,1);


    public boolean knockShield(Entity entity, int duration){
        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
                    if (standUserData.isGuarding()) {
                        if (!standUserData.getGuardBroken()){
                            standUserData.breakGuard();
                            return true;
                        }
                    } else {
                        if (entity instanceof PlayerEntity){
                            ItemStack itemStack = ((LivingEntity) entity).getActiveItem();
                            Item item = itemStack.getItem();
                            if (item.getUseAction(itemStack) == UseAction.BLOCK) {
                                ((LivingEntity) entity).stopUsingItem();
                                ((PlayerEntity) entity).getItemCooldownManager().set(Items.SHIELD, duration);
                                return true;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2;
        } else {
            return 5;
        }
    } private float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 3;
        } else {
            return 7;
        }
    }
    public boolean getReducedDamage(Entity entity){
        return entity instanceof PlayerEntity;
    }

    public void punchImpact(Entity entity){
        if (entity != null) {
            float pow;
            float knockbackStrength;
            if (this.activePowerPhase == 3) {
                /*The last hit in a string has more power and knockback if you commit to it*/
                pow = getHeavyPunchStrength(entity);
                knockbackStrength = 2F;
            } else {
                pow = getPunchStrength(entity);
                knockbackStrength = 0.5F;
            }
             if (StandDamageEntityAttack(entity, pow, knockbackStrength, this.self)){
                 knockShield(entity, 40);
             }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.standReach, false);
            float halfReach = (float) (distMax*0.5);
            Vec3d pointVec = DamageHandler.getRayPoint(self, halfReach);
            ((ServerWorld) this.self.getWorld()).spawnParticles(ParticleTypes.EXPLOSION,pointVec.x, pointVec.y, pointVec.z,
                            1,0.0, 0.0, 0.0,1);
        }

        SoundEvent SE;
        float pitch = 1F;
        if (this.activePowerPhase >= this.activePowerPhaseMax){
            if (entity != null) {
                SE = ModSounds.PUNCH_4_SOUND_EVENT;
                pitch = 1.2F;
            } else {
                SE = ModSounds.PUNCH_2_SOUND_EVENT;
            }
        }
        else {
            if (entity != null) {
                SE = ModSounds.PUNCH_3_SOUND_EVENT;
                pitch = 1.1F + 0.07F*activePowerPhase;
            } else {
                SE = ModSounds.PUNCH_1_SOUND_EVENT;
            }
        }
        this.self.getWorld().playSound(null, this.self.getBlockPos(), SE, SoundCategory.PLAYERS, 0.95F, pitch);
    }

    public void damage(Entity entity){

    }

    public Entity getTargetEntity(Entity User, float distMax){
        /*First, attempts to hit what you are looking at*/
        if (!(distMax >= 0)) {
            distMax = this.getDistanceOut(this.self, this.standReach, false);
        }
        Entity targetEntity = this.rayCastEntity(this.self,distMax);
        /*If that fails, attempts to hit the nearest entity in a spherical radius in front of you*/
        if (targetEntity == null) {
            float halfReach = (float) (distMax*0.5);
            Vec3d pointVec = DamageHandler.getRayPoint(self, halfReach);
            targetEntity = StandAttackHitboxNear(StandGrabHitbox(DamageHandler.genHitbox(self, pointVec.x, pointVec.y,
                    pointVec.z, halfReach, halfReach, halfReach), distMax));
        }
        return targetEntity;
    }


    public float getDistanceOut(Entity entity, float range, boolean offset){
        float distanceFront = this.getRayDistance(entity, range);
        if (offset) {
            Entity targetEntity = this.rayCastEntity(this.self,this.standReach);
            if (targetEntity != null && targetEntity.distanceTo(entity) < distanceFront) {
                distanceFront = targetEntity.distanceTo(entity);
            }
            distanceFront -= 1;
            distanceFront = Math.max(Math.min(distanceFront, 1.7F), 0.4F);
        }
        return distanceFront;
    }

    public float getRayDistance(Entity entity, float range){
        Vec3d vec3d = entity.getCameraPosVec(0);
        Vec3d vec3d2 = entity.getRotationVec(0);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        HitResult blockHit = entity.getWorld().raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
        if (blockHit.getType() != HitResult.Type.MISS){
            return MathHelper.sqrt((float) entity.squaredDistanceTo(blockHit.getPos()));
        }
        return range;
    } public Vec3d getRayBlock(Entity entity, float range){
        Vec3d vec3d = entity.getCameraPosVec(0);
        Vec3d vec3d2 = entity.getRotationVec(0);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        HitResult blockHit = entity.getWorld().raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
        return blockHit.getPos();
    }

    public float getPivotPoint(Vector3d pointToRotate, Vector3d axisStart, Vector3d axisEnd) {
        Vector3d d = new Vector3d(axisEnd.x-axisStart.x,axisEnd.y-axisStart.y,axisEnd.z-axisStart.z).normalize();
        Vector3d v = new Vector3d(pointToRotate.x-axisStart.x,pointToRotate.y-axisStart.y,pointToRotate.z-axisStart.z).normalize();
        double t = v.dot(d);
        return (float) pointToRotate.distance(axisStart.add(d.mul(t)));
    }

    public static float angleDistance(float alpha, float beta) {
        float phi = Math.abs(beta - alpha) % 360;       // This is either the distance or 360 - distance
        float distance = phi > 180 ? 360 - phi : phi;
        return distance;
    }

    /**Returns the vertical angle between two mobs*/
    public float getLookAtEntityPitch(Entity user, Entity targetEntity) {
        double f;
        double d = targetEntity.getX() - user.getX();
        double e = targetEntity.getZ() - user.getZ();
        if (targetEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)targetEntity;
            f = livingEntity.getEyeY() - user.getEyeY();
        } else {
            f = (targetEntity.getBoundingBox().minY + targetEntity.getBoundingBox().maxY) / 2.0 - user.getEyeY();
        }
        double g = Math.sqrt(d * d + e * e);
        return (float)(-(MathHelper.atan2(f, g) * 57.2957763671875));
    }
    /**Returns the horizontal angle between two mobs*/
    public float getLookAtEntityYaw(Entity user, Entity targetEntity) {
        double d = targetEntity.getX() - user.getX();
        double e = targetEntity.getZ() - user.getZ();
        return (float)(MathHelper.atan2(e, d) * 57.2957763671875) - 90.0f;
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

    public List<Entity> StandGrabHitbox(List<Entity> entities, float maxDistance){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
            for (Entity value : entities) {
                if (!value.isLiving() || value.isInvulnerable() || !value.isAlive() || (this.self.hasVehicle() && this.self.getVehicle().getUuid() == value.getUuid())){
                    hitEntities.remove(value);
                } else {
                    int angle = 25;
                    /*RoundaboutMod.LOGGER.info("RD = "+String.valueOf(rayDist));*/
                    if (!(angleDistance(getLookAtEntityYaw(this.self, value), (this.self.getHeadYaw()%360f)) <= angle && angleDistance(getLookAtEntityPitch(this.self, value), this.self.getPitch()) <= angle)){
                        hitEntities.remove(value);
                    }
                    //RoundaboutMod.LOGGER.info("Yaw = "+angleDistance(getLookAtEntityYaw(this.self, value), (this.self.getHeadYaw()%360f))+" "+value.getName());
                    //RoundaboutMod.LOGGER.info("Pitch = "+angleDistance(getLookAtEntityPitch(this.self, value), this.self.getPitch())+" "+value.getName());
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
                if (this.StandDamageEntityAttack(value,pow, knockbackStrength, this.self)){
                    hitSomething = true;
                }
            }
        }
        return hitSomething;
    }

    public boolean StandDamageEntityAttack(Entity target, float pow, float knockbackStrength, Entity attacker){
        if (DamageHandler.StandDamageEntity(target,pow, attacker)){
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).takeKnockback(knockbackStrength * 0.5f, MathHelper.sin(this.self.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(this.self.getYaw() * ((float) Math.PI / 180)));
            }
            return true;
        }
        return false;
    }

    public Entity StandAttackHitboxNear(List<Entity> entities){
        float nearestDistance = -1;
        Entity nearestMob = null;

        if (entities != null){
            for (Entity value : entities) {
                if (!value.isInvulnerable() && value.isAlive() && value.getUuid() != this.self.getUuid()){
                    float distanceTo = value.distanceTo(this.self);
                    if ((nearestDistance < 0 || distanceTo < nearestDistance) && distanceTo <= this.standReach){
                        nearestDistance = distanceTo;
                        nearestMob = value;
                    }
                }
            }
        }

        return nearestMob;
    }

    public void updateUniqueMoves(){
    }

    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    public void tryPower(int move, boolean forced){
        if (this.activePower == PowerIndex.NONE || forced){
            if (move == PowerIndex.NONE) {
                this.setPowerNone();
            } else if (move == PowerIndex.ATTACK) {
                this.setPowerAttack();
            } else if (move == PowerIndex.GUARD) {
                 this.setPowerGuard();
            }
        }
    }
    public void setPowerNone(){
        this.attackTimeDuring = -1;
        this.setActivePower(PowerIndex.NONE);
        this.poseStand((byte) 0);
    }

    public boolean canAttack(){
        if (this.attackTimeDuring <= -1) {
            return this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax;
        }
        return false;
    }
    public void setPowerGuard() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.GUARD);
        this.poseStand((byte) 1);
    }
    public boolean isGuarding(){
        return this.activePower == PowerIndex.GUARD;
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
                this.setActivePower(PowerIndex.ATTACK);
                this.setAttackTime(0);

                this.poseStand((byte) 1);
            }
        }
    }
}
