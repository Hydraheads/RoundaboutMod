package net.hydra.jojomod.access;

import net.minecraft.client.KeyMapping;

public interface IInputEvents {
    boolean sba$sameKeyOne(KeyMapping key1);
    boolean sba$sameKeyTwo(KeyMapping key1);

    boolean sba$sameKeyUseOverride(KeyMapping key1);

    int getSwitchTick();
}
