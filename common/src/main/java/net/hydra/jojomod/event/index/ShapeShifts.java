package net.hydra.jojomod.event.index;

import net.minecraft.network.chat.Component;

public enum ShapeShifts {
    PLAYER((byte) 0),
    VILLAGER((byte) 1),

    OVA((byte) 2),
    ZOMBIE((byte) 3),
    SKELETON((byte) 4);

    public final byte id;
    private ShapeShifts(byte $$0) {
        this.id = $$0;
    }

    public static ShapeShifts getShiftFromByte(byte bt){
        if (bt== VILLAGER.id){
            return VILLAGER;
        } if (bt== OVA.id){
            return OVA;
        } if (bt== ZOMBIE.id){
            return ZOMBIE;
        } if (bt== SKELETON.id){
            return SKELETON;
        }
        return PLAYER;
    }
}
