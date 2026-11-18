package net.hydra.jojomod.access;

public interface IGEProjectile {
    //had to place this in access because minecraft has an aneurysm if it's in mixins
    boolean roundabout$hasGETreeImbue();
    void roundabout$setGETreeImbue(boolean value);
}