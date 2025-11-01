package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.IGravityEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TheGratefulDeadEntity extends FollowingStandEntity{
    public TheGratefulDeadEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
        ANIME = 0,
        MANGA = 1;

    public final AnimationState idleAnimationState2 = new AnimationState();
    public final AnimationState hideFists = new AnimationState();
    public final AnimationState customBlock = new AnimationState(); //10
    public final AnimationState customBarrageCharge = new AnimationState(); //11
    public final AnimationState customBarrage = new AnimationState(); //12
    public final AnimationState customBarrageEnd = new AnimationState(); //13
    public final AnimationState customBrokenGuard = new AnimationState(); //15
    public final AnimationState customMiningBarrage = new AnimationState(); //16

    @Override
    public void setupAnimationStates() {
        if (this.getUser() != null) {
            byte idle = getIdleAnimation();
            byte animation = getAnimation();
            if(animation == 0 && this.getGrounded()){
                this.idleAnimationState.startIfStopped(this.tickCount);
            }else{
                this.idleAnimationState.stop();
            }
            if(animation == 0 && !this.getGrounded()){
                this.idleAnimationState2.startIfStopped(this.tickCount);
            }else{
                this.idleAnimationState2.stop();
            }
            if(animation == 12 && this.getGrounded()){
                this.customBarrage.startIfStopped(this.tickCount);
            }else{
                this.customBarrage.stop();
            }
            if(animation == 12 && !this.getGrounded()){
                this.barrageAnimationState.startIfStopped(this.tickCount);
            }else{
                this.barrageAnimationState.stop();
            }
            if(animation == 11 && this.getGrounded()){
                this.customBarrageCharge.startIfStopped(this.tickCount);
            }else{
                this.customBarrageCharge.stop();
            }
            if(animation == 11 && !this.getGrounded()){
                this.barrageChargeAnimationState.startIfStopped(this.tickCount);
            }else{
                this.barrageChargeAnimationState.stop();
            }
            if(animation == 10 && this.getGrounded()){
                this.customBlock.startIfStopped(this.tickCount);
            }else{
                this.customBlock.stop();
            }
            if(animation == 10 && !this.getGrounded()){
                this.blockAnimationState.startIfStopped(this.tickCount);
            }else{
                this.blockAnimationState.stop();
            }
            if(animation == 13 && this.getGrounded()){
                this.customBarrageEnd.startIfStopped(this.tickCount);
            }else{
                this.customBarrageEnd.stop();
            }
            if(animation == 13 && !this.getGrounded()){
                this.barrageEndAnimationState.startIfStopped(this.tickCount);
            }else{
                this.barrageEndAnimationState.stop();
            }
            if(animation == 15 && this.getGrounded()){
                this.customBrokenGuard.startIfStopped(this.tickCount);
            }else{
                this.customBrokenGuard.stop();
            }
            if(animation == 15 && !this.getGrounded()){
                this.brokenBlockAnimationState.startIfStopped(this.tickCount);
            }else{
                this.brokenBlockAnimationState.stop();
            }
            if(animation == 16 && this.getGrounded()){
                this.customMiningBarrage.startIfStopped(this.tickCount);
            }else{
                this.customMiningBarrage.stop();
            }
            if(animation == 16 && !this.getGrounded()){
                this.miningBarrageAnimationState.startIfStopped(this.tickCount);
            }else{
                this.miningBarrageAnimationState.stop();
            }
            if(animation != 12){
                this.hideFists.startIfStopped(this.tickCount);
            }else{
                this.hideFists.stop();
            }
            if(animation == 1){
                this.punchState1.startIfStopped(this.tickCount);
            }else{
                this.punchState1.stop();
            }
            if(animation == 2){
                this.punchState2.startIfStopped(this.tickCount);
            }else{
                this.punchState2.stop();
            }
            if(animation == 3){
                this.punchState3.startIfStopped(this.tickCount);
            }else{
                this.punchState3.stop();
            }
        }
    }

    @Override
    public boolean lockPos(){
        return !(this.getGrounded());
    }
    @Override
    public boolean hasNoPhysics(){
        return !(this.getGrounded());
    }
    @Override
    public boolean standHasGravity() {
        return this.getGrounded();
    }

    public boolean getGrounded(){
        if (this.getUser() == null){
            return false;
        }else{
            BlockPos blockBelowPosStand = this.blockPosition().below();
            Block blockBelowStand = this.level().getBlockState(blockBelowPosStand).getBlock();
            BlockPos blockBelowPos = this.getUser().blockPosition().below();
            Block blockBelow = this.getUser().level().getBlockState(blockBelowPos).getBlock();
            return (getIdleAnimation() == 0)&&!(blockBelowStand==Blocks.AIR || blockBelowStand==Blocks.WATER || blockBelowStand==Blocks.LAVA)&&!(blockBelow==Blocks.AIR || blockBelow==Blocks.WATER || blockBelow==Blocks.LAVA)&&!(this.getUser().isSwimming());
        }
    }
    @Override
    public boolean isVisuallyCrawling(){
        return false;
    }

    @Override
    public Vec3 getIdleOffset(LivingEntity standUser){
        int vis = this.getFadeOut();
        double r = (((double) vis / MaxFade) * ((standUser.getBbWidth()/2)+this.getDistanceOut()));
        if (r < 0.5) {
            r = 0.5;
        }
        double yawfix = standUser.getYRot();
        yawfix += this.getAnchorPlace();
        if (yawfix > 360) {
            yawfix -= 360;
        } else if (yawfix < 0) {
            yawfix += 360;
        }
        double ang = (yawfix - 180) * Math.PI;

        double mcap = 0.3;
        Vec3 xyz = standUser.getDeltaMovement();
        double yy = xyz.y() * 0.3;
        if (yy > mcap) {
            yy = mcap;
        } else if (yy < -mcap) {
            yy = -mcap;
        }
        if (isSwimming() || isVisuallyCrawling() || isFallFlying()) {
            yy += 1;
        }


        Direction dir = ((IGravityEntity)standUser).roundabout$getGravityDirection();
        Vec3 offset = new Vec3(
                (- (-1 * (r * (Math.sin(ang / 180))))),
                (getIdleYOffset() - yy),
                (-(r * (Math.cos(ang / 180))))
        );
        if (dir != Direction.DOWN){
            offset = RotationUtil.vecPlayerToWorld(offset,dir);
        }

        double x1 = standUser.getX() +offset.x;
        double y1 = standUser.getY() +offset.y;
        double z1 = standUser.getZ() +offset.z;

        return new Vec3(x1, standUser.getY(), z1);

    }
}
