package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.navigation.StandEntityNavigation;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.ExplosionUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class SheerHeartAttackEntity extends StandEntity {

	public SheerHeartAttackEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
		super($$0, $$1);
	}

	protected static final EntityDataAccessor<Byte> TARGET_STATUS = SynchedEntityData.defineId(SheerHeartAttackEntity.class,
			EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(SheerHeartAttackEntity.class,
			EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> ANIM = SynchedEntityData.defineId(SheerHeartAttackEntity.class,
			EntityDataSerializers.BYTE);

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(TARGET_STATUS, NONE);
		this.entityData.define(DATA_FLAGS_ID, (byte)0);
		this.entityData.define(ANIM, (byte)0);
	}

	public boolean isClimbing() {
		return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
	}

	public void setClimbing(boolean bl) {
		byte $$1 = (Byte)this.entityData.get(DATA_FLAGS_ID);
		if (bl) {
			$$1 = (byte)($$1 | 1);
		} else {
			$$1 = (byte)($$1 & -2);
		}

		this.entityData.set(DATA_FLAGS_ID, $$1);
	}

	public byte getAnim() {
		return this.entityData.get(ANIM);
	}

	public void setAnim(byte anim) {
		this.entityData.set(ANIM, anim);
	}

	public static float width = 0.5f;
	public static float height = 0.3f;

	@Override
	protected PathNavigation createNavigation(Level $$0) {
		StandEntityNavigation nav = new StandEntityNavigation(this, $$0);
		nav.setAvoidSun(false);
		nav.setAvoidFire(false);

		return nav;
	}

	public final AnimationState idle = new AnimationState();
	public final AnimationState moving = new AnimationState();

	int tickTargetFindCount = 0;
	static final int tickTargetFindMax = 2;

	int attackTick = 0;
	static final int attackTickMax = 25;
	int jumpTick = 0;
	static final int jumpTickMax = 30;

	public int struckTicks = 0;
	static final int struckMaxTicks = 12;
	public int flyngTicks = 0;

	private int soundsDelay = 40;

	static final float explosionRadius = 1.3f;

	static final byte
		NONE = 0,
		BLOCK = 1,
		ENTITY = 2,
		IDLE = 0,
		WALK = 1;

	public Entity entityTarget = null;
	public BlockPos blockTarget = null;
	public int ticksUntilNextPathRecalculation = 15;
	public int returnTicks = 0;
	private static final int returnMaxTicks = 300;
	public int inativeTicks = 0;
	private static final int inativeMaxTicks = 140;

	public int explosions = 0;

	public float viewRange = 10.0f;

	private boolean haveToReturn = false;

	public int getMaxExplosions() {
		return ClientNetworking.getAppropriateConfig().killerQueenSettings.sheerHeartAttackMaxExplosions;
	}

	public boolean getHaveToReturn() {
		return this.haveToReturn || (this.explosions >= getMaxExplosions() && getMaxExplosions() != 0)
				|| this.inativeTicks >= inativeMaxTicks;
	}

	public void setHaveToReturn(boolean value) {
		this.haveToReturn = value || (this.explosions >= getMaxExplosions() && getMaxExplosions() != 0);
		if (!this.haveToReturn) {
			this.returnTicks = 0;
		}
	}

	public static AttributeSupplier.Builder createStandAttributes() {
		return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
				0.5F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 5.0);
	}

	@Override
	public void setupAnimationStates() {

		if (getAnim() == IDLE) {
			this.idle.startIfStopped(this.tickCount);
		} else {
			this.idle.stop();
		}

		if (getAnim() == WALK) {
			this.moving.startIfStopped(this.tickCount);
		} else {
			this.moving.stop();
		}

	}

	@Override
	public void tick() {
		this.setFadeOut((byte)1);
		validateUUID();

		this.soundsDelay--;

		boolean client = this.level().isClientSide();
		LivingEntity user = this.getUser();

		if (!client) {
			if(user == null){
				this.discard();
			}else if((!(((StandUser)user).roundabout$getStandPowers() instanceof PowersKillerQueen)) || (!user.isAlive())){
				this.discard();
			}else {
				if ((((StandUser)user).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ)) {
					if (this != PKQ.SHA) {
						this.discard();
						return;
					}
				}

				this.setClimbing(this.horizontalCollision);

				if (this.tickTargetFindCount <= 0) {
					this.findTarget();
					this.tickTargetFindCount = tickTargetFindMax;
				}else {
					this.tickTargetFindCount--;
				}
				if (this.getTargetType() == NONE) {
					this.inativeTicks++;
				}else {
					this.inativeTicks = 0;
				}

				if (this.attackTick > 0) { this.attackTick--;}
				if (this.jumpTick > 0) { this.jumpTick--;}

				if (!this.onGround()) {
					flyngTicks++;
				}else {
					if (this.getDeltaMovement().length() > 0.1 && this.tickCount % 18 == 0) {
						this.level().playSound(null, this.blockPosition(), ModSounds.SHA_MOVING_EVENT, SoundSource.AMBIENT, 0.5F, 1.0f);
					}

					flyngTicks = 0;
				}
				this.moveToTarget();

				if (this.getHaveToReturn()) { this.returnTicks++; }


				if (flyngTicks > 2 && this.hasTarget()) {
					if (this.shouldExplode(this.getTargetPosition())) {
						this.attack();
					}
				}

				if (this.getDeltaMovement().length() > 0 && (this.onGround() || this.onClimbable())) {
					this.setAnim(WALK);
				}else {
					this.setAnim(IDLE);
				}
			}
		}

		super.tick();
	}

	protected void moveToTarget() {
		if (this.getHaveToReturn()) {
			Vec3 pos = this.getUser().position();
			this.shaMove(pos);
		} else if (this.hasTarget()) {
			Vec3 pos = this.getTargetPosition();
			if (this.shouldExplode(pos)) {
				this.attack();
				this.shaStopMove();
			} else if (this.shouldJump(pos)) {
				this.jump(pos);
				this.shaStopMove();
			} else {
				shaMove(pos);
			}
		} else {
			this.shaStopMove();
		}
	}

	public boolean hasTarget() {
		if (this.getTargetType() == ENTITY) {
			if (this.entityTarget == null) {
				this.setTargetType(NONE);
				return false;
			}
			if (!this.entityTarget.isAlive()) {
				this.setTargetType(NONE);
			}
			if (this.entityTarget instanceof LivingEntity LE) {
				if (LE.isDeadOrDying()) {
					this.setTargetType(NONE);
				}
			}
		}else if (this.getTargetType() == BLOCK) {
			BlockState BlockInfo = this.level().getBlockState(this.blockTarget);
			if (ExplosionUtil.isBlockBlackListed(BlockInfo)) {
				this.setTargetType(NONE);
				this.blockTarget = null;
			}
		}

		return this.getTargetType() != NONE;
	}

	public void findTarget() {
		int harmest = 0;
		double harmestDistance = -1;
		Entity targetEnt = null;
		int targetPosX = 0;
		int targetPosY = 0;
		int targetPosZ = 0;

		byte currentChoice = NONE;

		if (this.getTargetType() == BLOCK) {
			harmest = -1 + getBlockWarm(this.blockTarget, this.level());
			if (harmest > 0) {
				currentChoice = BLOCK;
				harmestDistance = this.blockTarget.distToCenterSqr(this.position());;

				targetPosX = this.blockTarget.getX();
				targetPosY = this.blockTarget.getY();
				targetPosZ = this.blockTarget.getZ();
			}
		}else if (this.getTargetType() == ENTITY && this.entityTarget != null
				&& this.entityTarget.isAlive()) {
			harmest = getEntityWarm(this.entityTarget);
			if (harmest > 0) {
				currentChoice = ENTITY;
				harmestDistance = this.entityTarget.distanceToSqr(this.position());
				targetEnt = this.entityTarget;
			}
		}

		List<Entity> entities = MainUtil.genHitbox(this.level(), this.getX(), this.getY(), this.getZ(), viewRange , viewRange , viewRange );

		for (Entity entity : entities) {
			if (!this.hasLineOfSight(entity)) {continue;}

			int points = getEntityWarm(entity);
            if (points <= 0) { continue; }

			double dist = entity.distanceToSqr(this.position());

            if (points > harmest || (points == harmest && (harmestDistance == -1 || dist < harmestDistance)))  {
				currentChoice = ENTITY;
                harmest = points;
                harmestDistance = dist;
                targetEnt = entity;
            }
        }
		BlockPos bPos = this.getOnPos();

		for (BlockPos pos : BlockPos.betweenClosed(
				bPos.offset((int)viewRange, (int)viewRange, (int)viewRange),
				bPos.offset(-(int)viewRange, -(int)viewRange, -(int)viewRange))) {
			BlockState info = this.level().getBlockState(pos);
			if (ExplosionUtil.isBlockBlackListed(info)) {
				continue;
			}

			int points = -1;

			points += getBlockWarm(pos, this.level());

			if (points < 0 || !canSeeBlock(pos)) { continue; }

			double dist = pos.distToCenterSqr(this.position());

			if (points > harmest || (points == harmest && (harmestDistance == -1 || dist < harmestDistance))) {
				currentChoice = BLOCK;
				harmest = points;
				harmestDistance = dist;
				targetPosX = pos.getX();
				targetPosY = pos.getY();
				targetPosZ = pos.getZ();

				//targetPos = pos.getCenter();
			}
		}


		if (currentChoice == ENTITY) {
			this.entityTarget = targetEnt;
			this.setTargetType(ENTITY);
		}else if (currentChoice == BLOCK) {
			//Vec3 bt = targetPos.subtract(0.5, 0.5, 0.5);
			this.blockTarget = new BlockPos(
					targetPosX,
					targetPosY,
					targetPosZ
			);
			this.setTargetType(BLOCK);
		}else {
			this.entityTarget = null;
			this.setTargetType(NONE);
		}
	}

	public boolean canSeeBlock(BlockPos pos) {
		BlockHitResult hitResult = this.level().clip(new ClipContext(this.getEyePosition(), pos.getCenter(),
				ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

		if (hitResult.getType() == HitResult.Type.MISS) {
			return true;
		}
		return (pos.closerToCenterThan(hitResult.getLocation(), 1.5));
	}

	public boolean shouldJump(Vec3 targetPos) {
		if ((this.attackTick > 0 || this.jumpTick > 0)|| this.isClimbing()) {
			return false;
		}
		double dist = Math.abs(this.position().distanceTo(targetPos));

		return (float)dist > (2.5f) && (float)dist < 4.0f;
	}

	public boolean shouldExplode(Vec3 targetPos) {
		if (this.attackTick > 0) {
			return false;
		}

		double dist = Math.abs(this.position().distanceTo(targetPos));

		float minDist = (explosionRadius-0.12f);
		if (this.getTargetType() == BLOCK) {
			minDist = 1.4f;
		}

		return (float)dist < minDist;
	}

	public byte getTargetType() {return this.entityData.get(TARGET_STATUS);}
	public void setTargetType(byte value) {this.entityData.set(TARGET_STATUS, value);}

	public Vec3 getTargetPosition() {
		byte type = this.getTargetType();

		if (type != NONE) {
			Vec3 targetPos;
			if (type == BLOCK) {
				targetPos = this.blockTarget.getCenter();
			}else {
				targetPos = this.entityTarget.position();
			}

			return targetPos;
		}
		return null;
	}


	public void attack() {

		DamageSource dmg = ModDamageTypes.of(this.level(), ModDamageTypes.EXPLOSIVE_STAND, this.getUser());;

		this.explosions++;

		if (this.getTargetType() == ENTITY){
			ExplosionUtil.explosionHurt(this.position(), dmg, this.level(),
					ClientNetworking.getAppropriateConfig().killerQueenSettings.SheerHeartAttackMaxDamage, 0.3f, explosionRadius);

			ExplosionUtil.explodeEffects(this.position(), this.level(), ModParticles.KILLER_QUEEN_EXPLOSION, 0.3f, 8);
			this.level().playSound(null, this.blockPosition(), ModSounds.KILLER_QUEEN_EXPLOSION_EVENT, SoundSource.PLAYERS, 0.65F, 1.0f);

			if (this.entityTarget != null) {

				MainUtil.takeDeterminedKnockbackWithY(this, this.entityTarget, 0.6f);

				if (!this.entityTarget.isAlive()) {
					this.entityTarget = null;
					this.setTargetType(NONE);
				} else if (this.entityTarget instanceof LivingEntity LE) {
					if (LE.isDeadOrDying()) {
						this.entityTarget = null;
						this.setTargetType(NONE);
					}
				}
			}
		}else if(this.getTargetType() == BLOCK){
			ExplosionUtil.explosionHurt(this.blockTarget.getCenter(), dmg, this.level(),
					ClientNetworking.getAppropriateConfig().killerQueenSettings.SheerHeartAttackMaxDamage, 0.3f, explosionRadius);

			ExplosionUtil.explodeEffects(this.blockTarget.getCenter(), this.level(), ModParticles.KILLER_QUEEN_EXPLOSION, 0.12f, 4);
			this.level().playSound(null, this.blockTarget, ModSounds.KILLER_QUEEN_EXPLOSION_EVENT, SoundSource.PLAYERS, 0.65F, 1.0f);

			boolean shouldDrop = !this.level().getBlockState(this.blockTarget).requiresCorrectToolForDrops();
			this.level().destroyBlock(this.blockTarget, shouldDrop);
			this.blockTarget = null;
			this.setTargetType(NONE);
		}

		this.attackTick = attackTickMax;
	}

	 public void jump(Vec3 jumpT0Pos){
		if (this.onGround()) {
			this.level().playSound(null, this.blockPosition(), ModSounds.SHA_JUMP_EVENT, SoundSource.PLAYERS, 0.5F, 1.0f);
			this.lookAt(EntityAnchorArgument.Anchor.EYES, jumpT0Pos);
			this.jumpTick = jumpTickMax;
			this.setDeltaMovement((this.getLookAngle().multiply(1.3, 0.54, 1.3)).add(0, 0.3, 0));
		}
	}

	@Override
	public boolean onClimbable() {
		return this.isClimbing();
	}



	public void shoot(Vec3 shootToPos){
		this.lookAt(EntityAnchorArgument.Anchor.EYES,shootToPos);
		this.setDeltaMovement((this.getLookAngle().multiply(1.6,1.6,1.6)).add(0,0.001,0));
	}

	public boolean shaIsNear() {
		Vec3 targetPos = this.getUser().position();
		double dist = Math.abs(targetPos.subtract(this.getX(), this.getY(), this.getZ()).length());

		boolean struck = this.getNavigation().isStuck() || this.struckTicks >= struckMaxTicks;

		return (dist <= 1.3f) || struck || this.returnTicks > returnMaxTicks;
	}

	public void shaStopMove() {
		this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0);
		this.getNavigation().setSpeedModifier(0.0);
		this.getNavigation().stop();
	}

	public void shaMove(Vec3 targetPos) {
		ticksUntilNextPathRecalculation--;
		if (ticksUntilNextPathRecalculation <= 0 ) {
			ticksUntilNextPathRecalculation = 15; // + mob.getRandom().nextInt(7);

			Path newPath;
			if (this.getHaveToReturn()) {
				newPath = this.getNavigation().createPath(this.getUser(), 1);
			}else if (this.getTargetType() == ENTITY) {
				newPath = this.getNavigation().createPath(this.entityTarget, 0);
			}else if (this.getTargetType() == BLOCK) {
				BlockState BS = this.level().getBlockState(this.blockTarget);
				if (BS.isPathfindable(this.level(), this.blockTarget, PathComputationType.LAND)) {
					newPath = this.getNavigation().createPath(this.blockTarget.below(), 0);
					//this.level().getBlockState(this.blockTarget);
				}else {
					newPath = this.getNavigation().createPath(this.blockTarget, 0);
				}


			}else {
				newPath = this.getNavigation().createPath(targetPos.x, targetPos.y, targetPos.z, 0);
			}

			if (newPath == null) {
				return;
			}

			if (!this.getNavigation().moveTo(newPath, 0.5f))
				ticksUntilNextPathRecalculation += 5;
		}
	}


	public int getBlockWarm(BlockPos pos, Level level) {
		BlockState info = level.getBlockState(pos);

		return (int)(info.getLightEmission() * 1.5);
	}

	public int getEntityWarm(Entity entity) {
		int points = 0;

		if (entity instanceof StandEntity || entity.is(this.getUser())){ return -1;}

		if (entity instanceof LivingEntity LE) {
			if (LE.isDeadOrDying()) { return points; }
			if (LE instanceof Player pl) {
				if (pl.isCreative()) { return points; }
			}
			points += 20;
			points += HeatUtil.getHeat(LE);

			if (LE.isOnFire() || LE.wasOnFire || HeatUtil.isHot(LE)) { points += 70;}
			if (LE.isFullyFrozen()) { points -= 80;
			}else if (LE.isFreezing()) { points -= 40;}
			
			if (((StandUser)LE).roundabout$hasAStand()) {
				StandUser SU = (StandUser)LE;
				if(SU.roundabout$getStandPowers() instanceof PowersWhiteAlbum && SU.roundabout$getActive()) {
					points -= 130;
				}
			}

			MobType mobType = LE.getMobType();
			if (mobType.equals(MobType.UNDEAD)) { points -= 30;}
		}
		return points;
	}

	@Override
	protected void playStepSound(BlockPos blockPos, BlockState blockState) {
		//this.playSound(ModSounds.KILLER_QUEEN_SHA_MOVING_EVENT, 0.15f, 1.0f);

		//SoundType soundType = blockState.getSoundType();
		//this.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15f, soundType.getPitch());
	}

	@Override
	protected SoundEvent getAmbientSound() {

		if (this.soundsDelay > 0) {
			return  null;
		}
		this.soundsDelay = 70;

		LivingEntity user = this.getUser();

		if (user == null) { return null; }

		byte skin = ((StandUser)user).roundabout$getStandSkin();

		double rand = Math.random();

		if ( skin == KillerQueenEntity.CRACKED || rand >= 0.7) {
			if (this.getTargetType() != NONE && (rand >= 0.82 || (skin == KillerQueenEntity.CRACKED && rand >= 0.5))) {
				return ModSounds.KILLER_QUEEN_SHA_CRACKED_KOCCHI_EVENT;
			}

			return ModSounds.KILLER_QUEEN_SHA_CRACKED_DEDE_EVENT;
		}else {
			if (this.getTargetType() != NONE && rand >= 0.3) {
				if (rand >= 0.5) {
					return ModSounds.KILLER_QUEEN_SHA_KOCCHI_1_EVENT;
				}

				return ModSounds.KILLER_QUEEN_SHA_KOCCHI_2_EVENT;
			}

			return ModSounds.KILLER_QUEEN_SHA_DEDEDEDE_EVENT;
		}

	}

	protected float getSoundVolume() {
		return 0.8f;
	}

	public float getVoicePitch() {
		return 1.0f;
	}

	public boolean mobAiShouldRetreactDetect(Entity Owner) {
		if (this.getTargetType() == ENTITY) {
			Entity target = entityTarget;

			if (Owner instanceof AbstractVillager || Owner instanceof IronGolem) {
				if (target instanceof AbstractVillager || target instanceof IronGolem) {
					return true;
				}
			}
			if (Owner instanceof Raider || Owner instanceof AbstractVillager) {
				if (target instanceof Raider || target instanceof AbstractVillager) {
					return true;
				}
			}
			if (Owner instanceof AbstractPiglin) {
				if (target instanceof AbstractPiglin) {
					return true;
				}
			}
		}
		return false;
	}

    @Override public boolean hurt(DamageSource source, float amount) { return false;}
	@Override public boolean isPickable() { return true;}
	@Override public boolean isPushedByFluid() { return true;}
	@Override public boolean hasNoPhysics() { return false;}
	@Override public boolean isNoGravity() { return false;}
	@Override public boolean isPushable() { return true;}
	@Override public boolean isAttackable() { return true;}
	@Override public boolean canBeHitByProjectile() { return true;}
	@Override public boolean canBeHitByStands() { return true;}
	@Override public boolean mayInteract(Level $$0, BlockPos pos) { return false;}
}