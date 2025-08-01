package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FallenPhantom extends FallenMob implements PlayerRideableJumping {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(FallenPhantom.class, EntityDataSerializers.BYTE);

    public final float changeHeightBy = 0.4f;
    public final float verticalSpeed = 0.1f;
    private final float slowSpeed = 0.04f;
    private final float nonDrivenSpeed = 0.3f;
    public float yaccel = 0f;

    public FallenPhantom(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.1).add(Attributes.MAX_HEALTH, 6)
                .add(Attributes.ATTACK_DAMAGE, 3).
                add(Attributes.FOLLOW_RANGE, 48.0D).add(Attributes.FLYING_SPEED,0.1);
    }


    @Override
    protected void playStepSound(BlockPos $$0, BlockState $$1) {

    }

    @Override
    public boolean isNoGravity() {
        boolean veh = this.isVehicle() && !this.getPassengers().isEmpty();
        if(veh) {
            if (!(this.level().getDayTime() % 24000L >= 13000)) {
                return false;
            }
        }
        return this.getActivated();
    }

    @Override
    public String getData() {
        return "phantom";
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }


    @Nullable
    public LivingEntity getControllingPassenger() {
        if (!this.getPassengers().isEmpty()) {
            Entity entity = this.getPassengers().get(0);
            if (entity instanceof LivingEntity && this.getActivated()
            && this.getController() == entity.getId()) {
                return (LivingEntity)entity;
            }
        }

        return null;
    }

    @Override

    public float modX(){
        return 2F;
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.addBehaviourGoals();
    }

    @Override
    protected float getStandingEyeHeight(Pose $$0, EntityDimensions $$1) {
        return $$1.height * 0.35f;
    }
    @Override
    public boolean onClimbable() {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick() {
        if(!this.getActivated()){
            for(Entity ent : this.getPassengers()){
                ent.unRide();
            }
            //Drop this dude
            if(!this.onGround() && this.level().getBlockState(new BlockPos((int) this.getX(),(int) (this.getY()-0.1),(int) this.getZ())).isAir() && !this.level().getBlockState(new BlockPos((int) this.getX(),(int) (this.getY()-0.1),(int) this.getZ())).isSolid() && this.getPassengers().isEmpty()){
                this.moveRelative(0.4f,new Vec3(0,-verticalSpeed,0));
            }
        } else {
            if (!this.level().isClientSide()){
                /**Tick into the second mount message on the actionbar*/
                sendOutMessage();
            }
        }
        if (this.level().isClientSide && this.getActivated()) {
            float f = Mth.cos((float)(this.getUniqueFlapTickOffset() + this.tickCount) * 7.448451f * ((float)Math.PI / 180) + (float)Math.PI);
            float g = Mth.cos((float)(this.getUniqueFlapTickOffset() + this.tickCount + 1) * 7.448451f * ((float)Math.PI / 180) + (float)Math.PI);
            if (f > 0.0f && g <= 0.0f) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95f + this.random.nextFloat() * 0.05f, 0.95f + this.random.nextFloat() * 0.05f, false);
            }
            int i = 1;
            float h = Mth.cos(this.getYRot() * ((float)Math.PI / 180)) * (1.3f + 0.21f * (float)i);
            float j = Mth.sin(this.getYRot() * ((float)Math.PI / 180)) * (1.3f + 0.21f * (float)i);
            float k = (0.3f + f * 0.45f) * ((float)i * 0.2f + 1.0f);
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + (double)h, this.getY() + (double)k, this.getZ() + (double)j, 0.0, 0.0, 0.0);
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - (double)h, this.getY() + (double)k, this.getZ() - (double)j, 0.0, 0.0, 0.0);
        }
        if(this.navigation.isInProgress()) {
            float targetY = this.navigation.getTargetPos().getY();
            if(this.getTarget() != null){
                targetY += (float) (this.getTarget().getBoundingBox().getYsize()/2);
            }
            if (targetY < this.getY()) {
                if (!this.level().getBlockState(new BlockPos((int) this.getX(), (int) (this.getY() - verticalSpeed), (int) this.getZ())).isSolid()) {
                    //this.setPos(this.getX(),this.getY()-verticalSpeed,this.getZ());
                    this.moveRelative(slowSpeed, new Vec3(0, -verticalSpeed, 0));
                }

            } else if (targetY > this.getY()) {
                if (!this.level().getBlockState(new BlockPos((int) this.getX(), (int) (this.getY() + verticalSpeed), (int) this.getZ())).isSolid()) {
                    //this.setPos(this.getX(),this.getY()+verticalSpeed,this.getZ());
                    this.moveRelative(slowSpeed, new Vec3(0, verticalSpeed * 2, 0));
                }

            }
        }
        super.tick();

    }


    @Override
    protected float getRiddenSpeed(Player $$0) {
        if(this.level().getDayTime() % 24000L < 13000) {
            return (float) ( this.getAttributeValue(Attributes.FLYING_SPEED) * 0.2);
        } else{
            float spd =(float) ((float) this.getAttributeValue(Attributes.FLYING_SPEED));

            return spd;

        }
    }


    public double getCustomJump() {
        return 0.5F;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public void aiStep() {
        if(navigation.isInProgress() && !navigation.isDone()) {
            if (new Vec2(navigation.getTargetPos().getX(),navigation.getTargetPos().getZ()).distanceToSqr(new Vec2((float) this.position().x, (float) this.position().z)) > 1) {
                this.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(navigation.getTargetPos().getX(), navigation.getTargetPos().getY(), navigation.getTargetPos().getZ()));
            }
        }
        super.aiStep();
    }
    @Override
    public void travel(Vec3 vec3) {

        if (!this.getActivated()){
            super.travel(vec3);
            return;
        } else {
            boolean veh = this.isVehicle() && !this.getPassengers().isEmpty();
            if(veh) {
                if (!(this.level().getDayTime() % 24000L >= 13000)) {
                    super.travel(vec3);
                    return;
                }
            }
        }
        if(navigation.isInProgress() && navigation.isStuck() && getTarget() == null){
            navigation.stop();
        }

        if(navigation.isInProgress() && !navigation.isDone() && Math.abs(this.getX() - navigation.getTargetPos().getX()) <= 0.4 && Math.abs(this.getY() - navigation.getTargetPos().getY()) <= 0.4 && Math.abs(this.getZ() - navigation.getTargetPos().getZ()) <= 0.4  ) {
            navigation.stop();
            this.setDeltaMovement(0,0,0);

        }
        if (this.isControlledByLocalInstance()) {
                float f = 0.91f;
                boolean inWA = isInWaterOrBubble() || isInLava();
                if (inWA){
                    f*=0.8F;
                }
                float yboost = 0.1f;
                //if (this.onGround()) {
                //    f = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91f;
                //}
                boolean veh = this.isVehicle() && !this.getPassengers().isEmpty();
                if(veh) {
                    if(this.level().getDayTime() % 24000L >= 13000) {
                        f = ClientNetworking.getAppropriateConfig().justiceSettings.phantomCorpseSpeed;
                        if (inWA){
                            f*=0.7F;
                        }
                        float spd = (float)this.getAttributeValue(Attributes.FLYING_SPEED);
                        this.moveRelative(spd, vec3);
                        this.setDeltaMovement(this.getDeltaMovement().add(0,vec3.y*yboost,0));
                    } else{
                        this.moveRelative( slowSpeed,vec3);
                    }
                } else{
                    if (navigation.isInProgress()) {
                        yboost = 0.5f;
                        this.moveRelative(nonDrivenSpeed, vec3);
                        this.setDeltaMovement(this.getDeltaMovement().add(0, vec3.y * yboost, 0));
                    }
                }
                if (veh || navigation.isInProgress()) {
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(f));
                }
        }
        this.calculateEntityAnimation(false);
    }

    @Override
    protected void tickRidden(Player $$0, Vec3 $$1) {
        super.tickRidden($$0, $$1);
        Vec2 $$2 = this.getRiddenRotation($$0);
        this.setRot($$2.y, $$2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if(this.isControlledByLocalInstance() && this.level().isClientSide){

        }

    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getEyeHeight();
    }

    @Override
    protected PathNavigation createNavigation(Level $$0) {
        FlyingPathNavigation fpath = new FlyingPathNavigation(this,$$0);
        fpath.setCanOpenDoors(false);
        fpath.setCanFloat(true);
        fpath.setCanPassDoors(false);
        return fpath;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
    }


    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getActivated()) {
            return SoundEvents.PHANTOM_AMBIENT;
        } else {
            return super.getAmbientSound();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        if (this.getActivated()) {
            return SoundEvents.PHANTOM_HURT;
        } else {
            return super.getHurtSound($$0);
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (this.getActivated()) {
            return SoundEvents.PHANTOM_DEATH;
        } else {
            return super.getDeathSound();
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    protected float playerJumpPendingScale;

    @Override
    public void onPlayerJump(int var1) {

    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public void handleStartJump(int $$0) {
    }

    @Override
    public void handleStopJump() {

    }


    protected Vec2 getRiddenRotation(LivingEntity $$0) {
        return new Vec2($$0.getXRot() * 0.5F, $$0.getYRot());
    }

    @Override
    protected Vec3 getRiddenInput(Player $$0, Vec3 $$1) {
        float $$2 = $$0.xxa;
        float $$3 = yaccel;
        float $$4 = $$0.zza * 2;
        if ($$4 <= 0.0F) {
            $$4 *= 0.25F;
        }

        if($$0.level().dimension() != Level.OVERWORLD || this.level().getDayTime() % 24000L < 13000){
            $$2 = 0;
            $$3 = -verticalSpeed;
            $$4 = 0;
        } else if (this.onGround()){
            $$2 = 0;
            $$4 = 0;
        }

        return new Vec3((double) $$2, $$3, (double) $$4);
    }

    int rideLengthTicks = 0;
    public void sendOutMessage(){
        Entity cpas = this.getControllingPassenger();
        if (cpas instanceof ServerPlayer SP){
            rideLengthTicks++;
            if (rideLengthTicks == 40){
                if(this.level().dimension() != Level.OVERWORLD){
                    SP.displayClientMessage(Component.translatable("text.roundabout.riding_flying_creature.dimension").withStyle(ChatFormatting.YELLOW), true);
                } else if (this.level().getDayTime() % 24000L < 13000){
                    SP.displayClientMessage(Component.translatable("text.roundabout.riding_flying_creature.day").withStyle(ChatFormatting.YELLOW), true);
                } else {
                    SP.displayClientMessage(Component.translatable("text.roundabout.riding_flying_creature").withStyle(ChatFormatting.YELLOW), true);
                }
            }
        } else {
            rideLengthTicks = 0;
        }
    }
    protected void doPlayerRide(Player $$0) {
        if (!this.level().isClientSide) {
            $$0.setYRot(this.getYRot());
            $$0.setXRot(this.getXRot());
            $$0.startRiding(this);

        }
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 $$0, LivingEntity $$1) {
        double $$2 = this.getX() + $$0.x;
        double $$3 = this.getBoundingBox().minY;
        double $$4 = this.getZ() + $$0.z;
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();

        for (Pose $$6 : $$1.getDismountPoses()) {
            $$5.set($$2, $$3, $$4);
            double $$7 = this.getBoundingBox().maxY + 0.75;

            do {
                double $$8 = this.level().getBlockFloorHeight($$5);
                if ((double)$$5.getY() + $$8 > $$7) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid($$8)) {
                    AABB $$9 = $$1.getLocalBoundsForPose($$6);
                    Vec3 $$10 = new Vec3($$2, (double)$$5.getY() + $$8, $$4);
                    if (DismountHelper.canDismountTo(this.level(), $$1, $$9.move($$10))) {
                        $$1.setPose($$6);
                        return $$10;
                    }
                }

                $$5.move(Direction.UP);
            } while (!((double)$$5.getY() < $$7));
        }

        return null;
    }


    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        for(Entity ent : this.getPassengers()){
            ent.unRide();
        }
        return super.hurt($$0, $$1);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity $$0) {
        Vec3 $$1 = getCollisionHorizontalEscapeVector(
                (double)this.getBbWidth(), (double)$$0.getBbWidth(), this.getYRot() + ($$0.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F)
        );
        Vec3 $$2 = this.getDismountLocationInDirection($$1, $$0);
        if ($$2 != null) {
            return $$2;
        } else {
            Vec3 $$3 = getCollisionHorizontalEscapeVector(
                    (double)this.getBbWidth(), (double)$$0.getBbWidth(), this.getYRot() + ($$0.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F)
            );
            Vec3 $$4 = this.getDismountLocationInDirection($$3, $$0);
            return $$4 != null ? $$4 : this.position();
        }
    }
    public InteractionResult mobInteract(Player $$0, InteractionHand $$1) {
        if (this.isVehicle() || !this.getActivated() || !(getController() == $$0.getId())) {
            return super.mobInteract($$0, $$1);
        } else {
            if (ClientNetworking.getAppropriateConfig().justiceSettings.phantomCorpseSpeed > 0) {
                this.doPlayerRide($$0);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
    }
}