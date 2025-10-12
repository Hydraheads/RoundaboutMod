package net.hydra.jojomod.event.index;

import net.minecraft.client.animation.AnimationDefinition;

public enum Poses {
    NONE((byte) 0,null),
    GIORNO((byte) 1, PoseAnimations.Giorno),
    JOSEPH((byte) 2, PoseAnimations.Joseph),
    KOICHI((byte) 3,PoseAnimations.Koichi),

    WRY((byte) 4, PoseAnimations.WRRRYYY),
    OH_NO((byte) 5, PoseAnimations.OH_NO),

    TORTURE_DANCE((byte) 6,PoseAnimations.TORTURE_DANCE),
    WAMUU((byte) 7,PoseAnimations.Wamuu),

    JOTARO((byte) 8, PoseAnimations.JOTARO),
    JONATHAN((byte) 9, PoseAnimations.Jonathan),
    WATCH((byte) 10, PoseAnimations.watch),
    SITTING((byte) 11, PoseAnimations.SITTING),
    VAMPIRE_TRANSFORMATION((byte) 12, PoseAnimations.VAMPIRE),
    SITTING_CHAIR((byte) 12, PoseAnimations.SITTING);

    public final byte id;

    public final AnimationDefinition ad;
    private Poses(byte $$0, AnimationDefinition ad) {
        this.id = $$0;
        this.ad = ad;
    }


    public static Poses getPosFromByte(byte bt){
        if (bt== GIORNO.id){
            return GIORNO;
        } if (bt== JOSEPH.id){
            return JOSEPH;
        } if (bt== KOICHI.id){
            return KOICHI;
        } if (bt== WRY.id){
            return WRY;
        } if (bt== OH_NO.id){
            return OH_NO;
        } if (bt== TORTURE_DANCE.id){
            return TORTURE_DANCE;
        } if (bt== WAMUU.id){
            return WAMUU;
        } if (bt== JOTARO.id){
            return JOTARO;
        } if (bt== JONATHAN.id){
            return JONATHAN;
        } if (bt== WATCH.id){
            return WATCH;
        } if (bt== SITTING.id) {
            return SITTING;
        } if (bt== VAMPIRE_TRANSFORMATION.id) {
            return VAMPIRE_TRANSFORMATION;
        }
        return NONE;
    }

}
