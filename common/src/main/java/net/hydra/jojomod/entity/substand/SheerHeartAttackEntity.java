package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.PenetratableWithProjectile;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;

import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.util.ExplosionUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;


public class SheerHeartAttackEntity extends StandEntity {

	public SheerHeartAttackEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
		super($$0, $$1);
	}

	protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SheerHeartAttackEntity.class,
			EntityDataSerializers.INT);

	@Override
	protected PathNavigation createNavigation(Level $$0) {
		return new WallClimberNavigation(this, $$0);
	}

	public final AnimationState idle = new AnimationState();
	public final AnimationState moving = new AnimationState();

	int tickTargetFindCount = 0;
	static final int tickTargetFindMax = 4;

	int attackTick = 0;
	static final int attackTickMax = 35;
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
	public Vec3 blockTarget = null;
	public int ticksUntilNextPathRecalculation = 15;

	public float viewRange = 10.0f;

	private boolean haveToReturn = false;

	public boolean getHaveToReturn() { return this.haveToReturn;}
	public void setHaveToReturn(boolean value) { this.haveToReturn = value;}

	public static AttributeSupplier.Builder createStandAttributes() {
		return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
				0.25F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 5.0);
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

	@Override
	protected void registerGoals() {
		//super.registerGoals();
		//this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		//this.goalSelector.addGoal(5, new WarmestSeek(this));
		//this.goalSelector.addGoal(5, new TryToReturn(this));
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
		}

		return this.currentTarget != NONE;
	}

	public void findTarget() {
		List<Entity> entities = MainUtil.genHitbox(this.level(), this.getX(), this.getY(), this.getZ(), viewRange , viewRange , viewRange );
		int harmest = 0;
		double harmestDistance = -1;

		Entity targetEnt = null;

		for(int j = 0;j<entities.size();j++) {
			Entity entity = entities.get(j);
			double dist = entity.distanceToSqr(this.position());

			int points = 0;

			if (entity instanceof StandEntity || entity.is(this.getUser())){ continue;}

			if (entity instanceof LivingEntity LE) {
				if (LE.isDeadOrDying()) { continue; }
				if (LE instanceof Player pl) {
					if (pl.isCreative()) { continue; }
				}
				points += 20;
				points += HeatUtil.getHeat(LE);

				if (LE.isOnFire() || LE.wasOnFire || HeatUtil.isHot(LE)) { points += 70;}
				if (LE.isFullyFrozen()) { points -= 80;
				}else if (LE.isFreezing()) { points -= 40;}

				MobType mobType = LE.getMobType();
				if (mobType.equals(MobType.UNDEAD)) { points -= 30;}
			}
			if (points <= 0) {continue; }

			if (points > harmest) {
				harmest = points;
				harmestDistance = dist;
				targetEnt = entity;
			} else if (points == harmest ) {
				if (harmestDistance == -1 || dist < harmestDistance) {
					harmestDistance = dist;
					targetEnt = entity;
				}
			}
		}




		if (targetEnt != null) {
			this.entityTarget = targetEnt;
			this.currentTarget = ENTITY;
		}else {
			this.entityTarget = null;
			this.currentTarget = NONE;
		}
	}

	public boolean canAttack(Vec3 targetPos) {

		double dist = this.position().distanceTo(targetPos);
		float minDist = 4.0f;

		if (this.getTargetType() == BLOCK) {
			minDist = 1.3f;
		}

		return (dist <= minDist && this.attackTick <= 0);
	}

	public boolean shouldJump(Vec3 targetPos) {
		if (this.jumpTick <= 0) {
			return false;
		}
		double dist = this.position().distanceTo(targetPos);

		return dist > (explosionRadius+0.6f);
	}
	public boolean shouldExplode(Vec3 targetPos) {
		if (this.attackTick <= 0) {
			return false;
		}

		double dist = this.position().distanceTo(targetPos);

		return dist < (explosionRadius-0.1f);
	}

	public byte getTargetType() { return this.currentTarget;}

	public Vec3 getTargetPosition() {
		byte type = this.getTargetType();

		if (type != NONE) {
			Vec3 targetPos;
			if (type == BLOCK) {
				targetPos = this.blockTarget;
			}else {
				targetPos = this.entityTarget.position();
			}

			return targetPos;
		}
		return null;
	}


	public void attack() {

		DamageSource dmg = ModDamageTypes.of(this.level(), DamageTypes.PLAYER_EXPLOSION, this);;

		ExplosionUtil.explosionHurt(this.position(), dmg, this.level(),
				ClientNetworking.getAppropriateConfig().killerQueenSettings.SheerHeartAttackMaxDamage, 0.3f, explosionRadius);

		ExplosionUtil.explodeEffects(this.position(), this.level(), ModParticles.KILLER_QUEEN_EXPLOSION, 0.3f, 8);

		if (this.getTargetType() == ENTITY){
			if (!this.entityTarget.isAlive()) {
				this.entityTarget = null;
				this.currentTarget = NONE;
			}else if (this.entityTarget instanceof LivingEntity LE) {
				if (LE.isDeadOrDying()) {
					this.entityTarget = null;
					this.currentTarget = NONE;
				}
			}
		}else {
			this.blockTarget = null;
			this.currentTarget = NONE;
		}

		this.attackTick = attackTickMax;
	}

	 public void jump(Vec3 jumpT0Pos){
		if (this.onGround()) {
			this.lookAt(EntityAnchorArgument.Anchor.EYES, jumpT0Pos);
			this.jumpTick = jumpTickMax;
			this.setDeltaMovement((this.getLookAngle().multiply(1.2, 1.2, 1.2)).add(0, 0.14, 0));
		}
	}

	public void shoot(Vec3 shootToPos){
		this.lookAt(EntityAnchorArgument.Anchor.EYES,shootToPos);

		this.setDeltaMovement((this.getLookAngle().multiply(1.3,1.3,1.3)).add(0,0.12,0));
	}

	public boolean shaIsNear() {
		Vec3 targetPos = this.getUser().position();
		double dist = Math.abs(targetPos.subtract(this.getX(), this.getY(), this.getZ()).length());

		boolean struck = this.getNavigation().isStuck() || this.struckTicks >= struckMaxTicks;

		return (dist <= 1.3f) || struck;
	}

	public void shaStopMove() {
		this.getNavigation().stop();
	}

	public void shaMove(Vec3 targetPos) {
		ticksUntilNextPathRecalculation--;
		if (ticksUntilNextPathRecalculation <= 0 ) {
			ticksUntilNextPathRecalculation = 5; // + mob.getRandom().nextInt(7);

			Path newPath = this.getNavigation().createPath(targetPos.x, targetPos.y, targetPos.z, 2);
			if (newPath == null) { return;}

			if (!this.getNavigation().moveTo(newPath, 2.0f))
				ticksUntilNextPathRecalculation += 5;
		}
	}



    /*
	static class WarmestSeek extends Goal {
		protected final SheerHeartAttackEntity mob;

		public int ticksUntilNextPathRecalculation = 15;

		public WarmestSeek(SheerHeartAttackEntity $$0) {
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
			this.mob = $$0;
		}

		@Override
		public void start() { super.start();}

		@Override
		public void stop() {
			this.mob.getNavigation().stop();
			super.stop();
		}

		public void move(Vec3 targetPos) {

			ticksUntilNextPathRecalculation--;
			if (ticksUntilNextPathRecalculation <= 0 ) {
				ticksUntilNextPathRecalculation = 5; // + mob.getRandom().nextInt(7);
				Path newPath;

				newPath = this.mob.getNavigation().createPath(targetPos.x, targetPos.y, targetPos.z, 2);
				if (newPath == null) { return;}

				if (!this.mob.getNavigation().moveTo(newPath, 1.2f))
					ticksUntilNextPathRecalculation = 5;
			}
		}

		@Override
		public void tick() {

			byte type = this.mob.getTargetType();
			if (type != NONE) {
				Vec3 targetPos = this.mob.getTargetPosition();

				this.mob.lookAt(EntityAnchorArgument.Anchor.FEET,targetPos);

				if (!this.mob.canAttack(targetPos)) {
					this.move(targetPos);
				}else if (this.mob.attackTick <= 0) {
					if (this.mob.shouldJump(targetPos)) {
						if (type == ENTITY && this.mob.jumpTick <= 0) {
							this.mob.jump(targetPos);
						}
					} else  {
						this.mob.attack();
					}
				}
			}

			super.tick();
		}

		@Override public boolean canUse() { return this.mob.hasTarget() && !this.mob.getHaveToReturn(); }
		@Override public boolean canContinueToUse() { return this.mob.hasTarget() && !this.mob.getHaveToReturn();}
	}


	static class TryToReturn extends Goal {
		protected final SheerHeartAttackEntity mob;

		public int ticksUntilNextPathRecalculation = 15;

		public TryToReturn(SheerHeartAttackEntity $$0) {
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
			this.mob = $$0;
		}

		@Override
		public void start() { super.start();}

		@Override
		public void stop() {
			this.mob.getNavigation().stop();
			super.stop();
		}

		public void move(Vec3 targetPos) {
			ticksUntilNextPathRecalculation--;
			if (ticksUntilNextPathRecalculation <= 0 ) {
				ticksUntilNextPathRecalculation = 5; // + mob.getRandom().nextInt(7);

				Path newPath = this.mob.getNavigation().createPath(targetPos.x, targetPos.y, targetPos.z, 2);
				if (newPath == null) { return;}

				if (!this.mob.getNavigation().moveTo(newPath, 2.0f))
					ticksUntilNextPathRecalculation += 5;
			}
		}


		@Override
		public void tick() {
			if (this.mob != null) {
				LivingEntity user = this.mob.getUser();
				if (user != null) {
					if ((!(((StandUser)user).roundabout$getStandPowers() instanceof PowersKillerQueen)) || (!user.isAlive())) {
						Vec3 targetPos = user.position();

						if (!this.mob.shaIsNear()) {
							this.move(targetPos);
						}
					}
				}
			}
			super.tick();
		}

		@Override public boolean canUse() { return this.mob.getHaveToReturn();}
		@Override public boolean canContinueToUse() { return this.mob.getHaveToReturn();}
	} */
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