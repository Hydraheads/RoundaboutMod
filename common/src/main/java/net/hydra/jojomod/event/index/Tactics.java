package net.hydra.jojomod.event.index;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.minecraft.world.level.Level;


public enum Tactics {
    NONE((byte) 0),
    SELECT_ALL((byte) 1),
    DESELECT_ALL((byte) 2),
    STAY_PUT((byte) 3),
    ROAM((byte) 4),
    FOLLOW((byte) 5),
    DEFEND((byte) 6),
    HUNT_TARGET((byte) 7),
    HUNT_MONSTERS((byte) 8),
    HUNT_PLAYERS((byte) 9),
    PEACEFUL((byte) 10),
    CHANGE_TEAM((byte) 11),
    KILL_ALL((byte) 12),
    CACKLE((byte) 13);

    public final byte id;

    private Tactics(byte $$0) {
        this.id = $$0;
    }


}
