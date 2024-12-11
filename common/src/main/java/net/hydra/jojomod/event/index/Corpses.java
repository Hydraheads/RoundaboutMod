package net.hydra.jojomod.event.index;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.minecraft.world.level.Level;


public enum Corpses {
    NONE((byte) 0),
    ZOMBIE((byte) 1),
    SKELETON((byte) 2),
    CREEPER((byte) 3),
    VILLAGER((byte) 4),
    SPIDER((byte) 5);

    public final byte id;

    private Corpses(byte $$0) {
        this.id = $$0;
    }

    public static FallenMob getEntity(Corpses corpses, Level level){
        if (corpses.equals(ZOMBIE)){
            return ModEntities.FALLEN_ZOMBIE.create(level);
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
        }
        return NONE;
    }

}
