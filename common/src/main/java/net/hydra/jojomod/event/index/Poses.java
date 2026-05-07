package net.hydra.jojomod.event.index;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.models.layers.animations.FirearmFirstPersonAnimations;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.player.Player;

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

    SITTING_CHAIR((byte) 12, PoseAnimations.SITTING),

    SNUBNOSE_AIM((byte) 13, FirearmFirstPersonAnimations.snubnose_aim),
    SNUBNOSE_RECOIL((byte) 14, FirearmFirstPersonAnimations.snubnose_fire_recoil),
    SNUBNOSE_MODEL_AIM((byte) 15, FirearmFirstPersonAnimations.snubnose_model_aim),
    SNUBNOSE_MODEL_RECOIL((byte) 16, FirearmFirstPersonAnimations.snubnose_model_recoil),
    SNUBNOSE_AIM_LEFT((byte) 17, FirearmFirstPersonAnimations.snubnose_aim_left),
    SNUBNOSE_RECOIL_LEFT((byte) 18, FirearmFirstPersonAnimations.snubnose_fire_recoil_left),
    SNUBNOSE_MODEL_AIM_LEFT((byte) 19, FirearmFirstPersonAnimations.snubnose_model_aim_left),
    SNUBNOSE_MODEL_RECOIL_LEFT((byte) 20, FirearmFirstPersonAnimations.snubnose_model_recoil_left),

    TOMMY_AIM((byte) 23, FirearmFirstPersonAnimations.tommy_aim),
    TOMMY_MODEL_AIM((byte) 24, FirearmFirstPersonAnimations.tommy_model_aim),
    TOMMY_AIM_LEFT((byte) 25, FirearmFirstPersonAnimations.tommy_aim_left),
    TOMMY_MODEL_AIM_LEFT((byte) 26, FirearmFirstPersonAnimations.tommy_model_aim_left),
    TOMMY_RECOIL((byte) 27, FirearmFirstPersonAnimations.tommy_recoil),
    TOMMY_MODEL_RECOIL((byte) 28, FirearmFirstPersonAnimations.tommy_model_recoil),
    TOMMY_RECOIL_LEFT((byte) 29, FirearmFirstPersonAnimations.tommy_recoil_left),
    TOMMY_MODEL_RECOIL_LEFT((byte) 30, FirearmFirstPersonAnimations.tommy_model_recoil_left),

    COLT_MODEL_AIM((byte) 31, FirearmFirstPersonAnimations.colt_model_aim),
    COLT_MODEL_RECOIL((byte) 32, FirearmFirstPersonAnimations.colt_model_recoil),
    COLT_MODEL_AIM_LEFT((byte) 33, FirearmFirstPersonAnimations.colt_model_aim_left),
    COLT_MODEL_RECOIL_LEFT((byte) 34, FirearmFirstPersonAnimations.colt_model_recoil_left);


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
        } if (bt== SNUBNOSE_AIM.id) {
            return SNUBNOSE_AIM;
        } if (bt== SNUBNOSE_RECOIL.id) {
            return SNUBNOSE_RECOIL;
        } if (bt== SNUBNOSE_MODEL_AIM.id) {
            return SNUBNOSE_MODEL_AIM;
        } if (bt== SNUBNOSE_MODEL_RECOIL.id) {
            return SNUBNOSE_MODEL_RECOIL;
        } if (bt== SNUBNOSE_AIM_LEFT.id) {
            return SNUBNOSE_AIM_LEFT;
        } if (bt== SNUBNOSE_RECOIL_LEFT.id) {
            return SNUBNOSE_RECOIL_LEFT;
        } if (bt== SNUBNOSE_MODEL_AIM_LEFT.id) {
            return SNUBNOSE_MODEL_AIM_LEFT;
        } if (bt== SNUBNOSE_MODEL_RECOIL_LEFT.id) {
            return SNUBNOSE_MODEL_RECOIL_LEFT;
        } if (bt== TOMMY_AIM.id) {
            return TOMMY_AIM;
        } if (bt== TOMMY_MODEL_AIM.id) {
            return TOMMY_MODEL_AIM;
        } if (bt== TOMMY_AIM_LEFT.id) {
            return TOMMY_AIM_LEFT;
        } if (bt== TOMMY_MODEL_AIM_LEFT.id) {
            return TOMMY_MODEL_AIM_LEFT;
        } if (bt== TOMMY_RECOIL.id) {
            return TOMMY_RECOIL;
        } if (bt== TOMMY_MODEL_RECOIL.id) {
            return TOMMY_MODEL_RECOIL;
        } if (bt== TOMMY_RECOIL_LEFT.id) {
            return TOMMY_RECOIL_LEFT;
        } if (bt== TOMMY_MODEL_RECOIL_LEFT.id) {
            return TOMMY_MODEL_RECOIL_LEFT;
        } if (bt== COLT_MODEL_AIM.id) {
            return COLT_MODEL_AIM;
        } if (bt== COLT_MODEL_RECOIL.id) {
            return COLT_MODEL_RECOIL;
        } if (bt== COLT_MODEL_AIM_LEFT.id) {
            return COLT_MODEL_AIM_LEFT;
        } if (bt== COLT_MODEL_RECOIL_LEFT.id) {
            return COLT_MODEL_RECOIL_LEFT;
        }
        return NONE;
    }

    public static AnimationDefinition getAnimation(Player player) {

        IPlayerEntity ipe = (IPlayerEntity) player;

        AnimationDefinition emote = null;
        if (Poses.WRY.id == ipe.roundabout$GetPoseEmote()) {
            emote = Poses.WRY.ad;
        } else if (Poses.GIORNO.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.GIORNO.ad;
        } else if (Poses.JOSEPH.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.JOSEPH.ad;
        } else if (Poses.KOICHI.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.KOICHI.ad;
        } else if (Poses.OH_NO.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.OH_NO.ad;
        } else if (Poses.TORTURE_DANCE.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.TORTURE_DANCE.ad;
        } else if (Poses.WAMUU.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.WAMUU.ad;
        } else if (Poses.JOTARO.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.JOTARO.ad;
        } else if (Poses.JONATHAN.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.JONATHAN.ad;
        } else if (Poses.WATCH.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.WATCH.ad;
        } else if (Poses.SITTING.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.SITTING.ad;
        } else if (Poses.VAMPIRE_TRANSFORMATION.id == ipe.roundabout$GetPoseEmote() ) {
            emote = Poses.VAMPIRE_TRANSFORMATION.ad;
        }
        return emote;
    }

}
