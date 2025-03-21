package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.entity.client.PreRenderEntity;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PowersMagiciansRed;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrossfireHurricaneEntity extends AbstractHurtingProjectile implements PreRenderEntity, UnburnableProjectile {
    public CrossfireHurricaneEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected CrossfireHurricaneEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(CrossfireHurricaneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CROSS_NUMBER = SynchedEntityData.defineId(CrossfireHurricaneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(CrossfireHurricaneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_SIZE = SynchedEntityData.defineId(CrossfireHurricaneEntity.class, EntityDataSerializers.INT);

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

    @Override
    public boolean isInWater() {
        return false;
    }

    public boolean isEffectivelyInWater() {
        return this.wasTouchingWater;
    }
    @Override
    protected float getInertia() {
        return 1F;
    }
    @Override
    public boolean isControlledByLocalInstance() {
        /**
        if (this.getStandUser() != null && ((StandUser)this.getStandUser()).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
            if (this.getCrossNumber() > 0) {
                return true;
            }
        }
         **/
        return super.isControlledByLocalInstance();
    }

    @Override
    public boolean isPickable() {
        return false;
    }


    public void setOldPosAndRot2() {
        if (storeVec != null) {
            double $$0 = storeVec.x();
            double $$1 = storeVec.y();
            double $$2 = storeVec.z();
            this.xo = $$0;
            this.yo = $$1;
            this.zo = $$2;
            this.xOld = $$0;
            this.yOld = $$1;
            this.zOld = $$2;
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }

    public double renderRotation = 0;
    public double lastRenderRotation = 0;

    public void setRenderRotation(double rotation){
        lastRenderRotation = renderRotation;
        renderRotation = rotation;
    }
    public Vec3 storeVec;
    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
        }
    }
    public boolean initialized = false;
    public int getSize() {
        return this.getEntityData().get(SIZE);
    }
    public void setSize(int idd) {
        this.getEntityData().set(SIZE, idd);
    }
    public int getMaxSize() {
        return this.getEntityData().get(MAX_SIZE);
    }
    public void setMaxSize(int idd) {
        this.getEntityData().set(MAX_SIZE, idd);
    }

    public float getRenderSize() {
        return renderSize;
    }
    public void setRenderSize(float renderSize) {
        this.renderSize = renderSize;
    }
    public float getLastRenderSize() {
        return lastRenderSize;
    }
    public void setLastRenderSize(float renderSize) {
        this.lastRenderSize = renderSize;
    }
    public float renderSize = 0;
    public float lastRenderSize = 0;
    public int getCrossNumber() {
        return this.getEntityData().get(CROSS_NUMBER);
    }
    public void setCrossNumber(int idd) {
        this.getEntityData().set(CROSS_NUMBER, idd);
    }
    protected CrossfireHurricaneEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public boolean isBundle = false;
    public boolean isTickable = false;
    public void actuallyTick(){
        isTickable = true;
        tick();
        isTickable = false;
    }
    public int saneAgeTicking;
    public void tick() {
        boolean client = this.level().isClientSide();
        if (!client){
            if (isEffectivelyInWater()){
                this.discard();
            }
        }

        if (this.getStandUser() != null && ((StandUser)this.getStandUser()).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){

            if (this.getCrossNumber() > 0){
                this.setDeltaMovement(Vec3.ZERO);
                if (client){

                    if (!initialized){
                        if (this.getCrossNumber() < 5) {
                            initialized = true;
                            PMR.addHurricaneSpecial(this);
                        } else {
                            PMR.hurricane = this;
                        }
                    }
                }
                if (!isTickable) {
                return;
                }
            }
        }
        if (client && this.saneAgeTicking != this.tickCount){
            if (lastRenderSize < getMaxSize()) {
                lastRenderSize += getAccrualRate();
            } else {
                lastRenderSize = getMaxSize();
            }
            if (!ClientUtil.checkIfGamePaused()) {
                int ticks = ConfigManager.getClientConfig().particleSettings.cfhTicksPerFlameParticle;
                if (ticks > -1 && this.tickCount % ticks == 0)
                    //for (int i = 0; i < 1; i++) {
                    this.level()
                            .addParticle(
                                    ModParticles.ORANGE_FLAME,
                                    this.getRandomX(0.26),
                                    this.getRandomY(0.26) + this.getBbHeight() * 0.55,
                                    this.getRandomZ(0.26),
                                    0,
                                    0,
                                    0
                            );
                //}
            }
        } else {
        }
        saneAgeTicking = this.tickCount;
        super.tick();
        if (!client){
            if (isEffectivelyInWater()){
                this.discard();
            }
        }
    }


    public double getRandomY(double $$0) {
        return this.getY((2.0 * this.random.nextDouble() - 1.0) * $$0);
    }
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (this.getCrossNumber() <= 0){
            //this is where ankh go boom boom
            radialExplosion(null);

            this.discard();
        }
    }
    public void shootFromRotationDeltaAgnostic(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
    }
    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (this.getCrossNumber() <= 0){
            Entity $$1 = $$0.getEntity();
            if (getUserID() != $$1.getId() && !($$1 instanceof MagiciansRedEntity)) {
                radialExplosion(null);
                this.discard();
            }
        }
    }
    public void radialExplosion(LivingEntity mainTarget){
        if (!this.level().isClientSide()){
            LivingEntity user = this.getStandUser();
            if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                this.level().playSound(null, this.blockPosition(), ModSounds.CROSSFIRE_EXPLODE_EVENT,
                        SoundSource.PLAYERS, 2F, 1F);
                ((ServerLevel) this.level()).sendParticles(PMR.getFlameParticle(), this.getX(),
                        this.getY(), this.getZ(),
                        200,
                        0.01, 0.01, 0.01,
                        0.1);
                if (mainTarget != null) {
                        getEntity(mainTarget, true, PMR);
                }

                List<Entity> entityList = DamageHandler.genHitbox(user, this.getX(), this.getY(),
                        this.getZ(), 2, 2, 2);
                if (!entityList.isEmpty()){
                    for (Entity value : entityList) {
                        if (!(mainTarget != null && value.is(mainTarget))){
                            getEntity(value, false, PMR);
                        }
                    }
                }
            }
        }
    }

    public void getEntity(Entity gotten, boolean direct,PowersMagiciansRed PMR){
        float dmg = 1;
        float strength = 0.91F;
        if (direct){
            dmg = PMR.getHurricaneDirectDamage(gotten,this);
            strength*=2;
        } else {
            dmg = PMR.getHurricaneDamage(gotten,this);

        }

        if (gotten.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.CROSSFIRE, this.standUser),
                dmg)) {
            float degrees = MainUtil.getLookAtEntityYaw(this,gotten);
            MainUtil.takeUnresistableKnockbackWithY(gotten, strength,
                    Mth.sin(degrees * ((float) Math.PI / 180)),
                    Mth.sin(-40 * ((float) Math.PI / 180)),
                    -Mth.cos(degrees * ((float) Math.PI / 180)));
            if (gotten instanceof LivingEntity LE) {
                ((StandUser) LE).roundabout$setOnStandFire((byte) 1, standUser);
                ((StandUser) LE).roundabout$setSecondsOnStandFire(10);
            }
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }



    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(USER_ID, -1);
        this.entityData.define(SIZE, 0);
        this.entityData.define(CROSS_NUMBER, 0);
        this.entityData.define(MAX_SIZE, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        super.addAdditionalSaveData($$0);
        if (standUser != null) {
            $$0.putUUID("standUser", standUser.getUUID());
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        super.readAdditionalSaveData($$0);
        if ($$0.hasUUID("standUser")) {
            standUserUUID = $$0.getUUID("standUser");
            if (!this.level().isClientSide()) {
                Entity ett = ((ServerLevel) this.level()).getEntity(standUserUUID);
                if (ett instanceof LivingEntity lett) {
                    standUser = lett;
                    this.setUserID(lett.getId());
                }
            }
        }
    }
    public LivingEntity getStandUser(){
        if (standUser != null){
            return standUser;
        } else if (standUserUUID != null && !this.level().isClientSide()){
            Entity ett = ((ServerLevel)this.level()).getEntity(standUserUUID);
            if (ett instanceof LivingEntity lett){
                standUser = lett;
                this.setUserID(lett.getId());
            }
        } else if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            standUser = LE;
        }
        return standUser;
    }

    public int getAccrualRate(){
        return 1;
    }
    public boolean preRender(Entity ent, double $$1, double $$2, double $$3, float $$4, PoseStack pose, MultiBufferSource $$6){
        if (ent instanceof CrossfireHurricaneEntity cfhe) {
            LivingEntity user = cfhe.getStandUser();
            if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                if (cfhe.getCrossNumber() > 0) {
                    if (cfhe.getCrossNumber() < 5) {
                        if (PMR.hurricaneSpecial == null) {
                            PMR.hurricaneSpecial = new ArrayList<>();
                        }
                        List<CrossfireHurricaneEntity> hurricaneSpecial2 = new ArrayList<>(PMR.hurricaneSpecial) {
                        };
                        if (!hurricaneSpecial2.isEmpty()) {
                            PMR.spinint = PMR.lastSpinInt + ($$4 * PMR.maxSpinint);
                            int totalnumber = hurricaneSpecial2.size();
                            double lerpX = (user.getX() * $$4) + (user.xOld * (1.0f - $$4));
                            double lerpY = (user.getY() * $$4) + (user.yOld * (1.0f - $$4));
                            double lerpZ = (user.getZ() * $$4) + (user.zOld * (1.0f - $$4));
                            PMR.transformHurricane(cfhe, totalnumber, lerpX,
                                    lerpY, lerpZ, getRenderSize());
                        }
                    } else {

                        double lerpX = (user.getX() * $$4) + (user.xOld * (1.0f - $$4));
                        double lerpY = (user.getY() * $$4) + (user.yOld * (1.0f - $$4));
                        double lerpZ = (user.getZ() * $$4) + (user.zOld * (1.0f - $$4));
                        PMR.transformHurricane(cfhe, 1, lerpX,
                                lerpY, lerpZ, getRenderSize());
                    }
                }
            }
        }
        return false;
    }
    @Override

    protected boolean shouldBurn() {
        return false;
    }


}
