package net.hydra.jojomod.access;

public interface ServerPlayerAccess {
    /**In theory, this file would be used to do things after you switch
     * dimensions, like keep your stand summoned. But it is currently
     * not called. */
    public void compatSync();
}
