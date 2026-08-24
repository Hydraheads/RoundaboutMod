package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class SilverChariotRapierShotEntity extends AbstractArrow implements UnburnableProjectile {
    private static final EntityDataAccessor<Integer> ROUNDABOUT$BOUNCES = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> ROUNDABOUT$TYPE = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> SKIN = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SilverChariotRapierShotEntity.class, EntityDataSerializers.INT);

    public SilverChariotRapierShotEntity(EntityType<? extends SilverChariotRapierShotEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected SilverChariotRapierShotEntity(EntityType<? extends SilverChariotRapierShotEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    public SilverChariotRapierShotEntity(LivingEntity $$0, Level $$1) {
        this(ModEntities.SILVER_CHARIOT_RAPIER, $$0.getX(), $$0.getY(), $$0.getZ(), $$1);
    }

    public LivingEntity standUser;
    public UUID standUserUUID;

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

    public void setUser(LivingEntity user) {
        standUser = user;
        this.getEntityData().set(USER_ID, user.getId());
        if (!this.level().isClientSide()){
            standUserUUID = user.getUUID();
        }
    }

    public int getBounces() {
        if (this.getEntityData().hasItem(ROUNDABOUT$BOUNCES)) {
            this.getEntityData().get(ROUNDABOUT$BOUNCES);
        }
        return 0;
    }

    public void setBounces(int bounces) {
        if (this.getEntityData().hasItem(ROUNDABOUT$BOUNCES)) {
            this.getEntityData().set(ROUNDABOUT$BOUNCES, bounces);
        }
    }

    public byte getRapierShotType() {
        if (this.getEntityData().hasItem(ROUNDABOUT$TYPE)) {
            return this.getEntityData().get(ROUNDABOUT$TYPE);
        }
        return BASE;
    }

    public void setRapierShotType(byte type) {
        if (this.getEntityData().hasItem(ROUNDABOUT$TYPE)) {
            this.getEntityData().set(ROUNDABOUT$TYPE, type);
        }
    }

    public byte getSkin() {
        return this.getEntityData().get(SKIN);
    }
    public void setSkin(byte skin) {
        this.getEntityData().set(SKIN, skin);
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        // super.onHitBlock($$0);
        if (!this.level().isClientSide()) {
            if (this.getRapierShotType() == PLATFORM) {

            } else if (this.getRapierShotType() == BASE && this.getBounces() > 0) {
                this.setBounces(this.getBounces() - 1);

                Vec3 velocity = this.getDeltaMovement();
                Direction hitDir = $$0.getDirection();
                Vec3 normal = Vec3.atLowerCornerOf(hitDir.getNormal());

                Vec3 reflected = velocity.subtract(normal.scale(2 * velocity.dot(normal)));
                // reflected = reflected.scale(1.0);

                this.setDeltaMovement(reflected);

                Vec3 hitLoc = $$0.getLocation();
                Vec3 pushOut = normal.scale(0.2);
                this.setPos(hitLoc.x + pushOut.x, hitLoc.y + pushOut.y, hitLoc.z + pushOut.z);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        // super.onHitEntity($$0);

        Entity $$1 = $$0.getEntity();

        if ($$1.equals(this.getOwner())) {
            return;
        }

        if (this.getOwner() instanceof TamableAnimal TT && TT.getOwner() != null) {
            if ($$1 instanceof TamableAnimal TA && TA.getOwner() != null && TT.getOwner().is(TA.getOwner())) {
                return;
            }
            if ($$1.is(TT.getOwner())) {
                return;
            }
        }

        float degrees = MainUtil.getLookAtEntityYaw(this, $$1);
        float force = 2.0F;

        Entity $$4 = this.getOwner();
        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.STAND);
        if (this.getOwner() != null) {
            $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.STAND, this, this.getOwner());
        }

        float damage = 1.0f;
    }

    public static void damageEntity(Entity gotten, Entity proj, LivingEntity user, PowersSilverChariot PSC, float multi) {

    }

    public void getEntity(Entity gotten, PowersSilverChariot PSC) {

    }

    public static final byte
                BASE = (byte) 1,
                PLATFORM = (byte) 2;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return super.canBeHitByProjectile();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    private int life = 0;

    @Override
    public void tick() {
        super.tick();
        this.life += 1;

        if (this.getRapierShotType() == BASE) {
            if (this.life > 600) {
                this.discard();
            }
        } else if (this.getRapierShotType() == PLATFORM) {
            if (this.life > 1200) {
                this.discard();
            }
        }
    }
}
