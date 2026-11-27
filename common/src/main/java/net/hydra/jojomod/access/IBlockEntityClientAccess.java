package net.hydra.jojomod.access;

/**This code lets me access the ZBlockEntityClient Mixin externally, so I can call BlockEntity functions*/
public interface IBlockEntityClientAccess {

    float sba$getPreTSTick();

    void sba$setPreTSTick();

    void sba$resetPreTSTick();

}
