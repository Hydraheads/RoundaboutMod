package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.access.IRenderSystem;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RenderSystem.class)
public class ZRenderSystem implements IRenderSystem {
    @Shadow @Final private static Vector3f[] shaderLightDirections;

    @Unique
    @Override
    public Vector3f[] roundabout$getShaderLightDirections(){
        return shaderLightDirections;
    }

}
