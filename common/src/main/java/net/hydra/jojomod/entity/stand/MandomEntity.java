package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

public class MandomEntity extends FollowingStandEntity {

    /**BLACK SABBATH EXCLUSIVE!!!*/

    public MandomEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            MANGA = 1,
            PURPLE = 2,
            SKY = 3,
            SQUID = 4,
            GLOW_SQUID = 5,
            ROSE = 6,
            NAUTILUS = 7,
            ALIEN = 8,
            DARK = 9,
            JELLYFISH = 10,
            HAPPY = 11,
            EYE = 12,
            MELON = 13,
            ESIDISI = 14,
            COMMAND = 15,
            SCULK = 16,
            TV = 17,
            CAPS = 18;

    @Override
    public void setPose(Pose $$0) {
        super.setPose($$0);
    }

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if(this.getUser() == null && this.getBsGrabbed() != 0){
            this.grabbedStandByBlackSabbath.startIfStopped(this.tickCount);
            return;
        }
    }

    @Override
    public float blackSabbathGrabOffset(){
        return 0.98f;
    }
}
