package net.hydra.jojomod.client.shader;

public abstract class GLResource {
    public static final int GL_RESOURCE_INVALID = -1;

    private int glId = -1;

    protected GLResource(int glId) {
        this.glId = glId;
    }

    public int getId()
    {
        return this.glId;
    }
}
