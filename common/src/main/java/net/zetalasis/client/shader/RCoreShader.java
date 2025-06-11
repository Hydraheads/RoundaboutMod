package net.zetalasis.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class RCoreShader {
    @Nullable public static ShaderInstance roundabout$meltDodgeProgram;
    @Nullable public static ShaderInstance roundabout$loveTrainProgram;
    private final ShaderInstance program;

    public RCoreShader(ResourceProvider provider, String name) throws IOException {
        this.program = new ShaderInstance(provider, name, DefaultVertexFormat.POSITION_COLOR_TEX);
    }

    public ShaderInstance getProgram()
    {
        return this.program;
    }
}
