package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class D4CEntity extends FollowingStandEntity {
    public D4CEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            BASE = 1;
    public final AnimationState hideFists = new AnimationState();


    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getAnimation() != BARRAGE) {
            this.hideFists.startIfStopped(this.tickCount);
        } else {
            this.hideFists.stop();
        }
    }

    @Override
    public void tick(){
        super.tick();
    }

}
