package net.hydra.jojomod.event.index;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.minecraft.world.level.Level;


public enum Corpses {
    NONE((byte) 0),
    ZOMBIE((byte) 1),
    SKELETON((byte) 2),
    CREEPER((byte) 3),
    VILLAGER((byte) 4),
    SPIDER((byte) 5),
    PHANTOM((byte) 6);

    public final byte id;

    private Corpses(byte $$0) {
        this.id = $$0;
    }

    public static FallenMob getEntity(Corpses corpses, Level level){
        if (corpses.equals(ZOMBIE)){
            return ModEntities.FALLEN_ZOMBIE.create(level);
        } else if (corpses.equals(SKELETON)){
            return ModEntities.FALLEN_SKELETON.create(level);
        } else if (corpses.equals(SPIDER)){
            return ModEntities.FALLEN_SPIDER.create(level);
        } else if (corpses.equals(VILLAGER)){
            return ModEntities.FALLEN_VILLAGER.create(level);
        } else if (corpses.equals(CREEPER)){
            return ModEntities.FALLEN_CREEPER.create(level);
        } else if(corpses.equals(PHANTOM)){
            return ModEntities.FALLEN_PHANTOM.create(level);
        }
        return null;
    }

    public static Corpses getPosFromByte(byte bt){
        if (bt== ZOMBIE.id){
            return ZOMBIE;
        } if (bt== SKELETON.id){
            return SKELETON;
        } if (bt== SPIDER.id){
            return SPIDER;
        } if (bt== CREEPER.id){
            return CREEPER;
        } if (bt== VILLAGER.id) {
            return VILLAGER;
        } if(bt== PHANTOM.id){
            return PHANTOM;
        }
        return NONE;
    }

}
