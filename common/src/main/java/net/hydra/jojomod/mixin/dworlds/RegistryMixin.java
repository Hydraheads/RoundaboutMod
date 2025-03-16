package net.hydra.jojomod.mixin.dworlds;

import net.hydra.jojomod.world.DynamicWorldAccessor;
import net.minecraft.core.MappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MappedRegistry.class)
public abstract class RegistryMixin implements DynamicWorldAccessor {
    @Shadow private boolean frozen;

    @Override
    public void roundabout$setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
