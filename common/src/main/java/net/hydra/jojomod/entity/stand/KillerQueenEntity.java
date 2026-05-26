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
	        case KillerQueenEntity.MEMENTO -> {return Component.translatable("skins.roundabout.killer_queen.final");}
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
    
    public static byte
		KICK = 25,
		KICK_CHARGE = 27,
    	DETONATE = 121,
    	BLOCK_PLANT = 122,
    	HEAVY_STRIKE = 26;
    
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
            if (this.getAnimation() == HEAVY_STRIKE) {
            	this.heavyStrike.startIfStopped(this.tickCount);
            } else {
                this.heavyStrike.stop();
            }
        }
    }
}
