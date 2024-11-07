package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.joml.Matrix4f;

import java.io.IOException;

public class TSShader extends ShaderInstance {
    private static final Matrix4f identity;
    private static TSShader lastApplied;

    static {
        identity = new Matrix4f();
        identity.identity();
    }

    private int textureToUnswizzle;

    public TSShader(ResourceProvider $$0, String $$1, VertexFormat $$2) throws IOException {
        super($$0, $$1, $$2);
    }

}
