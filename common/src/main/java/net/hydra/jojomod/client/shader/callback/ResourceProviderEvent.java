package net.hydra.jojomod.client.shader.callback;

import net.minecraft.server.packs.resources.ResourceProvider;

import java.util.ArrayList;
import java.util.List;

public class ResourceProviderEvent {
    private static final List<ResourceProviderCallback> registered = new ArrayList<>();

    public static void register(ResourceProviderCallback listener)
    {
        registered.add(listener);
    }

    public static void invoke(ResourceProvider provider)
    {
        for (ResourceProviderCallback c : registered)
        {
            c.EXECUTE(provider);
        }
    }
}
