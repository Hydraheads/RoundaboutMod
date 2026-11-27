package net.hydra.jojomod.access;

public interface  IHudAccess {
    /**If you ever want to call the Hud Mixin directly, you can use this interface
     * as an access point.*/

    public void sba$setFlashAlpha(float flashAlpha);

    public void sba$setOtherFlashAlpha(float otherFlashAlpha);
}
