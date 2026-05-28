package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.PenetratableWithProjectile;
import net.hydra.jojomod.entity.goals.TerrierBegGoal;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.hydra.jojomod.entity.mobs.AnubisGuardian.AnubisAttacks;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;

import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.UUID;

import org.joml.Vector3f;

public class SheerHeartAttackEntity extends StandEntity {
	
	protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SheerHeartAttackEntity .class,
            EntityDataSerializers.INT);
	
	private int tickTargetFindCount = 0;
	static final int tickTargetFindMax = 10;
	
	static final byte
		NONE = 0,
		BLOCK = 1,
		ENTITY = 2;
	
	public byte currentTarget = NONE;
	public Entity entityTarget = null;
	public Vec3 blockTarget = null;
	
	public float viewRange = 20.0f;
	
	public SheerHeartAttackEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
		
		super($$0, $$1);
	}
	
	public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.5F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }
	
	
	
	@Override
    public void tick() {
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
            }
        }
      this.aiStep();
      this.getNavigation().tick();
      //this.moveControl.tick();
     
      
      
      //this.lookControl.tick();
      //this.jumpControl.tick();
       //super.tick();
    }
	
	@Override
    protected void registerGoals() {
        super.registerGoals();
        //this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new WarmestSeek(this));    
        //this.goalSelector.getAvailableGoals().removeIf(e -> e.getPriority() == 9);
        //this.goalSelector.addGoal(9, new TerrierBegGoal(this, 8.0f));
    }
	
	
	static class WarmestSeek extends TargetGoal {
		protected final SheerHeartAttackEntity mob;
		
		public int ticksUntilNextPathRecalculation = 15;
	   
		float pathedTargetX;
        float pathedTargetY;
        float pathedTargetZ;
		
	    public WarmestSeek(SheerHeartAttackEntity $$0) {
	        super($$0, true);
	        this.mob = $$0;
	    }

	    @Override
	    public void start() {
	        super.start();
	        
	    }

	    @Override
	    public void stop() {
	        super.stop();
	        //this.mob.setAction(AnubisAttacks.NONE);
	    }
	    
	    public void move(Vec3 targetPos) {
            
	    	float x = (float)targetPos.x();
            float y = (float)targetPos.y();
            float z = (float)targetPos.z();
            float hx = x + 0.5f;
            float hy = y + 0.5f;
            float hz = z + 0.5f;
            this.mob.getLookControl().setLookAt(hx, hy, hz, 30.0F, 30.0F);
            double distSqr = this.mob.distanceToSqr(hx, hy, hz);
            ticksUntilNextPathRecalculation--;
            if (ticksUntilNextPathRecalculation <= 0 ) {
	            /*pathedTargetX = hx;
	            pathedTargetY = hy;
	            pathedTargetZ = hz;*/
	            ticksUntilNextPathRecalculation = 4 + mob.getRandom().nextInt(7);
	            if (distSqr > 1024.0D) ticksUntilNextPathRecalculation += 10;
	            else if (distSqr > 256.0D) ticksUntilNextPathRecalculation += 5;
	            Path newPath = mob.getNavigation().createPath(targetPos.x, targetPos.y, targetPos.z, 0);
	            if (newPath != null && !newPath.canReach()) {
	                Roundabout.LOGGER.info("cant reach");
	                return;
	            }
	            if (!mob.getNavigation().moveTo(newPath, 1.0f))
	            	ticksUntilNextPathRecalculation += 15;
	            
            	//ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation);
            }
            //ticksUntilNextAttack = Math.max(getTicksUntilNextAttack() - 1, 0);
            //checkAndPerformAttack(mob.buttonTarget, distSqr);
            
	        
	    }

	    public boolean canAttack(Vec3 targetPos) {
	    	
	    	double dist = MainUtil.cheapDistanceTo(this.mob.getX(), this.mob.getY(), this.mob.getZ(), targetPos.x, targetPos.y, targetPos.z);
	    	
	    	//Roundabout.LOGGER.info("target distance: " + dist);    	
	    	return dist <= 4.2f;
	    }

	    @Override
	    public void tick() {
	        
	        byte type = this.mob.getTargetType();
	    	if (type != NONE) {
	    		Vec3 targetPos;
		    	if (type == BLOCK) {
		    		targetPos = this.mob.blockTarget;
		    	}else {
		    		targetPos = this.mob.entityTarget.position();
		    	}
		    	
		    	
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
	        return this.mob.hasTarget() && this.mob.getTargetType() != NONE;
	    	//return super.canUse() && this.mob.getAction().equals(AnubisAttacks.SWORD_RUSH);
	    }

	    @Override
	    public boolean canContinueToUse() {
	    	return this.mob.hasTarget() && this.mob.getTargetType() != NONE;
	        //return super.canContinueToUse() && this.mob.getAction().equals(AnubisAttacks.SWORD_RUSH);
	    }
		
	}
	
	public boolean hasTarget() {
		
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
            int points = 2;
            
            if (entity instanceof StandEntity || entity.is(this.getUser())){
            	continue;
            }
            
            
            if (entity instanceof LivingEntity LE) {
            	if (LE.isDeadOrDying()) { continue; }
            	
            	if (LE.isOnFire() || LE.wasOnFire) {
            		points += 7;
            	}else if (LE.isFullyFrozen()){
            		points -= 8;
            	}else if (LE.isFreezing()){
            		points -= 4;
            	}
            	MobType mobType = LE.getMobType();
            	if (mobType.equals(MobType.UNDEAD)) {
            		points -= 4;
            	}
            	
            }
            
            if (points > harmest) {
            	points = harmest;
            	harmestDistance = dist;
            	targetEnt = entity;
            }else if (points == harmest ) {
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
	
	public byte getTargetType() {
		
		return this.currentTarget;
	}
	
	public void attack(Vec3 position) {
		Roundabout.LOGGER.info("LOOK BEHIND YOU!");
	}
	
	@Override
    public boolean isPickable() { return true;}
	
    @Override
    public boolean isInvulnerable() { return true;}	
	/*
	@Override public boolean canBeCollidedWith() { return true;}
    @Override public boolean canCollideWith(Entity $$0) { return true;}
    */
    
    @Override
    public boolean isPushedByFluid() { return true;}
    
    @Override
    public boolean isNoGravity() { return false;}



    @Override
    public boolean hurt(DamageSource source, float amount) {
    	return false;
    }
	
    @Override
    public boolean canBeHitByProjectile() { return true;}
    
    @Override
    public boolean canBeHitByStands() { return true;}
    
    @Override
    public boolean mayInteract(Level $$0, BlockPos pos) { return false;}
    
    @Override public boolean forceVisualRotation() { return true; }
  
}