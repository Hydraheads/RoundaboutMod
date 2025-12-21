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

    @Override
    public void setupAnimationStates() {
        if (this.getUser() != null) {
            byte idle = getIdleAnimation();
            byte animation = getAnimation();
            if(animation != BARRAGE){
                this.hideFists.startIfStopped(this.tickCount);
            }else{
                this.hideFists.stop();
            }
            super.setupAnimationStates();
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
        double z1 = standUser.getZ() +offset.z;

        return new Vec3(x1, standUser.getY(), z1);

    }
}
