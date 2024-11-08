package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IWalkAnimationState;
import net.minecraft.world.entity.WalkAnimationState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WalkAnimationState.class)
public class ZWalkAnimationState implements IWalkAnimationState {
    @Shadow
    private float speedOld;
    @Shadow
    private float speed;
    @Shadow
    private float position;

    @Unique
    @Override
    public float roundabout$getPosition(){
        return position;
    }
    @Unique
    @Override
    public float roundabout$getSpeedOld(){
        return speedOld;
    }
    @Unique
    @Override
    public void roundabout$setPosition(float position){
        this.position = position;
    }
    @Unique
    @Override
    public void roundabout$setSpeedOld(float speedOld){
        this.speedOld = speedOld;
    }
}
