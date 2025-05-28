package net.hydra.jojomod.client.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class CustomItemRendererRegistry {
    private static final HashMap<Item, CustomItemRenderer> registry = new HashMap<>();

    public static <T extends CustomItemRenderer> void register(Item i, T renderer)
    {
        registry.put(i, renderer);
    }

    public static @Nullable CustomItemRenderer getItemRenderer(Item i)
    {
        return registry.get(i);
    }

    static {
        //register(ModItems.FLAG, new FlagItemRenderer());
    }
}