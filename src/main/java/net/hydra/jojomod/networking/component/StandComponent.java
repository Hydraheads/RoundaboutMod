package net.hydra.jojomod.networking.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

/** @see StandData
 * the code needs an interface for other classes to call on.*/
public interface StandComponent extends Component, AutoSyncedComponent {
    @Nullable
    LivingEntity getUser();
    @Nullable
    LivingEntity getFollowing();

    void sync();

    void setUser(LivingEntity StandSet);
    void setFollowing(LivingEntity StandSet);
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player);
}
