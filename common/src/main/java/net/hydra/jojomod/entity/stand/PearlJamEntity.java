package net.hydra.jojomod.entity.stand;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PearlJamEntity extends FollowingStandEntity{

    public PearlJamEntity(EntityType<? extends Mob> entityType, Level world) {super(entityType, world);}

    public static final byte
            ANIME = 0,
            MANGA = 1;

    @Override
    public float getIdleYOffsetModified() {
        if (!this.getDisplay()){
            return super.getIdleYOffsetModified() + 0.4f;
        }
        return super.getIdleYOffsetModified();
    }

    @Override
    public float getDistanceOutModified() {
        if (!this.getDisplay()){
            return super.getDistanceOutModified() - 0.75f;
        }
        return super.getDistanceOutModified();
    }
}
