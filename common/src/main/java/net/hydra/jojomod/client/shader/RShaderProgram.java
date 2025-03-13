package net.hydra.jojomod.client.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import net.hydra.jojomod.Roundabout;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL32C;

public class RShaderProgram {
    private int program = 0;

    public RShaderProgram(RShader vertex, RShader fragment) throws Exception {
        this.program = GlStateManager.glCreateProgram();

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
    }

    public int getProgram() {return this.program;}
}
