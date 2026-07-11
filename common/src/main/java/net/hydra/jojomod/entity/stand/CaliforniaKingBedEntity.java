package net.hydra.jojomod.entity.stand;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class CaliforniaKingBedEntity extends FollowingStandEntity {
    public CaliforniaKingBedEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            PART_8_SKIN = 1,
            SUNSHINE = 2;

    public final AnimationState fall_brace = new AnimationState();
    public final AnimationState sleep = new AnimationState();
    public static final byte
            FALL_BRACE = 82,
            SLEEP = 83;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == FALL_BRACE) {
                this.fall_brace.startIfStopped(this.tickCount);
            } else {
                this.fall_brace.stop();
            }
            if (this.getAnimation() == SLEEP) {
                this.sleep.startIfStopped(this.tickCount);
            } else {
                this.sleep.stop();
            }
        }
    }
    @Override
    public float getDistanceOutModified() {return getDistanceOut()*1.15F;}
    @Override
    public float getAnchorPlaceModified() {return getAnchorPlace()+10;}

    public BlockPos bedBlockBind = null;
    public UUID bedUUID = null;


    public void tick(){
        super.tick();
        if (!this.level().isClientSide()){

            if (this.getAnimation() == SLEEP) {
                assertYaw();
            }
        }
    }

    public float yaw = 0;
    public void assertYaw(){
        float rand = (float) (Math.random()*0.0001F);
        float yaw2= yaw+rand;
        setYRot(yaw2);
        setYHeadRot(yaw2);
        setYBodyRot(yaw2);
        yRotO = yaw2;
        yHeadRotO = yaw2;
        yBodyRotO = yaw2;
        setXRot(0.01F+rand); // if desired
    }
}
