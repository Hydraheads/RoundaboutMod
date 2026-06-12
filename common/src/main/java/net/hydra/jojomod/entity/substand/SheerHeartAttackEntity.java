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
	static final int attackTickMax = 15;

	static final byte
		NONE = 0,
		BLOCK = 1,
		ENTITY = 2;

	public byte currentTarget = NONE;
	public Entity entityTarget = null;
	public Vec3 blockTarget = null;

	public float viewRange = 10.0f;

	private boolean haveToReturn = false;

	public boolean getHaveToReturn() { return this.haveToReturn;}
	public void setHaveToReturn(boolean value) { this.haveToReturn = value;}

	public static AttributeSupplier.Builder createStandAttributes() {
		return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
				0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 5.0);
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

				if (this.hasTarget()) {
					this.lookAt(EntityAnchorArgument.Anchor.FEET,this.getTargetPosition());
				}else if (!this.haveToReturn ){
					this.getNavigation().stop();
				}else {
					this.getNavigation().moveTo(this.getUser(), 1.5f);
				}

			}
		}

		super.tick();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(2, new WarmestSeek(this));
		//this.goalSelector.addGoal(1, new TryToReturn(this));
	}

	public boolean hasTarget() { return this.currentTarget != NONE;}

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
				points = harmest;
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


	public void attack(Vec3 position) {

		this.jump(position);

		DamageSource dmg = ModDamageTypes.of(this.level(), DamageTypes.PLAYER_EXPLOSION, this);;

		ExplosionUtil.explosionHurt(this.position(), dmg, this.level(),
				ClientNetworking.getAppropriateConfig().killerQueenSettings.explosionDetonateMaxDamage, 0.3f, 1.3f);

		ExplosionUtil.explodeEffects(this.position(), this.level(), ModParticles.KILLER_QUEEN_EXPLOSION, 0.3f, 12);

		if (this.getTargetType() == ENTITY){
			if (!this.entityTarget.isAlive()) {
				this.entityTarget = null;
				this.currentTarget = NONE;
			}
		}else {
			this.blockTarget = null;
			this.currentTarget = NONE;
		}

		this.attackTick = attackTickMax;
	}

	 public void jump(Vec3 jumpT0Pos){
			//Vec3 location = new Vec3(this.getX(),this.getY(),this.getZ());

			//this.setDeltaMovement(jumpT0Pos);
		this.lookAt(EntityAnchorArgument.Anchor.EYES,jumpT0Pos);

		this.setDeltaMovement((this.getLookAngle().multiply(1.5,1.5,1.5)).add(0,0.75,0));
	}


	@Override
	public boolean hurt(DamageSource source, float amount) {
		/*Entity entity = source.getEntity();

		if (entity != null) {
			if (this.equals(entity)) {
				return false;
			}
			if (entity instanceof StandEntity SE) {
				if (SE.getUser().equals(this.getUser())) {
					return false;
				}
			}
			if (entity.equals(this.getUser())) {
				return false;
			}
		}*/
		//return super.hurt(source, amount*0.1f);
		return false;
	}


	public boolean shaIsNear() {
		Vec3 targetPos = this.getUser().position();
		double dist = Math.abs(targetPos.subtract(this.getX(), this.getY(), this.getZ()).length());

		boolean struck = this.getNavigation().isStuck();

		return (dist <= 1.3f) || struck;
	}


	static class WarmestSeek extends TargetGoal {
		protected final SheerHeartAttackEntity mob;

		public int ticksUntilNextPathRecalculation = 15;

		public WarmestSeek(SheerHeartAttackEntity $$0) {
			super($$0, true);
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

				if (!mob.getNavigation().moveTo(newPath, 1.2f))
					ticksUntilNextPathRecalculation = 5;
			}
		}

		public boolean canAttack(Vec3 targetPos) {
			double dist = MainUtil.cheapDistanceTo(this.mob.getX(), this.mob.getY(), this.mob.getZ(), targetPos.x, targetPos.y, targetPos.z);

			return (dist <= 1.7f && this.mob.attackTick <= 0);
		}

		@Override
		public void tick() {

			byte type = this.mob.getTargetType();
			if (type != NONE) {
				Vec3 targetPos = this.mob.getTargetPosition();

				if (!this.canAttack(targetPos)) {
					this.move(targetPos);
				}else {
					this.mob.attack(targetPos);
				}
			}

			super.tick();
		}

		@Override
		public boolean canUse() {
			return this.mob.hasTarget() && !this.mob.getHaveToReturn();
		}

		@Override
		public boolean canContinueToUse() { return this.mob.hasTarget() && !this.mob.getHaveToReturn();}

	}


	static class TryToReturn extends TargetGoal {
		protected final SheerHeartAttackEntity mob;

		public int ticksUntilNextPathRecalculation = 15;

		public TryToReturn(SheerHeartAttackEntity $$0) {
			super($$0, true);
			this.mob = $$0;
		}

		@Override
		public void start() { super.start();}

		@Override
		public void stop() { super.stop();}

		public void move(Vec3 targetPos) {
			ticksUntilNextPathRecalculation--;
			if (ticksUntilNextPathRecalculation <= 0 ) {

				ticksUntilNextPathRecalculation = 5; // + mob.getRandom().nextInt(7);

				Path newPath = this.mob.getNavigation().createPath(targetPos.x, targetPos.y, targetPos.z, 0);
				if (newPath == null) { return;}

				if (!mob.getNavigation().moveTo(newPath, 2.0f))
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
	}

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