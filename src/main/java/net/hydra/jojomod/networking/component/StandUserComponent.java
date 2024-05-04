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
    void setDI(byte forward, byte strafe);
    void standMount(StandEntity StandSet);
    void setStand(StandEntity StandSet);
    float getGuardPoints();
    float getMaxGuardPoints();
    void setGuardPoints(float GuardPoints);
    boolean getGuardBroken();
    void fixGuard();
    void regenGuard(float regen);
    void damageGuard(float damage);
    void breakGuard();

    int getAttackTimeMax();
    int getAttackTime();
    int getAttackTimeDuring();
    byte getActivePowerPhase();
    byte getActivePowerPhaseMax();
    byte getActivePower();
    public boolean getInterruptCD();
    void setActive(boolean active);

    void summonStand(World theWorld, boolean forced, boolean sound);
    void sync();

    boolean getActive();
    boolean getMainhandOverride();
    boolean canAttack();
    float getRayDistance(Entity entity, float range);
    float getDistanceOut(Entity entity, float range, boolean offset);
    float getStandReach();

   StandPowers getStandPowers();

   void setStandPowers(StandPowers standPowers);
    void setAttackTimeDuring(int attackTimeDuring);
    void setInterruptCD(int interruptCD);
    void setPowerAttack();
    boolean isGuarding();
    boolean isBarraging();
    float getGuardCooldown();
    boolean isGuardingEffectively();
    boolean isGuardingEffectively2();
    boolean shieldNotDisabled();
    void setPowerGuard();
    void setPowerNone();

   void tryPower(int move, boolean forced);
  void stopSounds(byte soundNumber);

   void setSummonCD(int summonCD);
   boolean getSummonCD();
  int getSummonCD2();
  Entity getTargetEntity(Entity User, float distMax);

   LivingEntity getPowerUser();

    @Nullable
    StandEntity getStand();
}
