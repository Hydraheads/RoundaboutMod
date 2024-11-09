package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderer.class)
public class ZEntityRenderer implements IEntityRenderer {
    @Shadow
    protected float shadowRadius;
    @Shadow
    protected float shadowStrength = 1.0F;
    @Unique
    protected float roundabout$shadowRadius = 0;
    @Unique
    protected float roundabout$shadowStrength = 1.0F;
    @Unique
    protected boolean roundabout$disabledShadow = false;
    @Unique
    @Override
    public void roundabout$disableShadows(){
        roundabout$disabledShadow = true;
    }
    @Unique
    @Override
    public void roundabout$reloadShadows(){
        roundabout$disabledShadow = false;
    }
    @Unique
    @Override
    public boolean roundabout$getShadowDisabled(){
        return roundabout$disabledShadow;
    }
}
