package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class KillerQueenEntity extends FollowingStandEntity {

    public KillerQueenEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    protected static final EntityDataAccessor<Integer> PLANTED_BITES_THE_DUST = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.entityData.hasItem(PLANTED_BITES_THE_DUST)) {
            this.entityData.define(PLANTED_BITES_THE_DUST, -1);
        }
    }

    public void setPlantedBitesTheDust(LivingEntity entity) {
        if (entity == null) {
            this.entityData.set(PLANTED_BITES_THE_DUST, -1);
        }else {
            int id = entity.getId();
            this.entityData.set(PLANTED_BITES_THE_DUST, id);
        }
    }
    public Entity getPlantedBitesTheDust() {
        int id = this.entityData.get(PLANTED_BITES_THE_DUST);
        if (id != -1) {
            return this.level().getEntity(id);
        }
        return null;
    }

    public static Component getSkinNameT(byte skinId) {
        switch (skinId)
        {
	        case KillerQueenEntity.PART_4 -> {return Component.translatable("skins.roundabout.killer_queen.anime");}
	        case KillerQueenEntity.GOGO -> {return Component.translatable("skins.roundabout.killer_queen.agogo");}
	        case KillerQueenEntity.MANGA -> {return Component.translatable("skins.roundabout.killer_queen.manga");}
	        case KillerQueenEntity.UMBRA -> {return Component.translatable("skins.roundabout.killer_queen.umbra");}
	        case KillerQueenEntity.ARTWORK -> {return Component.translatable("skins.roundabout.killer_queen.artwork");}
	        case KillerQueenEntity.CRACKED -> {return Component.translatable("skins.roundabout.killer_queen.cracked");}
	        case KillerQueenEntity.CREEPER -> {return Component.translatable("skins.roundabout.killer_queen.creeper");}
	        case KillerQueenEntity.STRAY -> {return Component.translatable("skins.roundabout.killer_queen.stray");}
	        case KillerQueenEntity.NIGHTMARE -> {return Component.translatable("skins.roundabout.killer_queen.nightmare");}
	        case KillerQueenEntity.LIMBUSMORTIS -> {return Component.translatable("skins.roundabout.killer_queen.mortis");}
	        case KillerQueenEntity.JOJOLION -> {return Component.translatable("skins.roundabout.killer_queen.jojolion");}
	        case KillerQueenEntity.GUNPOWDER -> {return Component.translatable("skins.roundabout.killer_queen.gunpowder");}
	        case KillerQueenEntity.FINAL -> {return Component.translatable("skins.roundabout.killer_queen.final");}
	        case KillerQueenEntity.DEADLY -> {return Component.translatable("skins.roundabout.killer_queen.deadly");}
	        case KillerQueenEntity.YELLOW -> {return Component.translatable("skins.roundabout.killer_queen.yellow");}
	        case KillerQueenEntity.TAMA -> {return Component.translatable("skins.roundabout.killer_queen.tama");}
	        case KillerQueenEntity.MINESWEEPER -> {return Component.translatable("skins.roundabout.killer_queen.minesweeper");}
	        case KillerQueenEntity.NOTW -> {return Component.translatable("skins.roundabout.killer_queen.notw");}
	        case KillerQueenEntity.MEMENTO -> {return Component.translatable("skins.roundabout.killer_queen.memento");}
	        case KillerQueenEntity.STARDUST -> {return Component.translatable("skins.roundabout.killer_queen.stardust");}
	    }
        return Component.translatable("skins.roundabout.killer_queen.anime");
    }

    public static final byte
            PART_4 = 0,
    		MANGA = 1,
			UMBRA = 2,
    		GOGO = 3,
    		ARTWORK = 4,
    		CRACKED = 5,
    		CREEPER = 6,
    		STRAY = 7,
    		NIGHTMARE = 8,
    		LIMBUSMORTIS = 9,
    		JOJOLION = 10,
    		GUNPOWDER = 11,
    		FINAL = 12,
    		DEADLY = 13,
    		YELLOW = 14,
    		TAMA = 15,
    		MINESWEEPER = 16,
    		NOTW = 17,
    		MEMENTO = 18,
    		STARDUST = 19;

    public final AnimationState lid_open = new AnimationState();
    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();
    public final AnimationState finalKickWindup = new AnimationState();
    public final AnimationState finalKick = new AnimationState();
    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState blockPlant = new AnimationState();
    public final AnimationState itemGrab = new AnimationState();
    public final AnimationState itemThrow = new AnimationState();
    public final AnimationState detonate = new AnimationState();
    public final AnimationState thirdBomb = new AnimationState();
    public final AnimationState mobBombPlant= new AnimationState();
    public final AnimationState mobBombPlant2 = new AnimationState();
    public final AnimationState bubbleLaunch = new AnimationState();
    public final AnimationState bubbleRedirect= new AnimationState();
    public final AnimationState heavyStrike = new AnimationState();
    public final AnimationState shaSend = new AnimationState();
    public final AnimationState impale = new AnimationState();
    public final AnimationState bitesTheDust = new AnimationState();

    public final AnimationState itemThrowAnimation = new AnimationState();
    public final AnimationState blockThrowAnimation = new AnimationState();

    public static byte
		KICK = 25,
		KICK_CHARGE = 27,
    	DETONATE = 121,
    	BLOCK_PLANT = 122,
        MOB_PLANT = 123,
        MOB_PLANT_2 = 124,
        BUBBLE_SEND = 125,
        BITES_THE_DUST_FOLLOW = 126,
        SHA_SEND = 88,
    	HEAVY_STRIKE = 26;
    
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            byte animation = this.getAnimation();

            if (animation != BARRAGE) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }

            if (animation != KICK_BARRAGE) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
            }


            if (animation == KICK_BARRAGE_WINDUP) {
                this.kick_barrage_windup.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_windup.stop();
            }

            if (animation == KICK_BARRAGE_END) {
                this.kick_barrage_end.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_end.stop();
            }
            
            if (animation == DETONATE) {
            	this.detonate.startIfStopped(this.tickCount);
            } else {
                this.detonate.stop();
            }
            
            if (animation == BLOCK_PLANT) {
            	this.blockPlant.startIfStopped(this.tickCount);
            } else {
                this.blockPlant.stop();
            }
            
            if (animation == KICK) {
            	this.finalKick.startIfStopped(this.tickCount);
            } else {
                this.finalKick.stop();
            }
            
            if (animation == KICK_CHARGE) {
            	this.finalKickWindup.startIfStopped(this.tickCount);
            } else {
                this.finalKickWindup.stop();
            }
            if (animation == HEAVY_STRIKE) {
            	this.heavyStrike.startIfStopped(this.tickCount);
            } else {
                this.heavyStrike.stop();
            }
            if (animation == SHA_SEND) {
                this.shaSend.startIfStopped(this.tickCount);
            } else {
                this.shaSend.stop();
            }

            if (animation == MOB_PLANT) {
                this.mobBombPlant.startIfStopped(this.tickCount);
            } else {
                this.mobBombPlant.stop();
            }

            if (animation == MOB_PLANT_2) {
                this.mobBombPlant2.startIfStopped(this.tickCount);
            } else {
                this.mobBombPlant2.stop();
            }

            if (animation == IMPALE) {
                this.impale.startIfStopped(this.tickCount);
            } else {
                this.impale.stop();
            }

            if (animation == BUBBLE_SEND) {
                this.bubbleLaunch.startIfStopped(this.tickCount);
            } else {
                this.bubbleLaunch.stop();
            }

            if (animation == ITEM_THROW) {
                this.itemThrowAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemThrowAnimation.stop();
            }

            if (animation == BLOCK_THROW) {
                this.blockThrowAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockThrowAnimation.stop();
            }

            if (animation == BITES_THE_DUST_FOLLOW) {
                this.bitesTheDust.startIfStopped(this.tickCount);
            } else {
                this.bitesTheDust.stop();
            }
        }
    }


    public Vec3 getBitesTheDustOffset(LivingEntity standUser) {
        double r = 0.7;

        double yawfix = standUser.getYRot();
        yawfix += this.getAnchorPlace() + 125;
        if (yawfix > 360) {
            yawfix -= 360;
        } else if (yawfix < 0) {
            yawfix += 360;
        }
        double ang = (yawfix - 180) * Math.PI;

        double mcap = 0.3;
        Vec3 xyz = standUser.getDeltaMovement();
        double yy = xyz.y() * 0.3;
        if (yy > mcap) {
            yy = mcap;
        } else if (yy < -mcap) {
            yy = -mcap;
        }
        if (isSwimming() || isVisuallyCrawling() || isFallFlying()) {
            yy += 1;
        }


        Direction dir = ((IGravityEntity)standUser).roundabout$getGravityDirection();
        Vec3 offset = new Vec3(
                (- (-1 * (r * (Math.sin(ang / 180))))),
                (0.9f - yy),
                (-(r * (Math.cos(ang / 180))))
        );
        if (dir != Direction.DOWN){
            offset = RotationUtil.vecPlayerToWorld(offset,dir);
        }

        double x1 = standUser.getX() +offset.x;
        double y1 = standUser.getY() +offset.y;
        double z1 = standUser.getZ() +offset.z;

        return new Vec3(x1, y1, z1);
    }


    /** unused, unless someone fix the followOffset to work with following
    /*
    @Override
    public Vec3 getStandOffsetVector(LivingEntity standUser){

        if (((StandUser)standUser).roundabout$getStandPowers() instanceof PowersKillerQueen KQ && KQ.inBitesTheDustMode()) {
            /**
             * For some reason the "this.getFollowing()" will always return null
             *
            return getIdleOffset(this.getFollowing());
        }

        return super.getStandOffsetVector(standUser);
    }*/
}
