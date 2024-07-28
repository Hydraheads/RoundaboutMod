package net.hydra.jojomod.block;

import net.minecraft.util.StringRepresentable;

public enum GoddessStatuePart implements StringRepresentable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom");

    private final String name;

    private GoddessStatuePart(String $$0) {
        this.name = $$0;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
