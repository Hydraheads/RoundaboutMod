package net.hydra.jojomod.event.powers;

public class CooldownInstance {
    public int time;
    public int maxTime;
    private boolean frozen;

    public CooldownInstance(){
        this.time = -1;
        this.maxTime = -1;
        this.frozen = false;
    }

    public CooldownInstance(int time, int maxTime){
        this.time = time;
        this.maxTime = maxTime;
    }

    public void setFrozen(boolean value)
    {
        this.frozen = value;
    }

    public boolean isFrozen()
    {
        return this.frozen;
    }
}
