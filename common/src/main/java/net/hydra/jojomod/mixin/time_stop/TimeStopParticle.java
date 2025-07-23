package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.access.IParticleAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Particle.class)
public class TimeStopParticle implements IParticleAccess {

    /**Particles Created during a timestop are not frozen, but the others are*/

    @Unique
    public boolean roundabout$IsTimeStopCreated = false;
    @Unique
    private float roundabout$PrevTick;
    public boolean roundabout$getRoundaboutIsTimeStopCreated(){
        return roundabout$IsTimeStopCreated;
    }
    public void roundabout$setRoundaboutIsTimeStopCreated(boolean roundaboutIsTimeStopCreated){
        this.roundabout$IsTimeStopCreated = roundaboutIsTimeStopCreated;
    }

    @Override
    public float roundabout$getPreTSTick() {
        return this.roundabout$PrevTick;
    }

    @Override
    public void roundabout$setPreTSTick() {
        Minecraft mc = Minecraft.getInstance();
        roundabout$PrevTick = mc.getFrameTime();
    }
    @Override
    public void roundabout$resetPreTSTick() {
        roundabout$PrevTick = 0;
    }


}
