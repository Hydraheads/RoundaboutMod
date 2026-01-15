package net.hydra.jojomod.mixin.gravity;

import net.minecraft.world.entity.AreaEffectCloud;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;


@Mixin(AreaEffectCloud.class)
public abstract class GravityAreaEffectCloudMixin extends Entity {
    public GravityAreaEffectCloudMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}
