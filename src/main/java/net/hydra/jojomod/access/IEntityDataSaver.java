package net.hydra.jojomod.access;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();
}
