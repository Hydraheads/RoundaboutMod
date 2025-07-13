package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrimedTnt.class)
public abstract class ZPrimedTnt extends Entity {
    public ZPrimedTnt(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    /**Timestop mob throw works for tnt with this*/

    @Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$tick(CallbackInfo ci) {
        ((IEntityAndData)this).roundabout$universalTick();
        ((IEntityAndData)this).roundabout$tickQVec();

    }
}
