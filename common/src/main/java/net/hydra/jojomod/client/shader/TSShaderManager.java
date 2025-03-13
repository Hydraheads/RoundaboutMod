package net.hydra.jojomod.client.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import net.hydra.jojomod.Roundabout;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL32C;

public class TSShaderManager {
    public static int roundabout$program = 0;

    public static int createProgram(TSShader vertex, TSShader fragment) throws Exception {
        int program = GlStateManager.glCreateProgram();

        GlStateManager._glBindAttribLocation(program, 0, "Position");
        GlStateManager._glBindAttribLocation(program, 1, "UV0");

        GlStateManager.glAttachShader(program, vertex.getId());
        GlStateManager.glAttachShader(program, fragment.getId());

        GlStateManager.glLinkProgram(program);

        GL32C.glDetachShader(program, vertex.getId());
        GL32C.glDetachShader(program, fragment.getId());

        if (GlStateManager.glGetProgrami(program, GL20C.GL_LINK_STATUS) != GL20C.GL_TRUE)
        {
            throw new Exception("Failed to create shader program: ");
        }

        Roundabout.LOGGER.info("Created program successfully!");

        return program;
    }
}
