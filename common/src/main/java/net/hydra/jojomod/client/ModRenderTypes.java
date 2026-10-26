// code taken and inspired from: https://github.com/Violunae/arrowtrails
package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ModRenderTypes extends RenderType {
    // Private constructor required by RenderType
    private ModRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static final RenderType TRAIL_RENDER_TYPE = create(
            "roundabout_projectile_trail",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            CompositeState.builder()
                    .setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(
                            new ResourceLocation("minecraft", "textures/misc/white.png"), false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setLightmapState(LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false)
    );
}