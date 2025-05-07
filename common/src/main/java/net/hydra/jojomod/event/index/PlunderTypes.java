package net.hydra.jojomod.event.index;

public enum PlunderTypes {
    NONE((byte) -1),
    ITEM((byte) 0),
    MOBS((byte) 1),
    SIGHT((byte) 2),
    SOUND((byte) 3),
    FRICTION((byte) 4),
    OXYGEN((byte) 5),
    MOISTURE((byte) 6),
    POTION_EFFECTS((byte) 7);

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
