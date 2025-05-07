package net.hydra.jojomod.event.index;

public enum PlunderTypes {
    NONE((byte) 0),
    ITEM((byte) 1),
    MOBS((byte) 2),
    SIGHT((byte) 3),
    SOUND((byte) 4),
    FRICTION((byte) 5),
    OXYGEN((byte) 6),
    MOISTURE((byte) 7),
    POTION_EFFECTS((byte) 8);

    public final byte id;

    private PlunderTypes(byte $$0) {
        this.id = $$0;
    }

    public static net.hydra.jojomod.event.index.PlunderTypes getPlunderTypeDromByte(byte bt){
        if (bt== ITEM.id){
            return ITEM;
        } if (bt== MOBS.id){
            return MOBS;
        } if (bt== SIGHT.id){
            return SIGHT;
        } if (bt== SOUND.id){
            return SOUND;
        } if (bt== FRICTION.id) {
            return FRICTION;
        } if (bt== OXYGEN.id) {
            return OXYGEN;
        } if (bt== MOISTURE.id) {
            return MOISTURE;
        } if (bt== POTION_EFFECTS.id) {
            return POTION_EFFECTS;
        }
        return NONE;
    }
}
