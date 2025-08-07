package net.hydra.jojomod.mixin.gravity.client;

import net.hydra.jojomod.access.IClientEntity;
import net.hydra.jojomod.util.RotationAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.swing.text.html.parser.Entity;

@Mixin(value= Entity.class)
public class GravityClientEntityMixin implements IClientEntity {
    @Unique
    public RotationAnimation roundabout$gravityRotationAnim =new RotationAnimation();
    @Unique
    @Override
    public void roundabout$setGravityAnimation(RotationAnimation ra){
        roundabout$gravityRotationAnim = ra;
    }
    @Unique
    @Override
    public RotationAnimation roundabout$getGravityAnimation(){
        return roundabout$gravityRotationAnim;
    }
}
