package net.hydra.jojomod.event.index;

import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AnubisMemory {


    public static final byte INPUTS = (byte) 0;
    public static final byte MOUSE = (byte)1;
    public static final byte MOUSE_INVENTORY = (byte)2;


    public Item item;
    public List<AnubisMoment> moments;
    public List<Vec3> rots;
    public byte memory_type = 0;
    public AnubisMemory(Item item, List<AnubisMoment> moments) {
        this.item = item;
        this.moments = moments;
        this.rots = new ArrayList<>();
    }

    public boolean canPlayback() {
        if (this.memory_type != AnubisMemory.INPUTS) {
            return !this.rots.isEmpty();
        }
        return !this.moments.isEmpty();
    }
    public boolean canMouse() {
        if (this.memory_type != AnubisMemory.INPUTS) {
            return !this.rots.isEmpty();
        }
        return false;
    }
    public int getFirstTime() {
        int ret = -1;
        if (!moments.isEmpty()) {
            ret = moments.get(0).time;
        }
        if (this.memory_type != INPUTS) {
            if (rots.size() > 1) {
                int rRet =(int) rots.get(1).x;
                if (ret == -1 || rRet < ret) {
                    ret = rRet;
                }
            }
        }
        return ret;
    }
    public int getLastTime() {
        int ret = -1;
        if (!moments.isEmpty()) {
            ret = moments.get(moments.size()-1).time;
        }
        if (this.memory_type != INPUTS) {
            if (rots.size() > 1) {
                ret = Math.max(ret,(int)rots.get(rots.size()-1).x);
            }
        }
        return ret;
    }
}
