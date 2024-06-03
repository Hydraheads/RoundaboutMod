package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IParticleAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Particle.class)
public class ZParticle implements IParticleAccess {
    /**Particles Created during a timestop are not frozen*/
    public boolean roundaboutIsTimeStopCreated = false;
    public boolean getRoundaboutIsTimeStopCreated(){
        return roundaboutIsTimeStopCreated;
    }
    public void setRoundaboutIsTimeStopCreated(boolean roundaboutIsTimeStopCreated){
        this.roundaboutIsTimeStopCreated = roundaboutIsTimeStopCreated;
    }


    private float roundaboutPrevTick;

    @Override
    public float getPreTSTick() {
        return this.roundaboutPrevTick;
    }

    @Override
    public void setPreTSTick() {
        Minecraft mc = Minecraft.getInstance();
        roundaboutPrevTick = mc.getFrameTime();
    }
    @Override
    public void resetPreTSTick() {
        roundaboutPrevTick = 0;
    }
}
