package net.hydra.jojomod.client.shader.callback;

import net.minecraft.server.packs.resources.ResourceProvider;

public interface OnShaderLoadCallback {
    void onResourceProvider(ResourceProvider provider);
}