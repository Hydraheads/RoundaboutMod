package net.hydra.jojomod.mixin.gravity.client;

import net.hydra.jojomod.access.IClientEntity;
import net.hydra.jojomod.util.RotationAnimation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value= Entity.class)
public class GravityClientEntityMixin implements IClientEntity {
    @Unique
    public RotationAnimation sba$gravityRotationAnim =new RotationAnimation();
    @Unique
    @Override
    public void sba$setGravityAnimation(RotationAnimation ra){
        sba$gravityRotationAnim = ra;
    }
    @Unique
    @Override
    public RotationAnimation sba$getGravityAnimation(){
        return sba$gravityRotationAnim;
    }
}
