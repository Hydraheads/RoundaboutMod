package net.hydra.jojomod.block;

import net.minecraft.util.StringRepresentable;

public enum GamblingTablePart implements StringRepresentable {
    FRONT_LEFT("front_left"),
    FRONT_RIGHT("front_right"),
    BACK_LEFT("back_left"),
    BACK_RIGHT("back_right");

    private final String name;

    GamblingTablePart(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}