package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;

public class ProtectionEntry {
    public final BlockPos pos;

    public ProtectionEntry(BlockPos pos) {
        this.pos = pos;
    }

    public boolean isInside(BlockPos other) {
        return pos.closerThan(other, 200);
    }
}