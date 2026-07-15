package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class KingCrimsonEntity extends FollowingStandEntity {
    public KingCrimsonEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            PART_5_SKIN = 1,
            MANGA_SKIN = 2,
            STARLESS = 3,
            END = 4,
            END_2 = 5,
            HEAVEN = 6,
            AGOGO = 7,
            SPINE_ART = 8,
            GREEN = 9,
            YELLOW = 10,
            AQUA = 11,
            DARK = 12,
            BLACK = 13,
            BETA = 14,
            CONCEPT = 15,
            RED = 16;


    public final AnimationState hideFists = new AnimationState();
    public final AnimationState impale = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            byte animation = getAnimation();
            if (animation != BARRAGE) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }
            if (animation == IMPALE) {
                this.impale.startIfStopped(this.tickCount);
            } else {
                this.impale.stop();
            }
        }
    }

    public int tsReleaseTime = 0;
    @Override
    public void tick(){
        if (!this.level().isClientSide){
            if (this.getAnimation() == 31) {
                tsReleaseTime++;
                if (tsReleaseTime > 24){
                    this.setAnimation((byte) 0);
                    tsReleaseTime = 0;
                }
            }
        }
        super.tick();
    }
}
