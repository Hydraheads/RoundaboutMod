package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.NoVibrationEntity;
import net.hydra.jojomod.access.PenetratableWithProjectile;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class StrayCatAirBubble extends AbstractHurtingProjectile implements UnburnableProjectile, NoVibrationEntity, PenetratableWithProjectile {
    public StrayCatAirBubble(EntityType<? extends StrayCatAirBubble> $$0, Level $$1) {
        super($$0, $$1);
        this.lifeSpan = 300;
    }

    /*public StrayCatAirBubble(LivingEntity living, Level $$1) {
        super(ModEntities.STRAY_CAT_AIRBUBBLE, $$1);
        setOwner(living);
        //places = false;
    }*/
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(StrayCatAirBubble.class, EntityDataSerializers.INT);

    public int lifeSpan = 300;
    public LivingEntity standUser;
    public UUID standUserUUID;
    private static final EntityDataAccessor<Boolean> ACTIVATED = SynchedEntityData.defineId(StrayCatAirBubble.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAUNCHED = SynchedEntityData.defineId(StrayCatAirBubble.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(StrayCatAirBubble.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> SKIN = SynchedEntityData.defineId(StrayCatAirBubble.class, EntityDataSerializers.BYTE);


    public boolean getActivated() {
        return this.getEntityData().get(ACTIVATED);
    }
    public void setActivated(boolean activ) {
        this.getEntityData().set(ACTIVATED, activ);
    }
    public boolean getLaunched() {
        return this.getEntityData().get(LAUNCHED);
    }
    public void setLaunched(boolean activ) {
        this.getEntityData().set(LAUNCHED, activ);
    }
    public float getSped() {
        return this.getEntityData().get(SPEED);
    }
    public void setSped(float sped) {
        this.getEntityData().set(SPEED, sped);
    }
    public byte getSkin() {
        return this.getEntityData().get(SKIN);
    }
    public void setSkin(byte skin) {
        this.getEntityData().set(SKIN, skin);
    }


    static final float damagePoints = 2.5f;

    public float getDamagePoints() {
        return damagePoints;
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (this.isInvulnerableTo($$0)) {
            return false;
        } else {

            //this.popBubble();
        }
        return false;

        //return true;
    }

    public boolean hasTimeLimit = true;
    public void setHasTimeLimit(boolean value) {
        this.hasTimeLimit = value;
    }

    public boolean isKillerQueenBubble = false;
    public void setIsKQAirBubble(boolean value) {
        this.isKillerQueenBubble = value;
    }

    public boolean isPlanted = false;
    public void setIsPlanted(boolean value) {
        this.isPlanted = value;
    }

    public boolean followOwnerView = false;
    public void setFollowOwnerView(boolean value) {this.followOwnerView = value;}

    @Override
    public boolean dealWithPenetration(Entity proj){
        popBubble();
        return true;
    }


    /*protected StrayCatAirBubble(EntityType<? extends StrayCatAirBubble> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }*/

    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }
    public void setUserID(int idd) {
        this.getEntityData().set(USER_ID, idd);
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            this.standUser = LE;
            if (!this.level().isClientSide()){
                standUserUUID = LE.getUUID();
            }
        }
    }

    public void tick() {
        if (!this.level().isClientSide()) {

            if (this.hasTimeLimit) {
                this.lifeSpan--;
            }

            if (this.lifeSpan <= 0 || this.getOwner() == null || !this.getOwner().isAlive() || this.getOwner().isRemoved() ||
                    this.getOwner().distanceTo(this) > getDistanceUntilPopping() && distancePops()){
                popBubble();
                return;
            }

            if (this.followOwnerView && !(((StandUser)this.getOwner()).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ
                    && this.isKillerQueenBubble && PKQ.isPiloting())) {

                Entity owner = this.getOwner();
                this.shootFromRotationDeltaAgnostic2(owner, owner.getXRot(), owner.getYRot(), 1.0F, getSped());
            }

        }else if( this.tickCount % 30 == 9) {
            this.level().addAlwaysVisibleParticle(ModParticles.AIR_CRACKLE, true,
                    this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(),
                    0, 0, 0);
        }
        super.tick();
    }

    public boolean distancePops(){
        return true;
    }
    public int getDistanceUntilPopping(){
        return ClientNetworking.getAppropriateConfig().killerQueenSettings.maxAirBubbleTravelDistanceBeforePopping;
    }

    /**No sculker noises*/
    @Override
    public boolean getVibration(){
        return false;
    }
    public void setUser(LivingEntity User) {
        standUser = User;

        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
        }
    }

    public boolean canSeeBubble(Entity ent) {
        float dist = this.distanceTo(ent);

        return dist < 6.0f || this.ownedBy(ent);
    }

    /**Bubbles Don't alert skulk at all*/
    @Override
    public void gameEvent(GameEvent $$0, @Nullable Entity $$1) {
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }
    public static float eWidth=0.4f;
    public static float eHeight=0.4f;
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(eWidth, eHeight); // Width, Height
    }
    @Override
    protected float getInertia() {
        return 1F;
    }
    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }
    @Override
    public boolean isPickable() {
        return false;
    }
    @Override
    public boolean isInWater() {
        return false;
    }

    protected void doPostHurtEffects(LivingEntity $$0) {
    }

    /*public boolean isEffectivelyInWater() {
        return this.wasTouchingWater;
    }*/

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (this.getOwner() != null && ((StandUser) this.getOwner()).roundabout$getStandPowers() instanceof PowersKillerQueen KQ && this.isKillerQueenBubble && KQ.detonateTimer > -1) {
            KQ.explode();
        } else {
            popBubble();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity target = $$0.getEntity();
        Entity user = this.getOwner();

        if ((user != null && target.is(user)) || (user != null && target instanceof StandEntity SE && user.is(SE.getUser()))) {
            return;
        }
        if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersKillerQueen KQ && this.isKillerQueenBubble && KQ.bubbleTarget != null) {
            if (!KQ.bubbleTarget.is(target) && !KQ.isContactModeEnabled()) {
                return;
            }
        }

        super.onHitEntity($$0);

        DamageSource dmg = ModDamageTypes.of(target.level(), ModDamageTypes.STAND);
        float damage = getDamagePoints();

        if (user != null) {
            dmg = ModDamageTypes.of(target.level(), ModDamageTypes.STAND, this, user);

            if (((StandUser) user).roundabout$getStandPowers() instanceof PowersKillerQueen KQ && this.isKillerQueenBubble) {
                damage = KQ.getAirBubbleDamage(target);
            }
        }


        if (target.hurt(dmg,damage)) {

            if (user instanceof LivingEntity LE) {

                if (((StandUser) user).roundabout$getStandPowers() instanceof PowersKillerQueen KQ && this.isKillerQueenBubble) {
                    if (target instanceof LivingEntity l) {
                        KQ.addEXP(4, l);
                    }
                }

                LE.setLastHurtMob(target);
            }

            if (target.getType() == EntityType.ENDERMAN) { return; }

            if (target instanceof LivingEntity || (target instanceof EnderDragonPart)) {
                LivingEntity $$7;
                if (target instanceof LivingEntity L) {
                    $$7 = L;
                } else {
                    $$7 = ((EnderDragonPart) target).parentMob;
                }

                Vec3 launchVec = this.getDeltaMovement();
                Vec3 vec3d2 = launchVec.normalize().scale(0.6F);
                vec3d2 = vec3d2.add(0, 0.4F, 0);

                MainUtil.takeLiteralUnresistableKnockbackWithY(target,
                        vec3d2.x,
                        vec3d2.y,
                        vec3d2.z);

                if (user instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects($$7, user);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) user, $$7);
                }
                if (user instanceof LivingEntity LE && this.isPlanted) {
                    if (((StandUser) LE).roundabout$getStandPowers() instanceof PowersKillerQueen KQ && this.isKillerQueenBubble) {
                        KQ.bubbleContacted(target);
                    }
                }

                this.doPostHurtEffects($$7);
            }
        }

        popBubble();
    }

    public void popBubble(){
        this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                SoundSource.PLAYERS, 2F, (float)(0.98+(Math.random()*0.04)));
        if (!this.level().isClientSide()){
            if (this.tickCount % 40 == 9) {
                this.level().addAlwaysVisibleParticle(ModParticles.AIR_CRACKLE, true,
                        this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(),
                        0, 0, 0);
            }
        }

        this.discard();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ACTIVATED, false);
        this.entityData.define(LAUNCHED, false);
        this.entityData.define(USER_ID, -1);
        this.entityData.define(SPEED, 1F);
        this.entityData.define(SKIN, (byte)0);
    }

    @Override
    public void shoot(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = (new Vec3($$0, $$1, $$2)).normalize().add(this.random.triangle((double)0.0F, 0.0172275 * (double)$$4), this.random.triangle((double)0.0F, 0.0172275 * (double)$$4), this.random.triangle((double)0.0F, 0.0172275 * (double)$$4)).scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotationDeltaAgnosticR(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
    }

    public void shootFromRotationDeltaAgnostic(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection != Direction.DOWN) {
            Vec2 vecMagic = RotationUtil.rotPlayerToWorld($$0.getYRot(), $$0.getXRot(), gravityDirection);
            $$1 = vecMagic.y; $$2 = vecMagic.x;
        }

        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
    }
    public void shootFromRotationDeltaAgnostic2(Entity $$0, float $$1, float $$2, float $$3, float $$4) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection != Direction.DOWN) {
            Vec2 vecMagic = RotationUtil.rotPlayerToWorld($$0.getYRot(), $$0.getXRot(), gravityDirection);
            $$1 = vecMagic.y; $$2 = vecMagic.x;
        }

        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot2((double)$$6, (double)$$7, (double)$$8, $$4);
    }
    public void shootFromRotationDeltaAgnostic3(Entity $$0, float xrot, float yrot, float $$3, float $$4) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection != Direction.DOWN) {
            Vec2 vecMagic = RotationUtil.rotPlayerToWorld($$0.getYRot(), $$0.getXRot(), gravityDirection);
            xrot = vecMagic.y; yrot = vecMagic.x;
        }

        float $$6 = -Mth.sin(yrot * (float) (Math.PI / 180.0)) * Mth.cos(xrot * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin((xrot + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos(yrot * (float) (Math.PI / 180.0)) * Mth.cos(xrot * (float) (Math.PI / 180.0));
        this.shoot4((double)$$6, (double)$$7, (double)$$8, $$4);
    }

    public void shoot2(double $$0, double $$1, double $$2, float $$3) {
        Vec3 $$5 = (new Vec3($$0, $$1, $$2)).normalize();
        $$5 = $$5.add(this.getDeltaMovement()).normalize();
        $$5 = $$5.add(this.getDeltaMovement()).normalize();
        $$5 = $$5.add(this.getDeltaMovement()).normalize();
        $$5 = $$5.add(this.getDeltaMovement()).normalize();
        $$5 = $$5.scale($$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        if (!this.level().isClientSide()) {
            this.setYRot((float) (Mth.atan2($$5.x, $$5.z) * 180.0F / (float) Math.PI));
            this.setXRot((float) (Mth.atan2($$5.y, $$6) * 180.0F / (float) Math.PI));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }
    public void shoot3(double $$0, double $$1, double $$2, float $$3) {
        Vec3 $$5 = (new Vec3($$0, $$1, $$2)).reverse().normalize();
        $$5 = $$5.add(this.getDeltaMovement()).normalize();
        $$5 = $$5.add(this.getDeltaMovement()).normalize();
        $$5 = $$5.add(this.getDeltaMovement()).normalize();
        $$5 = $$5.add(this.getDeltaMovement()).normalize();
        $$5 = $$5.scale($$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        if (!this.level().isClientSide()) {
            this.setYRot((float) (Mth.atan2($$5.x, $$5.z) * 180.0F / (float) Math.PI));
            this.setXRot((float) (Mth.atan2($$5.y, $$6) * 180.0F / (float) Math.PI));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }

    public void shoot4(double $$0, double $$1, double $$2, float $$3) {
        Vec3 $$5 = (new Vec3($$0, $$1, $$2)).normalize();
        $$5 = $$5.scale($$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        if (!this.level().isClientSide()) {
            this.setYRot((float) (Mth.atan2($$5.x, $$5.z) * 180.0F / (float) Math.PI));
            this.setXRot((float) (Mth.atan2($$5.y, $$6) * 180.0F / (float) Math.PI));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }
}
