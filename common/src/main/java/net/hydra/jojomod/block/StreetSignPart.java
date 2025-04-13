package net.hydra.jojomod.block;

import net.minecraft.util.StringRepresentable;

public enum StreetSignPart implements StringRepresentable {
    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    private StreetSignPart(String $$0) {
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