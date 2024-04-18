package net.hydra.jojomod.networking.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.sync.ComponentPacketWriter;
import dev.onyxstudios.cca.api.v3.component.sync.PlayerSyncPredicate;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/** @see StandUserData
 * the code needs an interface for other classes to call on.*/
public interface StandUserComponent extends Component, AutoSyncedComponent {

    boolean hasStandOut();
    void onStandOutLookAround(StandEntity passenger);

    void updateStandOutPosition(StandEntity passenger);

    void updateStandOutPosition(StandEntity passenger, Entity.PositionUpdater positionUpdater);
    void removeStandOut();
    void setDI(int forward, int strafe);
    void standMount(StandEntity StandSet);
    void setStand(StandEntity StandSet);

    int getAttackTimeMax();
    int getAttackTime();
    int getActivePowerPhase();
    int getActivePowerPhaseMax();
    int getActivePower();

    void setActive(boolean active);

    void summonStand(World theWorld, boolean forced, boolean sound);
    void sync();

    boolean getActive();

   StandPowers getStandPowers();

   void setStandPowers(StandPowers standPowers);
    void setPowerAttack();

   void tryPower(int move, boolean forced);

   LivingEntity getPowerUser();

    @Nullable
    StandEntity getStand();
}
