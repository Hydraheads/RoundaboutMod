package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.access.NoHitboxRendering;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class SilverChariotAfterimageEntity extends StandEntity implements NoHitboxRendering {

    public SilverChariotAfterimageEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.discard();
        return super.hurt(source, amount);
    }
}
