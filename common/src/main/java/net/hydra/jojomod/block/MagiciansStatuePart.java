package net.hydra.jojomod.block;

import net.minecraft.util.StringRepresentable;

public enum MagiciansStatuePart implements StringRepresentable{
    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    private MagiciansStatuePart(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
    @Override
    public String getSerializedName(){
        return this.name;
    }
}
