package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

public class HeyYaEntity extends FollowingStandEntity {

    /**BLACK SABBATH EXCLUSIVE!!!*/

    public HeyYaEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            MANGA = 1,
            GOTHIC = 2,
            VOLUME_2 = 3,
            CHAPTER_24 = 4,
            GREENER = 5,
            WORLD = 6,
            FIRE_AND_ICE = 7,
            WARDEN = 8,
            ICE_COLD = 9,
            VILLAGER = 10,
            GEEZER = 11,
            SKELETON = 12,
            WITHER = 13,
            TUSK = 14,
            DEVIL = 15,
            HELL_NAH = 16,
            ALIEN = 17,
            AMERICA = 18,
            ZOMBIE = 19,
            ANTI = 20,
            GREY_YA = 21,
            ANGEL = 22,
            KNIGHT = 23,
            SAINT = 24,
            VAMPIRE = 25;

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
        return 0.95f;
    }
}
