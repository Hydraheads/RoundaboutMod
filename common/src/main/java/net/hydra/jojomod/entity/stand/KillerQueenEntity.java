package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class KillerQueenEntity extends FollowingStandEntity {



    public KillerQueenEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static Component getSkinNameT(byte skinId) {
        switch (skinId)
        {
            case PART_4 -> {return Component.translatable("skins.roundabout.killer_queen.base");}
        }
        return Component.translatable("skins.roundabout.killer_queen.base");
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
    		YELLOW = 14;

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
    
    public static byte
		KICK = 25,
		KICK_CHARGE = 27,
    	DETONATE = 121,
    	BLOCK_PLANT = 122;
    
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {

            if (this.getAnimation() != BARRAGE) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }

            if (this.getAnimation() != KICK_BARRAGE) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
            }


            if (this.getAnimation() == KICK_BARRAGE_WINDUP) {
                this.kick_barrage_windup.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_windup.stop();
            }

            if (this.getAnimation() == KICK_BARRAGE_END) {
                this.kick_barrage_end.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_end.stop();
            }
            
            if (this.getAnimation() == DETONATE) {
            	this.detonate.startIfStopped(this.tickCount);
            } else {
                this.detonate.stop();
            }
            
            if (this.getAnimation() == BLOCK_PLANT) {
            	this.blockPlant.startIfStopped(this.tickCount);
            } else {
                this.blockPlant.stop();
            }
            
            if (this.getAnimation() == KICK) {
            	this.finalKick.startIfStopped(this.tickCount);
            } else {
                this.finalKick.stop();
            }
            
            if (this.getAnimation() == KICK_CHARGE) {
            	this.finalKickWindup.startIfStopped(this.tickCount);
            } else {
                this.finalKickWindup.stop();
            }
        }
    }
}
