package net.hydra.jojomod.mixin;

import com.google.common.collect.Maps;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.stand.TheWorldRenderer;
import net.minecraft.client.renderer.entity.AllayRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityRenderers.class)
public class RegisterEntityRenderer {
    @Shadow
    private static final Map<EntityType<?>, EntityRendererProvider<?>> PROVIDERS = Maps.newHashMap();
    @Shadow
    private static <T extends Entity> void register(EntityType<? extends T> $$0, EntityRendererProvider<T> $$1) {
        PROVIDERS.put($$0, $$1);
    }

        static {
            register(ModEntities.TERRIER_DOG, TerrierEntityRenderer::new);
            register(ModEntities.THE_WORLD, TheWorldRenderer::new);
        }
}
