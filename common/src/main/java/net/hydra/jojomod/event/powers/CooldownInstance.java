package net.hydra.jojomod.event.powers;

public class CooldownInstance {
    public int time;
    public int maxTime;
    public boolean dirty;
    public CooldownInstance(){
        this.time = -1;
        this.maxTime = -1;
        this.dirty = false;
    }

    public CooldownInstance(int time, int maxTime){
        this.time = time;
        this.maxTime = maxTime;
        this.dirty = false;
    }
}
