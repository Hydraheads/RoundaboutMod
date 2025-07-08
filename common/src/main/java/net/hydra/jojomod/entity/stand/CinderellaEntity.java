package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class CinderellaEntity extends FollowingStandEntity {
    public CinderellaEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            PART_4_SKIN = 1,
            MANGA_SKIN = 2,
            ZOMBIE_SKIN = 3;

    public final AnimationState deface = new AnimationState();
    public final AnimationState visages = new AnimationState();

    public static final byte
            DEFACE = 81,
            VISAGES = 82;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == DEFACE) {
                this.deface.startIfStopped(this.tickCount);
            } else {
                this.deface.stop();
            }
            if (this.getAnimation() == VISAGES) {
                this.visages.startIfStopped(this.tickCount);
            } else {
                this.visages.stop();
            }
        }
    }
}
