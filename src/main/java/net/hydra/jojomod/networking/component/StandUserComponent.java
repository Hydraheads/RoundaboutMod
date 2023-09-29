package net.hydra.jojomod.networking.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.sync.ComponentPacketWriter;
import dev.onyxstudios.cca.api.v3.component.sync.PlayerSyncPredicate;
import net.hydra.jojomod.entity.StandEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public interface StandUserComponent extends Component, AutoSyncedComponent {

    boolean hasStandOut();
    void onStandOutLookAround(StandEntity passenger);

    void updateStandOutPosition(StandEntity passenger);

    void updateStandOutPosition(StandEntity passenger, Entity.PositionUpdater positionUpdater);
    void removeStandOut();
    void setDI(int forward, int strafe);
    void standMount(StandEntity StandSet);
    void setStand(StandEntity StandSet);

    void setActive(boolean active);

    void sync();

    boolean getActive();

    @Nullable
    StandEntity getStand();
    @Override
    boolean shouldSyncWith(ServerPlayerEntity player);
}
