package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.AccessSwellGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SwellGoal.class)
public abstract class ZSwellGoal implements AccessSwellGoal {
    @Shadow
    private @Nullable LivingEntity target;

    @Shadow
    public abstract void stop();

    @Override
    public void rdbt$cancelTarget() {
        stop();
    }
}
