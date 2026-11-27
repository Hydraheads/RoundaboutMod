package net.hydra.jojomod.access;

/**This code lets me access the ZBlockEntity Mixin externally, so I can call clientside only functions for BlockEntity*/
public interface IBlockEntityAccess {
    void sba$setSBATimeInteracted(boolean sbaTimeInteracted);

    boolean sba$getSBATimeInteracted();
}
