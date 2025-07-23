package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.IWalkAnimationState;
import net.minecraft.world.entity.WalkAnimationState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WalkAnimationState.class)
public class AccessWalkAnimationState implements IWalkAnimationState {

    /**There is no reason for these to be private or protected, we should be able to tap into them.*/
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


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow
    private float speedOld;
    @Shadow
    private float speed;
    @Shadow
    private float position;

}
