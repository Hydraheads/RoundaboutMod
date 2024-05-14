package net.hydra.jojomod.event.powers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
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

    /**This is when the punch combo goes on cooldown. Default is 3 hit combo.*/
    private final byte activePowerPhaseMax = 3;

    /**This variable exists so that a client can begin displaying your attack hud info without ticking through it.
     * Basically, stand attacks are clientside, but they need the server's confirmation to kickstart so you
     * can't hit targets in frozen tps*/
    private boolean kickStarted = true;

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
    public float getStandReach(){
        return this.standReach;
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

    /**This updates when a punch is thrown, to stop the stand from throwing the same punch twice if the game lags*/
    private byte activePowerPhaseCheck = -1;

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

    /**
     * Returns the sound based on the punch # in the rush.
     * -1 signifies the last hit in the rush
     */
    private SoundEvent getBarrageCrySound(){
            return ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT;
    }

    public Identifier getBarrageCryID(){
        return ModSounds.STAND_THEWORLD_MUDA1_SOUND_ID;
    }
    private SoundEvent getBarrageChargeSound(){
        return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
    }
    public Identifier getBarrageChargeID(){
        return ModSounds.STAND_BARRAGE_WINDUP_ID;
    }
    private SoundEvent getLastHitSound(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
    }

    public Identifier getLastHitID(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_ID;
    }

    public Identifier getSoundID(byte soundNumber){
        if (soundNumber == SoundIndex.BARRAGE_CRY_SOUND) {
            return getBarrageCryID();
        } else if (soundNumber == SoundIndex.BARRAGE_CHARGE_SOUND) {
            return getBarrageChargeID();
        }
        return null;
    }

    public void tickPower(){
        if (this.self.isAlive() && !this.self.isRemoved()) {
            if (!this.self.getWorld().isClient || kickStarted) {
                if (this.attackTimeDuring != -1) {
                    this.attackTimeDuring++;
                    if (this.attackTimeDuring == -1) {
                        poseStand(OffsetIndex.FOLLOW);
                    } else {
                        if (this.hasStandActive(this.self) && !this.self.isUsingItem() && !this.isDazed(this.self)) {
                            if (this.activePower == PowerIndex.ATTACK && this.attackTimeDuring > -1) {
                                this.updateAttack();
                            } else if (this.isBarraging()) {
                                this.updateBarrage();
                            } else {
                                this.updateUniqueMoves();
                            }
                        } else {
                            resetAttackState();
                        }
                    }
                }
                this.attackTime++;
                if (this.attackTime > this.attackTimeMax) {
                    this.setActivePowerPhase((byte) 0);
                }
                if (this.interruptCD > 0) {
                    this.interruptCD--;
                }
            }
            if (this.summonCD > 0) {
                this.summonCD--;
            }
        }
    }
    public void updateBarrage(){
        if (this.attackTimeDuring >= this.getBarrageWindup()) {
            if (this.attackTimeDuring > this.getBarrageWindup() + this.getBarrageLength()) {
                this.setPowerGuard();
            } else {
                if (this.attackTimeDuring == this.getBarrageWindup()) {
                    if (!this.self.getWorld().isClient()) {
                        SoundEvent barrageCrySound = this.getBarrageCrySound();
                        if (barrageCrySound != null) {
                            this.self.getWorld().playSound(null, this.self.getBlockPos(), barrageCrySound,
                                    SoundCategory.PLAYERS, 0.95F, 1);
                        }
                    }
                }
                standBarrageHit();
            }
        }
    }
    public void updateAttack(){
        if (this.attackTimeDuring > this.attackTimeMax) {
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
        poseStand(OffsetIndex.FOLLOW);
    }

    public void poseStand(byte r){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            stand.setOffsetType(r);
        }
    }

    public StandUser getUserData(LivingEntity User){
        return ((StandUser) User);
    }
    public StandEntity getStandEntity(LivingEntity User){
        return this.getUserData(User).getStand();
    } public boolean hasStandEntity(LivingEntity User){
        return this.getUserData(User).hasStandOut();
    } public boolean hasStandActive(LivingEntity User){
        return this.getUserData(User).getActive();
    }

    float standReach = 5;

    public int getTargetEntityId(){
        Entity targetEntity = getTargetEntity(this.self, -1);
        int id;
        if (targetEntity != null) {
            id = targetEntity.getId();
        } else {
            id = -1;
        }
        return id;
    }
    public void standBarrageHit(){
        if (this.self instanceof PlayerEntity){
            if (isPacketPlayer()){
                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeInt(getTargetEntityId());
                    buffer.writeInt(this.attackTimeDuring);
                    ClientPlayNetworking.send(ModMessages.STAND_BARRAGE_HIT_PACKET, buffer);
            }
        } else {
            /*Caps how far out the barrage hit goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            barrageImpact(targetEntity, this.attackTimeDuring);
        }
    }

    public void standPunch(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof PlayerEntity){
            if (isPacketPlayer()){
                //RoundaboutMod.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
                this.attackTimeDuring = -10;
                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeInt(getTargetEntityId());
                buffer.writeByte(this.activePowerPhase);
                ClientPlayNetworking.send(ModMessages.STAND_PUNCH_PACKET, buffer);
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            punchImpact(targetEntity);
        }

    }

    /**This function ensures the client sending attack packets is ONLY the player using the attack, prevents double attacking*/
    public boolean isPacketPlayer(){
        if (this.self.getWorld().isClient) {
            MinecraftClient mc = MinecraftClient.getInstance();
            return mc.player != null && mc.player.getId() == this.self.getId();
        }
        return false;
    }
    //((ServerWorld) this.self.getWorld()).spawnParticles(ParticleTypes.EXPLOSION,pointVec.x, pointVec.y, pointVec.z,
    //        1,0.0, 0.0, 0.0,1);

    private boolean isDazed(LivingEntity entity){
        return this.getUserData(entity).isDazed();
    }
    private void setDazed(LivingEntity entity, byte dazeTime){
        if ((1.0 - entity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) <= 0.0) {
            /*Warden, iron golems, and anything else knockback immmune can't be dazed**/
            return;
        } else if (entity instanceof EnderDragonEntity || entity instanceof WitherEntity){
            /*Bosses can't be dazed**/
            return;
        }
        this.getUserData(entity).setDazed(dazeTime);
    }

    public boolean knockShield(Entity entity, int duration){

        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    StandUser standUser= this.getUserData((LivingEntity) entity);
                    if (standUser.isGuarding()) {
                        if (!standUser.getGuardBroken()){
                            standUser.breakGuard();
                        }
                    }
                    if (entity instanceof PlayerEntity){
                         ItemStack itemStack = ((LivingEntity) entity).getActiveItem();
                         Item item = itemStack.getItem();
                         if (item.getUseAction(itemStack) == UseAction.BLOCK) {
                             ((LivingEntity) entity).stopUsingItem();
                             ((PlayerEntity) entity).clearActiveItem();
                         }
                        ((PlayerEntity) entity).getItemCooldownManager().set(Items.SHIELD, duration);
                        entity.getWorld().sendEntityStatus(entity, EntityStatuses.BREAK_SHIELD);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**Override these methods to fine tune the attack strength of the stand*/
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
    } private float getBarrageFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 3;
        } else {
            return 8;
        }
    } private float getBarrageHitStrength(Entity entity){
        float barrageLength = this.getBarrageLength();
        float power;
        if (this.getReducedDamage(entity)){
            power = 8/barrageLength;
        } else {
            power = 20/barrageLength;
        }
        /**Barrage hits are incapable of killing their target until the last hit.*/
        if (entity instanceof LivingEntity){
            if (power >= ((LivingEntity) entity).getHealth()){
                if (entity instanceof PlayerEntity) {
                    power = 0.00001F;
                } else {
                    power = 0F;
                }
            }
        }
        return power;
    }
    public boolean getReducedDamage(Entity entity){
        return entity instanceof PlayerEntity;
    }

    public void barrageImpact(Entity entity, int hitNumber){
        if (entity != null) {
            if (hitNumber >= this.getBarrageWindup()) {
                float pow;
                float knockbackStrength = 0;
                /**By saving the velocity before hitting, we can let people approach barraging foes
                 * through shields.*/
                Vec3d prevVelocity = entity.getVelocity();
                boolean lastHit = (hitNumber >= (this.getBarrageLength() + this.getBarrageWindup()));
                if (lastHit) {
                    pow = this.getBarrageFinisherStrength(entity);
                    knockbackStrength = 2.2F;
                } else {
                    pow = this.getBarrageHitStrength(entity);
                    knockbackStrength = 0.01F;
                }
                if (StandDamageEntityAttack(entity, pow, 0.0001F, this.self)) {
                    if (entity instanceof LivingEntity) {
                        if (lastHit) {
                            setDazed((LivingEntity) entity, (byte) 0);
                            playBarrageEndNoise(hitNumber);
                        } else {
                            setDazed((LivingEntity) entity, (byte) 3);
                            playBarrageNoise(hitNumber);
                        }
                    }
                    barrageImpact2(entity, lastHit, knockbackStrength);
                } else {
                    if (lastHit) {
                        knockShield(entity, 200);
                        playBarrageEndNoise(hitNumber);
                    } else {
                        entity.setVelocity(prevVelocity);

                        if (!this.self.getWorld().isClient()) {
                            this.self.getWorld().playSound(null, this.self.getBlockPos(), ModSounds.STAND_BARRAGE_BLOCK_EVENT, SoundCategory.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
                        }
                    }
                }
            }
        } else {
            playBarrageMissNoise(hitNumber);
        }
    } public void barrageImpact2(Entity entity, boolean lastHit, float knockbackStrength){
        if (entity instanceof LivingEntity){
            if (lastHit) {
                this.takeKnockbackWithY((LivingEntity) entity, knockbackStrength,
                        MathHelper.sin(this.self.getYaw() * ((float) Math.PI / 180)),
                        MathHelper.sin(this.self.getPitch() * ((float) Math.PI / 180)),
                        -MathHelper.cos(this.self.getYaw() * ((float) Math.PI / 180)));
            } else {
                this.takeKnockbackUp((LivingEntity) entity,knockbackStrength);
            }
        }
    }
    public void playBarrageMissNoise(int hitNumber){
        if (!this.self.getWorld().isClient()) {
            if (hitNumber%2==0) {
                this.self.getWorld().playSound(null, this.self.getBlockPos(), ModSounds.STAND_BARRAGE_MISS_EVENT, SoundCategory.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
            }
        }
    }
    public void playBarrageNoise(int hitNumber){
        if (!this.self.getWorld().isClient()) {
            if (hitNumber%2==0) {
                this.self.getWorld().playSound(null, this.self.getBlockPos(), ModSounds.STAND_BARRAGE_HIT_EVENT, SoundCategory.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
            }
        }
    } public void playBarrageNoise2(int hitNumber){
        if (!this.self.getWorld().isClient()) {
            if (hitNumber%2==0) {
                this.self.getWorld().playSound(null, this.self.getBlockPos(), ModSounds.STAND_BARRAGE_HIT2_EVENT, SoundCategory.PLAYERS, 0.95F, (float) (0.9 + (Math.random() * 0.25)));
            }
        }
    }
    public void playBarrageEndNoise(int hitNumber){
        if (!this.self.getWorld().isClient()) {
            if (hitNumber%2==0) {
                this.self.getWorld().playSound(null, this.self.getBlockPos(), ModSounds.STAND_BARRAGE_END_EVENT, SoundCategory.PLAYERS, 0.95F, 1f);
            }
        }
    }

    public void punchImpact(Entity entity){
        this.attackTimeDuring = -10;
        RoundaboutMod.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
            if (entity != null) {
                float pow;
                float knockbackStrength;
                if (this.activePowerPhase >= this.activePowerPhaseMax) {
                    /*The last hit in a string has more power and knockback if you commit to it*/
                    pow = getHeavyPunchStrength(entity);
                    knockbackStrength = 2F;
                } else {
                    pow = getPunchStrength(entity);
                    knockbackStrength = 0.5F;
                }
                if (StandDamageEntityAttack(entity, pow, knockbackStrength, this.self)) {
                } else {
                    if (this.activePowerPhase >= this.activePowerPhaseMax) {
                        knockShield(entity, 40);
                    }
                }
            } else {
                // This is less accurate raycasting as it is server sided but it is important for particle effects
                float distMax = this.getDistanceOut(this.self, this.standReach, false);
                float halfReach = (float) (distMax * 0.5);
                Vec3d pointVec = DamageHandler.getRayPoint(self, halfReach);
                if (!this.self.getWorld().isClient) {
                    ((ServerWorld) this.self.getWorld()).spawnParticles(ParticleTypes.EXPLOSION, pointVec.x, pointVec.y, pointVec.z,
                            1, 0.0, 0.0, 0.0, 1);
                }
            }

            SoundEvent SE;
            float pitch = 1F;
            if (this.activePowerPhase >= this.activePowerPhaseMax) {

                if (!this.self.getWorld().isClient()) {
                    SoundEvent LastHitSound = this.getLastHitSound();
                    if (LastHitSound != null) {
                        this.self.getWorld().playSound(null, this.self.getBlockPos(), LastHitSound,
                                SoundCategory.PLAYERS, 1F, 1);
                    }
                }

                if (entity != null) {
                    SE = ModSounds.PUNCH_4_SOUND_EVENT;
                    pitch = 1.2F;
                } else {
                    SE = ModSounds.PUNCH_2_SOUND_EVENT;
                }
            } else {
                if (entity != null) {
                    SE = ModSounds.PUNCH_3_SOUND_EVENT;
                    pitch = 1.1F + 0.07F * activePowerPhase;
                } else {
                    SE = ModSounds.PUNCH_1_SOUND_EVENT;
                }
            }

            if (!this.self.getWorld().isClient()) {
                this.self.getWorld().playSound(null, this.self.getBlockPos(), SE, SoundCategory.PLAYERS, 0.95F, pitch);
            }
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
        float tickDelta = 0;
        if (this.self.getWorld().isClient()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            tickDelta = mc.getLastFrameDuration();
        }
        Vec3d vec3d = User.getCameraPosVec(tickDelta);

        Vec3d vec3d2 = User.getRotationVec(1.0f);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
        float f = 1.0f;
        Box box = new Box(vec3d.x+reach, vec3d.y+reach, vec3d.z+reach, vec3d.x-reach, vec3d.y-reach, vec3d.z-reach);

        EntityHitResult entityHitResult = ProjectileUtil.raycast(User, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit() && !entity.isInvulnerable(), reach*reach);
        if (entityHitResult != null){
            Entity hitResult = entityHitResult.getEntity();
            if (hitResult.isAlive() && !hitResult.isRemoved()) {
                return hitResult;
            }
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
            if (target instanceof LivingEntity && knockbackStrength > 0) {
                ((LivingEntity) target).takeKnockback(knockbackStrength * 0.5f, MathHelper.sin(this.self.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(this.self.getYaw() * ((float) Math.PI / 180)));
            }
            return true;
        }
        return false;
    }

    public void takeKnockbackWithY(LivingEntity entity, double strength, double x, double y, double z) {

        if ((strength *= 1.0 - entity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) <= 0.0) {
            return;
        }
        entity.velocityModified = true;
        Vec3d vec3d2 = new Vec3d(x, y, z).normalize().multiply(strength);
        entity.setVelocity(- vec3d2.x,
                -vec3d2.y,
                - vec3d2.z);
        entity.velocityDirty = true;
    }


    public void takeKnockbackUp(LivingEntity entity, double strength) {
        if ((strength *= 1.0 - entity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) <= 0.0) {
            return;
        }
        entity.velocityDirty = true;
        Vec3d vec3d2 = new Vec3d(0, strength, 0).normalize().multiply(strength);
        entity.setVelocity(vec3d2.x,
                vec3d2.y,
                vec3d2.z);
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

    public void kickStartClient(){
        this.kickStarted = true;
    }

    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    public void tryPower(int move, boolean forced){
        if ((this.activePower == PowerIndex.NONE || forced) && !this.isDazed(this.self)){

            if (move == PowerIndex.NONE) {
                this.setPowerNone();
            } else if (move == PowerIndex.ATTACK) {
                this.setPowerAttack();
            } else if (move == PowerIndex.GUARD) {
                 this.setPowerGuard();
            } else if (move == PowerIndex.BARRAGE) {
                 this.setPowerBarrage();
            }
        }
        if (this.self.getWorld().isClient){
            kickStarted = false;
        }
    }
    public void setPowerNone(){
        this.attackTimeDuring = -1;
        this.setActivePower(PowerIndex.NONE);
        poseStand(OffsetIndex.FOLLOW);
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
        this.poseStand(OffsetIndex.GUARD);
    }
    public void setPowerBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE);
        this.poseStand(OffsetIndex.ATTACK);
        playBarrageGuardSound();
    }

    public void playBarrageGuardSound(){
        if (!this.self.getWorld().isClient()) {
            SoundEvent barrageChargeSound = this.getBarrageChargeSound();
            if (barrageChargeSound != null) {
                this.self.getWorld().playSound(null, this.self.getBlockPos(), barrageChargeSound,
                        SoundCategory.PLAYERS, 0.96F, 0.9F);
            }
        }
    }

    public boolean isGuarding(){
        return this.activePower == PowerIndex.GUARD;
    }
    public boolean isBarraging(){
        return this.activePower == PowerIndex.BARRAGE;
    }

    public int getBarrageWindup(){
        return 22;
    }
    public int getBarrageLength(){
        return 60;
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
                this.setActivePower(PowerIndex.ATTACK);
                this.setAttackTime(0);

                poseStand(OffsetIndex.ATTACK);
            }
        }
    }

    public void syncCooldowns(){
        if (!this.self.getWorld().isClient && this.self instanceof ServerPlayerEntity){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(attackTime);

            buf.writeInt(attackTimeMax);
            buf.writeInt(attackTimeDuring);
            buf.writeByte(activePower);
            buf.writeByte(activePowerPhase);
            ServerPlayNetworking.send((ServerPlayerEntity) this.self, ModMessages.POWER_COOLDOWN_SYNC_ID, buf);
        }
    }
}
