package net.hydra.jojomod.entity.gold_experience;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GEBatEntity extends Bat implements GELifeform {
    private boolean isReflectingDamage = false;
    private LivingEntity creator;

    public GEBatEntity(EntityType<? extends Bat> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isReflectingDamage() { return isReflectingDamage; }
    @Override
    public void setReflectingDamage(boolean reflecting) { this.isReflectingDamage = reflecting; }
    @Override
    @Nullable
    public LivingEntity getCreator() { return creator; }
    @Override
    public void setCreator(@Nullable LivingEntity creator) { this.creator = creator; }
}