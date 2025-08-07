package net.hydra.jojomod.access;

import net.hydra.jojomod.util.RotationAnimation;
import org.spongepowered.asm.mixin.Unique;

public interface IClientEntity {
    public void roundabout$setGravityAnimation(RotationAnimation ra);
    RotationAnimation roundabout$getGravityAnimation();
}
