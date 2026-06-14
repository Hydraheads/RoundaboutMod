package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.PenetratableWithProjectile;
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
	
	
	private BlockPos bombPos;
	//private BlockEntity blockInfo;
	private BlockState originalState;
	private static final int maxTickIndicator = 6;
	private int tickIndicator = maxTickIndicator;
	private Vec3 blockSize = new Vec3(1.0f, 1.0f, 1.0f);
	
	public BlockBombEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
		
		super($$0, $$1);
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
				this.setYRot(0f);
            	if (this.tickIndicator > 0 && this.tickIndicator % 2 == 0){   
	            	Vec3 pos = bombPos.getCenter();
	            	
	            	
	            	((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(0.02F, 0.02F, 0.04F), 2.5f),
	            			pos.x,
	                        pos.y+1.0f,
	                        pos.z,
	                        2, 0, 0, 0, 1.2);
	            	this.tickIndicator--;
	            }
            	
            	//this.detectInside();
            }
		
        }
        super.tick();
    }
	
	public Entity detectContact() {
		Vec3 pos = this.bombPos.getCenter();
		float skinSize = 0.10f;

		
		List<Entity> entitiesDetect = MainUtil.genHitbox(this.level(), pos.x(), pos.y(), pos.z(),
				this.blockSize.x() + skinSize, this.blockSize.y() + skinSize, this.blockSize.z() + skinSize);

		double distRecord = -1.0;
		Entity blowTarget = null;

		for (Entity entity : entitiesDetect) {
			if (entity.equals(this.getUser()) || entity.equals(((StandUser)this.getUser()).roundabout$getStand()) || entity.equals(this)
				|| entity instanceof StandEntity || !(entity instanceof LivingEntity)) {
				continue;
			}


			double dist = MainUtil.cheapDistanceTo(
					this.getX(),
					this.getY(),
					this.getZ(),
					entity.getX(),
					entity.getY(),
					entity.getZ()
			);

			if (distRecord == -1 || dist < distRecord) {
				blowTarget = entity;
				distRecord = dist;
			}
		}

		return blowTarget;
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
    protected AABB makeBoundingBox() { return super.makeBoundingBox();}
	
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
    
    @Override
    public boolean canBeHitByStands() { return false;}
    
    @Override
    public boolean mayInteract(Level $$0, BlockPos pos) { return false;}
    

    @Override
    public boolean forceVisualRotation(){
        return true;
    }
  
}