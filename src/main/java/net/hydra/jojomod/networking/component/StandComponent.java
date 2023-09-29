package net.hydra.jojomod.networking.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

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