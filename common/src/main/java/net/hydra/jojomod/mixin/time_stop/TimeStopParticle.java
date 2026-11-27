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
    public boolean sba$IsTimeStopCreated = false;
    @Unique
    private float sba$PrevTick;
    public boolean sba$getSBAIsTimeStopCreated(){
        return sba$IsTimeStopCreated;
    }
    public void sba$setSBAIsTimeStopCreated(boolean sbaIsTimeStopCreated){
        this.sba$IsTimeStopCreated = sbaIsTimeStopCreated;
    }

    @Override
    public float sba$getPreTSTick() {
        return this.sba$PrevTick;
    }

    @Override
    public void sba$setPreTSTick() {
        Minecraft mc = Minecraft.getInstance();
        sba$PrevTick = mc.getFrameTime();
    }
    @Override
    public void sba$resetPreTSTick() {
        sba$PrevTick = 0;
    }


}
