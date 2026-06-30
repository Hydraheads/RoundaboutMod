package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class CaliforniaKingBedEntity extends FollowingStandEntity {
    public CaliforniaKingBedEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            PART_8_SKIN = 1;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
    }
    @Override
    public float getDistanceOutModified() {return getDistanceOut()*1.15F;}
    @Override
    public float getAnchorPlaceModified() {return getAnchorPlace()+10;}
}
