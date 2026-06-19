package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.navigation.StandEntityNavigation;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;

import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.util.ExplosionUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class SheerHeartAttackEntity extends StandEntity {

	public SheerHeartAttackEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
		super($$0, $$1);
	}

	protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SheerHeartAttackEntity.class,
			EntityDataSerializers.INT);

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
	static final int tickTargetFindMax = 4;

	int attackTick = 0;
	static final int attackTickMax = 25;
	int jumpTick = 0;
	static final int jumpTickMax = 30;

	public int struckTicks = 0;
	static final int struckMaxTicks = 12;
	public int flyngTicks = 0;

	static final float explosionRadius = 1.3f;

	static final byte
		NONE = 0,
		BLOCK = 1,
		ENTITY = 2,
		IDLE = 0,
		WALK = 1;

	public byte currentTarget = NONE;
	public Entity entityTarget = null;
	public BlockPos blockTarget = null;
	public int ticksUntilNextPathRecalculation = 15;

	public float viewRange = 10.0f;

	private boolean haveToReturn = false;

	public boolean getHaveToReturn() { return this.haveToReturn;}
	public void setHaveToReturn(boolean value) { this.haveToReturn = value;}

	public static AttributeSupplier.Builder createStandAttributes() {
		return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
				0.5F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 5.0);
	}

	@Override
	public void setupAnimationStates() {
		//super.setupAnimationStates();
		if (this.getUser() != null) {
			if (this.getDeltaMovement() == Vec3.ZERO) {
				this.idle.startIfStopped(this.tickCount);
				this.moving.stop();
			} else {
				this.idle.stop();
				this.moving.startIfStopped(this.tickCount);
			}

		}
	}

	@Override
	public void tick() {
		this.setFadeOut((byte)1);
		validateUUID();

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

				if (this.tickTargetFindCount <= 0) {
					this.findTarget();
					this.tickTargetFindCount = tickTargetFindMax;
				}else {
					this.tickTargetFindCount--;
				}

				if (this.attackTick > 0) { this.attackTick--;}
				if (this.jumpTick > 0) { this.jumpTick--;}

				if (!this.onGround()) {
					flyngTicks++;
				}else {
					flyngTicks = 0;
				}
				this.moveToTarget();

				if (flyngTicks > 2 && this.hasTarget()) {
					if (this.shouldExplode(this.getTargetPosition())) {
						this.attack();
					}
				}
			}
		}

		super.tick();
	}

	protected void moveToTarget() {
		if (this.haveToReturn) {
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
		if (this.currentTarget == ENTITY) {
			if (this.entityTarget == null) {
				this.currentTarget = NONE;
				return false;
			}
			if (!this.entityTarget.isAlive()) {
				this.currentTarget = NONE;
			}
			if (this.entityTarget instanceof LivingEntity LE) {
				if (LE.isDeadOrDying()) {
					this.currentTarget = NONE;
				}
			}
		}else if (this.currentTarget == BLOCK) {
			BlockState BlockInfo = this.level().getBlockState(this.blockTarget);
			if (ExplosionUtil.isBlockBlackListed(BlockInfo)) {
				this.currentTarget = NONE;
				this.blockTarget = null;
			}
		}

		return this.currentTarget != NONE;
	}

	public void findTarget() {
		List<Entity> entities = MainUtil.genHitbox(this.level(), this.getX(), this.getY(), this.getZ(), viewRange , viewRange , viewRange );
		int harmest = 0;
		double harmestDistance = -1;

		Entity targetEnt = null;
		int targetPosX = 0;
		int targetPosY = 0;
		int targetPosZ = 0;

		byte currentChoice = NONE;

        for (Entity entity : entities) {
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

			points += (int)(info.getLightEmission() * 1.5);

			if (points < 0) { continue; }

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
			this.currentTarget = ENTITY;
		}else if (currentChoice == BLOCK) {
			//Vec3 bt = targetPos.subtract(0.5, 0.5, 0.5);
			this.blockTarget = new BlockPos(
					targetPosX,
					targetPosY,
					targetPosZ
			);
			this.currentTarget = BLOCK;
		}else {
			this.entityTarget = null;
			this.currentTarget = NONE;
		}
	}

	public boolean shouldJump(Vec3 targetPos) {
		if (this.jumpTick > 0) {
			return false;
		}
		double dist = Math.abs(this.position().distanceTo(targetPos));

		return (float)dist > (3.0f) && (float)dist < 4.0f;
	}
	public boolean shouldExplode(Vec3 targetPos) {
		if (this.attackTick > 0) {
			return false;
		}

		double dist = Math.abs(this.position().distanceTo(targetPos));

		Roundabout.LOGGER.info("Distance: " + dist);

		float minDist = (explosionRadius-0.12f);
		if (this.currentTarget == BLOCK) {
			minDist = 1.4f;
		}

		return (float)dist < minDist;
	}

	public byte getTargetType() { return this.currentTarget;}

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

		DamageSource dmg = ModDamageTypes.of(this.level(), DamageTypes.PLAYER_EXPLOSION, this);;

		if (this.getTargetType() == ENTITY){
			ExplosionUtil.explosionHurt(this.position(), dmg, this.level(),
					ClientNetworking.getAppropriateConfig().killerQueenSettings.SheerHeartAttackMaxDamage, 0.3f, explosionRadius);

			ExplosionUtil.explodeEffects(this.position(), this.level(), ModParticles.KILLER_QUEEN_EXPLOSION, 0.3f, 8);

			if (this.entityTarget != null) {

				MainUtil.takeDeterminedKnockbackWithY(this, this.entityTarget, 0.6f);

				if (!this.entityTarget.isAlive()) {
					this.entityTarget = null;
					this.currentTarget = NONE;
				} else if (this.entityTarget instanceof LivingEntity LE) {
					if (LE.isDeadOrDying()) {
						this.entityTarget = null;
						this.currentTarget = NONE;
					}
				}
			}
		}else if(this.getTargetType() == BLOCK){
			ExplosionUtil.explosionHurt(this.blockTarget.getCenter(), dmg, this.level(),
					ClientNetworking.getAppropriateConfig().killerQueenSettings.SheerHeartAttackMaxDamage, 0.3f, explosionRadius);

			ExplosionUtil.explodeEffects(this.blockTarget.getCenter(), this.level(), ModParticles.KILLER_QUEEN_EXPLOSION, 0.12f, 4);

			boolean shouldDrop = !this.level().getBlockState(this.blockTarget).requiresCorrectToolForDrops();
			this.level().destroyBlock(this.blockTarget, shouldDrop);
			this.blockTarget = null;
			this.currentTarget = NONE;
		}

		this.attackTick = attackTickMax;
	}

	 public void jump(Vec3 jumpT0Pos){
		if (this.onGround()) {
			this.lookAt(EntityAnchorArgument.Anchor.EYES, jumpT0Pos);
			this.jumpTick = jumpTickMax;
			this.setDeltaMovement((this.getLookAngle().multiply(1.5, 1.5, 1.5)).add(0, 0.21, 0));
		}
	}

	public void shoot(Vec3 shootToPos){
		this.lookAt(EntityAnchorArgument.Anchor.EYES,shootToPos);

		this.setDeltaMovement((this.getLookAngle().multiply(1.6,1.6,1.6)).add(0,0.01,0));
	}

	public boolean shaIsNear() {
		Vec3 targetPos = this.getUser().position();
		double dist = Math.abs(targetPos.subtract(this.getX(), this.getY(), this.getZ()).length());

		boolean struck = this.getNavigation().isStuck() || this.struckTicks >= struckMaxTicks;

		return (dist <= 1.3f) || struck;
	}

	public void shaStopMove() {
		this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0);
		this.getNavigation().setSpeedModifier(0.0);
		this.getNavigation().stop();
	}


	public void shaMove(Vec3 targetPos) {
		ticksUntilNextPathRecalculation--;
		if (ticksUntilNextPathRecalculation <= 0 ) {
			ticksUntilNextPathRecalculation = 5; // + mob.getRandom().nextInt(7);

			Path newPath;
			if (this.haveToReturn) {
				newPath = this.getNavigation().createPath(this.getUser(), 1);
			}else if (this.currentTarget == ENTITY) {
				newPath = this.getNavigation().createPath(this.entityTarget, 0);
			}else if (this.currentTarget == BLOCK) {
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

			//this.lookAt(EntityAnchorArgument.Anchor.EYES, targetPos);

			if (newPath == null) {
				return;
			}



			if (!this.getNavigation().moveTo(newPath, 0.5f))
				ticksUntilNextPathRecalculation += 5;
		}
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

			MobType mobType = LE.getMobType();
			if (mobType.equals(MobType.UNDEAD)) { points -= 30;}
		}
		return points;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}

    @Override public boolean hurt(DamageSource source, float amount) { return false;}

	@Override public boolean isPickable() { return true;}
	@Override public boolean isPushedByFluid() { return true;}
	@Override public boolean hasNoPhysics() { return false;}
	@Override public boolean isNoGravity() { return false;}
	@Override public boolean isPushable() { return true;}
	@Override public boolean isAttackable() { return false;}
	@Override public boolean canBeHitByProjectile() { return true;}
	@Override public boolean canBeHitByStands() { return true;}
	@Override public boolean mayInteract(Level $$0, BlockPos pos) { return false;}
}