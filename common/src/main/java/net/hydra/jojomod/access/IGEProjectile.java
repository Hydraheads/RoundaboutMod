package net.hydra.jojomod.access;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IGEProjectile {
    //had to place this in access because minecraft has an aneurysm if it's in mixins
    boolean sba$hasGETreeImbue();
    void sba$setGETreeImbue(boolean value);
    @Nullable UUID sba$getGETreeImbueOwner();
    void sba$setGETreeImbueOwner(@Nullable UUID ownerId);
}