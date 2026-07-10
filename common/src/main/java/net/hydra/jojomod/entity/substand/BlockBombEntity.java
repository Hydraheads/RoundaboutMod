package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.PenetratableWithProjectile;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.StepRuleEntity;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.UUID;

import org.joml.Vector3f;

public class BlockBombEntity extends StandEntity {

	protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(BlockBombEntity .class,
			EntityDataSerializers.INT);


	public Entity userEntity;

	private BlockPos bombPos;
	private BlockState originalState;
	private static final int maxTickIndicator = 6;
	private int tickIndicator = maxTickIndicator;
	private Vec3 blockSize = new Vec3(1.0f, 1.0f, 1.0f);
	public int renderFadeIn = 1;

	public BlockBombEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
		super($$0, $$1);
	}

	public static final float dimensions = 1F;

	@Override
	public void push(Entity $$0) {
	}

	protected MovementEmission getMovementEmission() {
		return MovementEmission.NONE;
	}

	public float distanceToClearWhileTicked(){
		return 0.3f;
	}
	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;

	public boolean updated = false;

	@Override
	public void lerpTo(double $$0, double $$1, double $$2, float $$3, float $$4, int $$5, boolean $$6) {
		this.lerpX = $$0;
		this.lerpY = $$1;
		this.lerpZ = $$2;
		this.setRot($$3, $$4);
		this.lerpSteps = $$5;
	}
	
	public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.0F).add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ATTACK_DAMAGE, 0.0);
    }
	
	public void setBlockPos(BlockPos pos) {
		if (pos != null) {
			this.bombPos = pos;
			this.originalState = this.level().getBlockState(pos);
			/*if (this.originalState.hasBlockEntity()) {
				this.blockInfo = this.level().getBlockEntity(pos);
			}*/
			Vec3 positionInWorld = this.bombPos.getCenter().add(0.0F, -0.5F, 0.0F);
			this.getBlockSize();
			
			this.setPos(positionInWorld);
			
		}
	}
	
	public BlockPos getBlockPos() { return this.bombPos;}
	
	public boolean blockGotDestroyed() {
		BlockState state = this.level().getBlockState(this.bombPos);
		return state.isAir();
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
            }
            else{
				if ((((StandUser)user).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ)) {
					if (this != PKQ.bombBlock) {
						this.discard();
						return;
					}
				}
				this.setYRot(0f);
				this.setYBodyRot(0);
            	
            	//this.detectInside();
            }
		
        }else {
	        if (this.tickIndicator > 0 && this.tickIndicator % 2 == 0) {
				Vec3 pos = this.getPosition(0).add(0, 0.5f, 0);

				this.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.02F, 0.02F, 0.04F), 2.5f),
						pos.x,
						pos.y + 1.0f,
						pos.z,
						0, 1.2, 0);
				this.tickIndicator--;
			}
			if (this.renderFadeIn < 12) {
				this.renderFadeIn++;
			}
		}
        super.tick();
		refreshDimensions();
    }

	
	public void getBlockSize() {
		AABB shape;
		VoxelShape voxShape = this.originalState.getCollisionShape(getCommandSenderWorld(), bombPos, null);
		
		if (voxShape.isEmpty()) {
			voxShape = this.originalState.getVisualShape(getCommandSenderWorld(), bombPos, null);
		}
		if (!voxShape.isEmpty()) {
			shape = voxShape.bounds();
			this.blockSize = new Vec3(shape.maxX, shape.maxY, shape.maxZ);
		}
	}
	
	/*
	public void detectInside() {
		BlockState test = this.level().getBlockState(this.bombPos);
		if (test.hasBlockEntity() && !test.equals(this.originalState)) {
			BlockEntity info = this.level().getBlockEntity(this.bombPos);
			Roundabout.LOGGER.info("Hmmmmmm?");
			
			if (info.getBlockState().equals(this.originalState)) {
				this.blockInfo = info; 
				Roundabout.LOGGER.info("Hmmmmmm");
			}
		}
		
	}*/

	@Override
	public boolean canAttack(LivingEntity le){
		super.canAttack(le);
		return false;
	}
	@Override
	public boolean canBeSeenAsEnemy() {return false; }

	@Override
	public boolean canBeAffected(MobEffectInstance $$0) {
		return false;
	}

	@Override
    public boolean isPickable() { return false;}

    @Override
    public boolean isInvulnerable() { return true;}	
	
	@Override
    public boolean canBeCollidedWith() { return false;}

    @Override
    public boolean isPushedByFluid() { return false;}
    
    @Override
    public boolean isNoGravity() { return true;}

    @Override
    public boolean canCollideWith(Entity $$0) { return false;}

    @Override
    public boolean hurt(DamageSource source, float amount) { return false;}
	
    @Override
    public boolean canBeHitByProjectile() { return false;}
    
    //@Override
    //public boolean canBeHitByStands() { return false;}
    
    @Override
    public boolean mayInteract(Level $$0, BlockPos pos) { return false;}
    

    //@Override
    /*public boolean forceVisualRotation(){
        return true;
    }*/
}