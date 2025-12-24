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
    public Component description2 = null;
    public Component description3 = null;

    public int context;
    public int currentLevel = 0;
    public int maxLevel = 0;

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

    public AbilityIconInstance(int size, int startingLeft, int startingTop, int currentLevel, int maxLevel,
                               Component name, Component instruction, Component description, int context,
                               Component description2,
                               Component description3){
        this.size = size;
        this.startingLeft = startingLeft;
        this.startingTop = startingTop;
        this.levelToUnlock = 0;
        this.currentLevel = currentLevel;
        this.maxLevel = maxLevel;
        this.name = name;
        this.instruction = instruction;
        this.description = description;
        this.context = context;
        this.description2 = description2;
        this.description3 = description3;
    }
}
