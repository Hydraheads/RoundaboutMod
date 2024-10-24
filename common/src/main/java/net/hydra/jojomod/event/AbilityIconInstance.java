package net.hydra.jojomod.event;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AbilityIconInstance {
    public int size;
    public int startingLeft;
    public int startingTop;
    public int levelToUnlock;

    public Component name;
    public Component instruction;
    public Component description;

    public int context;

    public AbilityIconInstance(int size, int startingLeft, int startingTop, int levelToUnlock,
                               Component name, Component instruction, Component description, int context){
        this.size = size;
        this.startingLeft = startingLeft;
        this.startingTop = startingTop;
        this.levelToUnlock = levelToUnlock;
        this.name = name;
        this.instruction = instruction;
        this.description = description;
        this.context = context;
    }
}
