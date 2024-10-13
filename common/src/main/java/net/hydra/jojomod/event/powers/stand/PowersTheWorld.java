package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.event.powers.stand.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class PowersTheWorld extends TWAndSPSharedPowers {

    public PowersTheWorld(LivingEntity self) {
        super(self);
    }

    @Override
    protected SoundEvent getSummonSound() {
        return ModSounds.WORLD_SUMMON_SOUND_EVENT;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersTheWorld(entity);
    }

    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.THE_WORLD.create(this.getSelf().level());
    }

    @Override
    public SoundEvent getLastHitSound(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
    }
    @Override
    public SoundEvent getLastRejectionHitSound(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
    }

    /**Assault Ability*/
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide && !this.isClashing() && this.getActivePower() != PowerIndex.POWER_2
                && (this.getActivePower() != PowerIndex.POWER_2_EXTRA || this.getAttackTimeDuring() < 0) && !hasEntity()
                && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0) && !hasBlock()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                if (!options.keyShift.isDown()) {
                    if (keyIsDown) {
                        if (!hold1) {
                            hold1 = true;
                            if (!this.onCooldown(PowerIndex.SKILL_1)) {
                                if (this.activePower == PowerIndex.POWER_1 || this.activePower == PowerIndex.POWER_1_BONUS) {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                                } else {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1);
                                }
                                return;
                            }
                        }
                    } else {
                        hold1 = false;
                    }
                }
            }
        }
        super.buttonInput1(keyIsDown,options);
    }

    @Override
    public float inputSpeedModifiers(float basis){

        return super.inputSpeedModifiers(basis);
    }



    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (this.activePower == PowerIndex.POWER_1){
            return;
        }
        super.buttonInput3(keyIsDown,options);
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        if (this.getActivePower() == PowerIndex.POWER_1 && this.getAttackTimeDuring() >= 0 && slot != 2 && slot != 1){
            return true;
        } else if (this.getActivePower() == PowerIndex.POWER_1_BONUS && this.getAttackTimeDuring() >= 0 && slot != 1){
            return true;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }
    @Override
    public void playBarrageClashSound(){
        if (!this.self.level().isClientSide()) {
            playSoundsIfNearby(BARRAGE_NOISE_2, 32, false);
        }
    }
    @Override
    public boolean tryPower(int move, boolean forced) {

        if (this.getActivePower() == PowerIndex.POWER_1 || this.getActivePower() == PowerIndex.POWER_1_BONUS){
            if (move != PowerIndex.POWER_1_BONUS) {
                stopSoundsIfNearby(ASSAULT_NOISE, 32, false);
            }
        }
        return super.tryPower(move,forced);
    }

    @Override
    public int setCurrentMaxTSTime(int chargedTSSeconds){
        if (chargedTSSeconds >= 100){
            this.maxChargeTSTime = 180;
            this.setChargedTSTicks(180);
            return 80;
        } else if (chargedTSSeconds == 20) {
            this.maxChargeTSTime = 20;
        } else {
            this.maxChargeTSTime = 100;
        }
        return 0;
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_1) {
            return this.assault();
        } else if (move == PowerIndex.POWER_1_BONUS && this.getActivePower() == PowerIndex.POWER_1) {
            return this.assaultGrab();
        }
        return super.setPowerOther(move,lastMove);
    }




    public Vec3 assultVec = Vec3.ZERO;
    public boolean assaultGrab(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setActivePower(PowerIndex.POWER_1_BONUS);
            if (!this.getSelf().level().isClientSide) {
                this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.PLAYERS, 0.95F, 1.3F);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        stand.getX(), stand.getY() + 0.3, stand.getZ(),
                        30, 0.4, 0.4, 0.4, 0.4);
            }
            return true;
        }
        return false;
    }
    public boolean assault(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_1);
            playSoundsIfNearby(ASSAULT_NOISE, 32, false);
            this.animateStand((byte)39);
            this.poseStand(OffsetIndex.LOOSE);
            stand.setYRot(this.getSelf().getYHeadRot() % 360);
            stand.setXRot(this.getSelf().getXRot());
            assultVec = DamageHandler.getRotationVector(
                    this.getSelf().getXRot(), (float) (this.getSelf().getYRot())).scale(1.8).add(0,0.25,0);
            stand.setPos(this.getSelf().position().add(assultVec));
            return true;
        }
        return false;
    }
    public static final byte ASSAULT_NOISE = 80;

    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
            super.updateUniqueMoves();
    }

    public void playBarrageCrySound(){
        if (!this.self.level().isClientSide()) {
            byte barrageCrySound = this.chooseBarrageSound();
            if (barrageCrySound != SoundIndex.NO_SOUND) {
                playSoundsIfNearby(barrageCrySound, 32, false);
            }
        }
    }
    @Override
    public float getSoundPitchFromByte(byte soundChoice){
            return super.getSoundPitchFromByte(soundChoice);
    }


    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
    }




    @Override
    public void tickPowerEnd(){

        super.tickPowerEnd();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {
            if (this.getActivePower() == PowerIndex.POWER_1 || this.getActivePower() == PowerIndex.POWER_1_BONUS) {
                if (!this.getSelf().level().isClientSide()) {
                    if (this.attackTimeDuring == 108) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    } else if (this.attackTimeDuring >= 0) {
                        StandEntity stand = getStandEntity(this.self);
                        if (Objects.nonNull(stand)) {
                            AABB BB1 = stand.getBoundingBox();
                            Vec3 vec3d = this.getSelf().getEyePosition(0);
                            Vec3 vec3d2 = this.getSelf().getViewVector(0);
                            Vec3 vec3d3 = vec3d.add(vec3d2.x * 15, vec3d2.y * 15, vec3d2.z * 15);
                            double mag = 0.05F;

                            if (this.attackTimeDuring > 10) {
                                mag += Math.pow(attackTimeDuring-10, 1.4) / 1000;
                            }
                            BlockHitResult blockHit = this.getSelf().level().clip(
                                    new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                                            this.getSelf()));

                            Vec3 blockCenterPlus = blockHit.getBlockPos().getCenter();

                            assultVec = assultVec.add(
                                    blockCenterPlus.subtract(this.getSelf().position().add(assultVec)).normalize().scale(mag)
                            );
                            Vec3 yes = this.getSelf().position().add(assultVec);
                            double post = stand.position().distanceTo(blockHit.getBlockPos().getCenter());
                            if (post< 1.5){
                                stand.setYRot(this.getSelf().getYHeadRot() % 360);
                                stand.setXRot(this.getSelf().getXRot());
                            } else {
                                stand.setYRot(getLookAtPlaceYaw(stand,blockCenterPlus));
                                stand.setXRot(getLookAtPlacePitch(stand,blockCenterPlus));
                            }
                            if (post < 0.4){
                                stand.setPos(blockHit.getBlockPos().getCenter());
                            } else {
                                stand.setPos(yes);
                            }

                            if (this.getActivePower() == PowerIndex.POWER_1_BONUS) {
                                if (post <= 2){
                                    ((StandUser) this.getSelf()).roundabout$tryPosPower(PowerIndex.POWER_2,
                                            true, blockHit.getBlockPos());
                                    return;
                                }
                            }

                            if ((stand.isTechnicallyInWall() && this.getActivePower() != PowerIndex.POWER_1_BONUS) ||
                                    stand.position().distanceTo(this.getSelf().position()) > 10 ||
                                    stand.position().distanceTo(this.getSelf().position()) > 10){
                                stopSoundsIfNearby(ASSAULT_NOISE, 32, false);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                            }
                            AABB BB2 = stand.getBoundingBox();
                            if (this.attackTimeDuring > 10) {
                                if (this.getActivePower() != PowerIndex.POWER_1_BONUS){
                                    tryAssaultHit(stand, BB1, BB2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public double getGrabRange(){
        if (this.getActivePower() == PowerIndex.POWER_1_BONUS){
            return 121;
        }
        return super.getGrabRange();
    }

    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == ASSAULT_NOISE) {
            return ASSAULT_NOISE;
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1
        || soundChoice == KICK_BARRAGE_NOISE_2 || soundChoice == KICK_BARRAGE_NOISE){
                return SoundIndex.BARRAGE_SOUND_GROUP;
        }
        return super.getSoundCancelingGroupByte(soundChoice);
    }

    public boolean tryAssaultHit(StandEntity stand, AABB bb1, AABB bb2){
        bb1 = bb1.inflate(1.6F);
        bb2 = bb2.inflate(1.6F);

        AABB $$2 = bb1.minmax(bb2);
        List<Entity> $$3 = stand.level().getEntities(stand, $$2);
        if (!$$3.isEmpty()) {
            for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                Entity $$5 = $$3.get($$4);
                if ($$5 instanceof LivingEntity && !$$5.is(this.getSelf()) && $$5.showVehicleHealth() &&
                        !$$5.isInvulnerable() && $$5.isAlive() && !(this.self.isPassenger() &&
                        this.self.getVehicle().getUUID() == $$5.getUUID()) && stand.getSensing().hasLineOfSight($$5)){

                    if (this.StandDamageEntityAttack($$5,getAssaultStrength($$5), 0.4F, this.self)){
                        MainUtil.makeBleed($$5,0,200,null);
                    } else if (((LivingEntity) $$5).isBlocking()) {
                        MainUtil.knockShieldPlusStand($$5,40);
                    }

                    stopSoundsIfNearby(ASSAULT_NOISE, 32, false);
                    stand.setYRot(getLookAtEntityYaw(stand,$$5));
                    stand.setXRot(getLookAtEntityPitch(stand,$$5));
                    this.self.level().playSound(null, this.self.blockPosition(),  ModSounds.PUNCH_4_SOUND_EVENT,
                            SoundSource.PLAYERS, 0.95F, 1.3F);
                    ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()),
                            PowerIndex.SKILL_1, 40);
                    this.setCooldown(PowerIndex.SKILL_1, 40);
                    this.setAttackTimeDuring(-12);
                    animateStand((byte) 40);
                    return true;
                }
            }
        }
        return false;
    }

    public float getAssaultStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2.5F;
        } else {
            return 6;
        }
    }

    @Override
    public void tickPower(){

        //Roundabout.LOGGER.info("AT: "+this.attackTime+" ATD: "+this.attackTimeDuring+" kickstarted: "+this.kickStarted+" APP: "+this.getActivePowerPhase()+" MAX:"+this.getActivePowerPhaseMax());
        super.tickPower();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {
            if (this.getSelf().getAirSupply() < this.getSelf().getMaxAirSupply() && ((StandUser) this.getSelf()).roundabout$getActive()){
                if (this.getAirAmount() > 0) {
                    this.getSelf().setAirSupply(((StandUser) this.getSelf()).roundabout$increaseAirSupply(this.getSelf().getAirSupply()));
                    this.setAirAmount(Math.max(0, Math.min(this.getAirAmount() - 4, this.getMaxAirAmount())));
                }
            } else {
                if (this.getSelf().isEyeInFluid(FluidTags.WATER)
                        && !this.getSelf().level().getBlockState(BlockPos.containing(
                        this.getSelf().getX(), this.getSelf().getEyeY(),
                        this.getSelf().getZ())).is(Blocks.BUBBLE_COLUMN)) {
                } else {
                    if (((StandUser) this.getSelf()).roundabout$getActive()) {
                            this.setAirAmount(Math.min(this.getAirAmount() + 4, this.getMaxAirAmount()));
                    }
                }
            }
        }
    }

    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide && !this.isClashing()) {
            if (keyIsDown) {
                if (this.getActivePower() == PowerIndex.POWER_1){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_BONUS, true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1_BONUS);
                    return;
                }
            }
        }
        super.buttonInput2(keyIsDown,options);
    }

    @Override
    public boolean canInterruptPower(){
        if (this.getActivePower() == PowerIndex.POWER_1 || this.getActivePower() == PowerIndex.POWER_1_BONUS){
            if (this.getSelf() instanceof Player) {
                ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1, 60);
            }
            this.setCooldown(PowerIndex.SKILL_1, 60);
            return true;
        } else {
            return super.canInterruptPower();
        }
    }

    /**Charge up Time Stop*/
    @Override
    public boolean canChangePower(int move, boolean forced){
        if (!this.isClashing() || move == PowerIndex.CLASH_CANCEL) {
            if ((this.getActivePower() == PowerIndex.NONE || forced) &&
                    (!this.isDazed(this.getSelf()) || move == PowerIndex.BARRAGE_CLASH)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte chooseBarrageSound(){
        double rand = Math.random();
        if (rand > 0.5) {
            return BARRAGE_NOISE;
        } else {
            return BARRAGE_NOISE_2;
        }
    }


    public int teleportTime = 0;
    public int postTPStall = 0;

    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (attackTarget != null && attackTarget.isAlive() && !this.isDazed(this.getSelf())){
            boolean water = (this.getSelf() instanceof WaterAnimal || this.getSelf() instanceof Guardian);
            if (this.attackTimeDuring <= -1) {
                if (!this.getSelf().isPassenger()) {
                    teleportTime = Math.max(0,teleportTime-1);
                    if (teleportTime == 0 && !(this.getSelf() instanceof Creeper CREEP && CREEP.isIgnited())) {
                        double dist = attackTarget.distanceTo(this.getSelf());
                        if (dist <= 8 && !(this.getSelf() instanceof Creeper)) {
                            Vec3 pos = this.getSelf().position().add(0,this.getSelf().getEyeHeight(),0);
                            float p = 0;
                            float y = 0;
                            if (this.getSelf() instanceof Villager || this.getSelf() instanceof Skeleton){
                                p =getLookAtEntityPitch(this.getSelf(), attackTarget);
                                y = getLookAtEntityYaw(this.getSelf(), attackTarget);
                            }
                            if (this.teleport(water)){
                                if (this.getSelf() instanceof Villager){
                                    for (int i = 0; i< 4; i++) {
                                        KnifeEntity $$7 = new KnifeEntity(this.getSelf().level(), this.getSelf(), ModItems.KNIFE.getDefaultInstance(),pos);
                                        $$7.pickup = AbstractArrow.Pickup.DISALLOWED;
                                        $$7.shootFromRotationWithVariance(this.getSelf(),
                                                p,
                                                y,
                                                -0.5F, 1.5F, 1.0F);
                                        this.getSelf().level().addFreshEntity($$7);
                                    }
                                } else if (this.getSelf() instanceof Skeleton){
                                        Arrow $$7 = new Arrow(this.getSelf().level(),pos.x,pos.y,pos.z);
                                        $$7.pickup = AbstractArrow.Pickup.DISALLOWED;
                                        $$7.shootFromRotation(this.getSelf(),
                                                p,
                                                y,
                                                0F, 3.0F, 1.0F);
                                        $$7.setOwner(this.getSelf());
                                        this.getSelf().level().addFreshEntity($$7);
                                }
                                teleportTime = 200;
                                postTPStall = 8;
                            }
                        } else if (dist < 40) {
                            if (this.teleportTowards(attackTarget,water)) {
                                if (this.getSelf() instanceof Creeper){
                                    this.teleportTime = 100;
                                } else {
                                    this.teleportTime = 200;
                                }
                                postTPStall = 8;
                            }
                        }

                    }
                }
            }
        }
        postTPStall = Math.max(0,postTPStall-1);
        if (postTPStall == 0) {
            if (!(this.getSelf() instanceof Creeper)) {
                super.tickMobAI(attackTarget);
            }
        }
    }


    protected boolean teleport(boolean water) {
        if (!this.getSelf().level().isClientSide() && this.getSelf().isAlive()) {
            double $$0 = this.getSelf().getX() + (this.getSelf().getRandom().nextDouble() - 0.5) * 19.0;
            double $$1 = this.getSelf().getY() + (double)(this.getSelf().getRandom().nextInt(16) - 8);
            double $$2 = this.getSelf().getZ() + (this.getSelf().getRandom().nextDouble() - 0.5) * 19.0;
            return this.teleport($$0, $$1, $$2,water);
        } else {
            return false;
        }
    }

    boolean teleportTowards(Entity $$0,boolean water) {
        Vec3 $$1 = new Vec3(this.getSelf().getX() - $$0.getX(), this.getSelf().getY(0.5) - $$0.getEyeY(), this.getSelf().getZ() - $$0.getZ());
        $$1 = $$1.normalize();
        double $$2 = 16.0;
        double $$3 = this.getSelf().getX() + (this.getSelf().getRandom().nextDouble() - 0.5) * 8.0 - $$1.x * 16.0;
        double $$4 = this.getSelf().getY() + (double)(this.getSelf().getRandom().nextInt(16) - 8) - $$1.y * 16.0;
        double $$5 = this.getSelf().getZ() + (this.getSelf().getRandom().nextDouble() - 0.5) * 8.0 - $$1.z * 16.0;
        return this.teleport($$3, $$4, $$5, water);
    }

    private boolean teleport(double $$0, double $$1, double $$2,boolean water) {
        BlockPos.MutableBlockPos $$3 = new BlockPos.MutableBlockPos($$0, $$1, $$2);

        while ($$3.getY() > this.getSelf().level().getMinBuildHeight() && !this.getSelf().level().getBlockState($$3).blocksMotion()) {
            $$3.move(Direction.DOWN);
        }

        BlockState $$4 = this.getSelf().level().getBlockState($$3);
        boolean $$5 = $$4.blocksMotion();
        boolean $$6 = $$4.getFluidState().is(FluidTags.WATER);
        if ($$5 && !$$6) {
            Vec3 $$7 = this.getSelf().position();
            boolean $$8 = randomTeleport($$0, $$1, $$2, true,water);
            if ($$8) {
                if (!this.getSelf().isSilent()) {
                    this.getSelf().level().playSound(null, this.getSelf().xo, this.getSelf().yo,
                            this.getSelf().zo, ModSounds.TIME_SNAP_EVENT, this.getSelf().getSoundSource(), 2.0F, 1.0F);
                    this.getSelf().playSound(ModSounds.TIME_SNAP_EVENT, 2.0F, 1.0F);
                }
            }

            return $$8;
        } else {
            return false;
        }
    }

    public boolean randomTeleport(double $$0, double $$1, double $$2, boolean $$3,boolean water) {
        double $$4 = this.getSelf().getX();
        double $$5 = this.getSelf().getY();
        double $$6 = this.getSelf().getZ();
        double $$7 = $$1;
        boolean $$8 = false;
        BlockPos $$9 = BlockPos.containing($$0, $$1, $$2);
        Level $$10 = this.getSelf().level();
        if ($$10.hasChunkAt($$9)) {
            boolean $$11 = false;

            while (!$$11 && $$9.getY() > $$10.getMinBuildHeight()) {
                BlockPos $$12 = $$9.below();
                BlockState $$13 = $$10.getBlockState($$12);
                if ($$13.blocksMotion()) {
                    $$11 = true;
                } else {
                    $$7--;
                    $$9 = $$12;
                }
            }

            if ($$11) {
                AABB bb2 = this.getSelf().getDimensions(this.getSelf().getPose()).makeBoundingBox($$0,$$7,$$2);
                if ($$10.noCollision(null,bb2) &&
                        ((!water && !$$10.containsAnyLiquid(bb2)) ||
                                (water && $$10.containsAnyLiquid(bb2)))) {
                    $$8 = true;
                    packetNearby(new Vector3f((float) $$0, (float) $$7, (float) $$2));
                    this.getSelf().teleportTo($$0, $$7, $$2);
                    packetNearby(new Vector3f((float) $$0, (float) $$7, (float) $$2));
                }
            }
        }
        if (!$$8) {
            this.getSelf().teleportTo($$4, $$5, $$6);
            return false;
        } else {

            if (this.getSelf() instanceof PathfinderMob) {
                ((PathfinderMob)this.getSelf()).getNavigation().stop();
            }

            return true;
        }
    }


    public final void packetNearby(Vector3f blip) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100)) {
                    ModPacketHandler.PACKET_ACCESS.sendBlipPacket(serverPlayerEntity, (byte) 2, this.getSelf().getId(),blip);
                }
            }
        }
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA5_SOUND_EVENT;
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
        } else if (soundChoice == KICK_BARRAGE_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA4_SOUND_EVENT;
        } else if (soundChoice == KICK_BARRAGE_NOISE_2) {
            return ModSounds.THE_WORLD_WRY_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2){
            return ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT;
        } else if (soundChoice == ASSAULT_NOISE){
            return ModSounds.THE_WORLD_ASSAULT_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE) {
            return ModSounds.TIME_STOP_THE_WORLD_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_2) {
            return ModSounds.TIME_STOP_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_3) {
            return ModSounds.TIME_STOP_THE_WORLD3_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_CHARGE){
            return ModSounds.TIME_STOP_CHARGE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_2){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_3){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD3_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE_2){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE){
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_TICKING){
            return ModSounds.TIME_STOP_TICKING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    //public void setSkillIcon(GuiGraphics context, int x, int y, ResourceLocation rl, boolean dull, @Nullable CooldownInstance cooldownInstance){
    public int currentAir = this.getMaxAirAmount();
    @Override
    public int getAirAmount(){
        return currentAir;
    }
    @Override
    public void setAirAmount(int airAmount){
        currentAir = airAmount;
        if (this.getSelf() instanceof ServerPlayer) {
            ModPacketHandler.PACKET_ACCESS.sendIntPacket(((ServerPlayer) this.getSelf()),
                    PacketDataIndex.S2C_INT_OXYGEN_TANK, currentAir);
        }
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y){
        if (this.getSelf().isCrouching()){

            setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_IMPALE, PowerIndex.SKILL_1_SNEAK);
            setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_ITEM, PowerIndex.SKILL_2);

            boolean done = false;
            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1){

                if (!this.getSelf().onGround() && canStandRebound()) {
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_WORLD, PowerIndex.SKILL_3_SNEAK);
                }

            } else {

                if (!this.getSelf().onGround()){
                    if (canVault()){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_LEDGE_GRAB, PowerIndex.SKILL_3_SNEAK);
                    } else if (this.getSelf().fallDistance > 3){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                    }
                }
            }
            if (!done){
                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_WORLD, PowerIndex.SKILL_3_SNEAK);
            }
        } else {


            setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_ASSAULT, PowerIndex.SKILL_1);

            /*If it can find a mob to grab, it will*/
            Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(),2.1F);
            if (targetEntity != null && canGrab(targetEntity)) {
                setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_MOB, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_BLOCK, PowerIndex.SKILL_2);
            }


            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1 && !this.getSelf().onGround() && canStandRebound()) {
                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_WORLD, PowerIndex.SKILL_3_SNEAK);
            } else {
                if (!(((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1) && !this.getSelf().onGround() && canVault()) {
                    setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_LEDGE_GRAB, PowerIndex.SKILL_3_SNEAK);
                } else if (!this.getSelf().onGround() && this.getSelf().fallDistance > 3){
                    setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
                }
            }
        }

        if (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP_RESUME, PowerIndex.NO_CD);
        } else if (this.getSelf().isCrouching()){
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP_IMPULSE, PowerIndex.SKILL_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP, PowerIndex.SKILL_4);
        }
    }

    protected void clampRotation(Entity $$0) {
        $$0.setYBodyRot(this.getSelf().getYRot());
        float $$1 = Mth.wrapDegrees($$0.getYRot() - this.getSelf().getYRot());
        float $$2 = Mth.clamp($$1, -105.0F, 105.0F);
        $$0.yRotO += $$2 - $$1;
        $$0.setYRot($$0.getYRot() + $$2 - $$1);
        $$0.setYHeadRot($$0.getYRot());
    }

    public static final byte DODGE_NOISE = 19;

}
