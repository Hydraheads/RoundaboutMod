package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class MagiciansRedEntity extends StandEntity {
    public MagiciansRedEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.base");
        } else if (skinId == BLUE_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.blue");
        } else if (skinId == PURPLE_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.purple");
        } else if (skinId == GREEN_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.green");
        } else if (skinId == DREAD_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.dread");
        } else if (skinId == BLUE_ACE_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.blue_ace");
        } else if (skinId == DREAD_BEAST_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.chagaroth");
        } else if (skinId == MAGMA_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.magma");
        } else if (skinId == LIGHTER_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.lighter");
        } else if (skinId == MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.manga");
        } else if (skinId == OVA_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.ova");
        } else if (skinId == ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.ablaze");
        } else if (skinId == LIGHTER_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.lighter_ablaze");
        } else if (skinId == BLUE_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.blue_ablaze");
        } else if (skinId == PURPLE_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.purple_ablaze");
        } else if (skinId == GREEN_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.green_ablaze");
        } else if (skinId == DREAD_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.dread_ablaze");
        } else if (skinId == SIDEKICK){
            return Component.translatable(  "skins.roundabout.magicians_red.sidekick");
        } else if (skinId == BETA){
            return Component.translatable(  "skins.roundabout.magicians_red.beta");
        }
        return Component.translatable(  "skins.roundabout.magicians_red.base");
    }
    public final AnimationState finalKick = new AnimationState();
    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalKickWindup = new AnimationState();
    public final AnimationState lash1 = new AnimationState();
    public final AnimationState lash2 = new AnimationState();
    public final AnimationState lash3 = new AnimationState();
    public final AnimationState flamethrower_charge = new AnimationState();
    public final AnimationState flamethrower_shoot = new AnimationState();
    public final AnimationState fireball_charge = new AnimationState();
    public final AnimationState fireball_shoot = new AnimationState();
    public final AnimationState red_bind = new AnimationState();
    public final AnimationState fire_crash = new AnimationState();
    public final AnimationState life_detector = new AnimationState();

    public final AnimationState hideLash = new AnimationState();
    public final AnimationState hideFlames = new AnimationState();
    public final AnimationState cycleFlames = new AnimationState();

    public boolean emitsFlameCycle(){
        byte skn = this.getSkin();
        return switch (skn) {
            case ABLAZE, DREAD_ABLAZE, LIGHTER_ABLAZE, BLUE_ABLAZE, PURPLE_ABLAZE, GREEN_ABLAZE -> true;
            default -> false;
        };
    }

    public boolean isPutOutByWater(){
        if (this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            return this.getUser().isUnderWater() || PMR.isInRain();
        }
        return false;
    }
    public boolean emitsLight(){
        if (ConfigManager.getClientConfig().magiciansRedLashesMakeItEmmissive && (this.lash1.isStarted() || this.lash2.isStarted() || lash3.isStarted())){
            return true;
        }
        if (isPutOutByWater()){
                return false;
        }
        byte skn = this.getSkin();
        if (!ConfigManager.getClientConfig().magiciansRedTexturesMakeItEmmissive){
            return false;
        }
        return switch (skn) {
            case ABLAZE, DREAD_ABLAZE, LIGHTER_ABLAZE, BLUE_ABLAZE, PURPLE_ABLAZE, GREEN_ABLAZE, MAGMA_SKIN, MANGA_SKIN -> true;
            default -> false;
        };
    }
    private boolean isInRain() {
        BlockPos $$0 = this.blockPosition();
        return this.level().isRainingAt($$0)
                || this.level().isRainingAt(BlockPos.containing((double)$$0.getX(), this.getBoundingBox().maxY, (double)$$0.getZ()));
    }
    public static final byte
            PART_3_SKIN = 1,
            BLUE_SKIN = 2,
            PURPLE_SKIN = 3,
            GREEN_SKIN = 4,
            DREAD_SKIN = 5,
            BLUE_ACE_SKIN = 6,
            DREAD_BEAST_SKIN = 7,
            MAGMA_SKIN = 8,
            MANGA_SKIN = 9,
            LIGHTER_SKIN = 10,
            OVA_SKIN = 11,
            ABLAZE = 12,
            LIGHTER_ABLAZE = 13,
            BLUE_ABLAZE = 14,
            PURPLE_ABLAZE = 15,
            GREEN_ABLAZE = 16,
            DREAD_ABLAZE = 17,
            SIDEKICK = 18,
            BETA = 19;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {

            if (emitsFlameCycle() && !isPutOutByWater()){
                this.cycleFlames.startIfStopped(this.tickCount);
                this.hideFlames.stop();
            } else {
                this.cycleFlames.stop();
                this.hideFlames.startIfStopped(this.tickCount);
            }

            byte animation = this.getAnimation();
            if (animation > 40 && animation < 44) {
                this.hideLash.stop();
                if (animation == 41) {
                    this.lash1.startIfStopped(this.tickCount);
                } else {
                    this.lash1.stop();
                }
                if (animation == 42) {
                    this.lash2.startIfStopped(this.tickCount);
                } else {
                    this.lash2.stop();
                }
                if (animation == 43) {
                    this.lash3.startIfStopped(this.tickCount);
                } else {
                    this.lash3.stop();
                }
            } else {
                this.hideLash.startIfStopped(this.tickCount);
                this.lash1.stop();
                this.lash2.stop();
                this.lash3.stop();
            }

            if (this.getAnimation() == 45) {
                this.flamethrower_charge.startIfStopped(this.tickCount);
            } else {
                this.flamethrower_charge.stop();
            }

            if (this.getAnimation() == 46) {
                this.flamethrower_shoot.startIfStopped(this.tickCount);
            } else {
                this.flamethrower_shoot.stop();
            }

            if (this.getAnimation() == 47) {
                this.fireball_charge.startIfStopped(this.tickCount);
            } else {
                this.fireball_charge.stop();
            }

            if (this.getAnimation() == 48) {
                this.fireball_shoot.startIfStopped(this.tickCount);
            } else {
                this.fireball_shoot.stop();
            }
            if (this.getAnimation() == 49) {
                this.red_bind.startIfStopped(this.tickCount);
            } else {
                this.red_bind.stop();
            }
            if (this.getAnimation() == 50) {
                this.fire_crash.startIfStopped(this.tickCount);
            } else {
                this.fire_crash.stop();
            }
            if (this.getAnimation() == 51) {
                this.life_detector.startIfStopped(this.tickCount);
            } else {
                this.life_detector.stop();
            }

            if (this.getAnimation() == 85) {
                this.finalKickWindup.startIfStopped(this.tickCount);
            } else {
                this.finalKickWindup.stop();
            }
            if (this.getAnimation() == 86) {
                this.finalKick.startIfStopped(this.tickCount);
            } else {
                this.finalKick.stop();
            }
            if (this.getAnimation() == 87) {
                this.finalPunch.startIfStopped(this.tickCount);
            } else {
                this.finalPunch.stop();
            }
        }
    }
}
