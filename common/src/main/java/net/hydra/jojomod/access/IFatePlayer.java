package net.hydra.jojomod.access;

import net.hydra.jojomod.fates.FatePowers;

public interface IFatePlayer {
    void rdbt$startVampireTransformation();
    boolean rdbt$isTransforming();
    FatePowers rdbt$getFatePowers();
}
