package net.hydra.jojomod.access;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IGEProjectile {
    //had to place this in access because minecraft has an aneurysm if it's in mixins
    boolean roundabout$hasGETreeImbue();
    void roundabout$setGETreeImbue(boolean value);
    @Nullable UUID roundabout$getGETreeImbueOwner();
    void roundabout$setGETreeImbueOwner(@Nullable UUID ownerId);
}