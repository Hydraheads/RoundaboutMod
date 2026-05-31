package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class PlanetWavesEntity extends FollowingStandEntity {
    public PlanetWavesEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            PART_6_SKIN = 1,
            MANGA_SKIN = 2,
            BLUE_SKIN = 3,
            PURPLE_SKIN = 4,
            GREEN_SKIN = 5;




    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
    }

}

