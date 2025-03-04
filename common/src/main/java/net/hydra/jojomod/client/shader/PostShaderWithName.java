package net.hydra.jojomod.client.shader;

import net.minecraft.client.renderer.PostChain;

public class PostShaderWithName {
    private final String name;
    private final PostChain chain;

    public PostShaderWithName(String name, PostChain chain)
    {
        this.name = name;
        this.chain = chain;
    }

    public String getName()
    {
        return this.name;
    }

    public PostChain getChain()
    {
        return this.chain;
    }
}
