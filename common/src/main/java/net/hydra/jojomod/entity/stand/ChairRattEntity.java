package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class ChairRattEntity extends RattEntity{
    public ChairRattEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
}
