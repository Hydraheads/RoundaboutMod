package net.hydra.jojomod.access;

import net.minecraft.client.KeyMapping;

public interface IInputEvents {
    boolean roundabout$sameKeyOne(KeyMapping key1);
    boolean roundabout$sameKeyTwo(KeyMapping key1);

    boolean roundabout$sameKeyUseOverride(KeyMapping key1);
}
