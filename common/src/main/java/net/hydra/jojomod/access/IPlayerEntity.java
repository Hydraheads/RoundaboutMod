package net.hydra.jojomod.access;

import net.hydra.jojomod.event.VampireData;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.util.BlackSabbathPlayerInventory;
import net.hydra.jojomod.util.PlayerMaskSlots;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Deque;

public interface IPlayerEntity {
    Inventory sba$GetInventory();
    boolean sba$getBlinded();
    byte sba$getModChest();
    float rdbt$mutualMiningSpeedFunction(BlockState $$0, StandPowers powers);
    float rdbt$mutualMiningSpeedFunction2(BlockState $$0, GeneralPowers powers);
    void sba$setModChest(byte chestType);
    void sba$setBlinded(boolean blinded);
    void sba$setCombatMode(boolean only);
    boolean sba$getCombatMode();
    boolean sba$getEffectiveCombatMode();
    void sba$SetPos(byte Pos);
    void rdbt$setCooldownQuery2();
    Deque<Vec3> rdbt$getMovementHistory();
    void sba$SetPos2(byte Pos);
    int rdbt$getFlagTicks();
    void rdbt$setLevelDecreaseTicks(int decreaseTicks);
    int rdbt$getLevelDecreaseTicks();
    int sba$getAirTime();
    int sba$getClientDodgeTime();
    int sba$getControlling();
    int sba$getAttackStrengthTicker();
    void sba$setIsControlling(int pilot);
    int sba$getDodgeTime();
    void sba$addStandExp(int amt);
    void sba$setWidthFactor(byte level);
    byte sba$getWidthFactor();
    int sba$getCameraHits();
    void sba$setCameraHits(int ticks);
    void sba$setClientDodgeTime(int dodgeTime);
    void sba$setDodgeTime(int dodgeTime);
    void sba$setAirTime(int airTime);
    void sba$setShapeShift(byte level);
    void sba$shapeShiftSilent();
    void sba$shapeShift();
    void rdbt$setRespawnStrategy(byte strat);
    byte rdbt$getRespawnStrategy();
    void sba$setPower(byte style);
    byte sba$getPower();
    void sba$setPowerWithPenalty(byte style);

    /// Zombie Fate's zombie silverfish count
    void rdbt$setZombieFish(int fish);
    int rdbt$getZombieFish();

    /// bypasses the combat mode overrides
    ItemStack sba$getForRealMainHand();
    ItemStack sba$getForRealOffHand();

    float rdbt$getHairColorX();
    float rdbt$getHairColorY();
    float rdbt$getHairColorZ();
    void rdbt$setHairColorX(float color);
    void rdbt$setHairColorY(float color);
    void rdbt$setHairColorZ(float color);

    void sba$setFate(byte style);
    byte sba$getFate();
    void sba$qmessage(int messageID);

    void rdbt$queryServerForCooldowns();
    boolean rdbt$getCooldownQuery();
    void rdbt$setCooldownQuery(boolean query);


    void rdbt$setVampireData(VampireData vdata);
    VampireData rdbt$getVampireData();
    void sba$setShapeShiftExtraData(byte level);
    byte sba$getShapeShift();
    byte sba$getShapeShiftExtraData();
    byte sba$GetPos();
    byte sba$GetPos2();
    void sba$SetPoseEmote(byte Pos);
    void rdbt$onSyncedDataUpdated(EntityDataAccessor<?> $$0);
    byte sba$GetPoseEmote();
    void sba$addKnife();
    void sba$setKnife(byte knives);
    int sba$getKnifeCount();
    VoiceData sba$getVoiceData();
    void sba$setVoiceData(VoiceData vd);
    ItemStack sba$getMaskSlot();
    ItemStack sba$getMaskVoiceSlot();
    void sba$setMaskSlot(ItemStack stack);
    void sba$setMaskVoiceSlot(ItemStack stack);
    PlayerMaskSlots sba$getMaskInventory();
    void sba$setMaskInventory(PlayerMaskSlots pm);
    void sba$setStandLevel(byte level);
    byte sba$getStandLevel();
    void sba$setShowName(boolean boo);
    void sba$showExp(boolean keyIsDown);
    boolean sba$getDisplayExp();
    void sba$setStandExp(int level);
    int sba$getStandExp();
    boolean sba$getUnlockedBonusSkin();
    byte sba$getStandSkin();
    void sba$setStandSkin(byte level);
    void sba$setIdlePos(byte level);
    byte sba$getIdlePos();
    AnimationState sba$getBarrageArms();
    byte sba$getTeam();
    void sba$setTeamColor(byte color);
    byte sba$getTeamColor();
    void sba$setWatchStyle(byte style);
    byte sba$getWatchStyle();
    void sba$setUnlockedBonusSkin(boolean unlock);
    int sba$getAnchorPlace();
    int sba$getAnchorPlaceAttack();
    void sba$setAnchorPlaceAttack(int anchorPlace);
    void sba$setAnchorPlace(int anchorPlace);
    float sba$getDistanceOut();
    void sba$setDistanceOut(float distanceOut);
    float sba$getSizePercent();
    void sba$setSizePercent(float idleOpacity);
    float sba$getIdleRotation();
    void sba$setIdleRotation(float combatOpacity);
    float sba$getIdleYOffset();
    void sba$setIdleYOffset(float enemyOpacity);
    void sba$setLastVisage(ItemStack stack);
    AnimationState sba$getBubbleAim();
    int sba$getBubbleShotAimPoints();
    void sba$setBubbleShotAimPoints(int shotPoints);
    BlackSabbathPlayerInventory sba$getBlckSabbathPlayerInventory();
    void sba$setBlckSabbathPlayerInventory(BlackSabbathPlayerInventory bsi);


    AnimationState sba$getItemAnimationActive();
    AnimationState sba$getItemAnimation();

    AnimationState sba$getBubbleShotAim();
    AnimationState sba$getOffsetCorrect();
    AnimationState getStyleAnimation();

    long sba$getPurpleHazePodResetDay();
    void sba$setPurpleHazePodResetDay(long day);

    byte sba$getPurpleHazePods();
    void sba$setPurpleHazePods(byte pods);

    int rdbt$getGerDeathLoops();
    void rdbt$setGerDeathLoops(int loops);
    // used to temp clear the inventory for things like keep inventory true
    ListTag rdbt$getGerSavedInventory();
    void rdbt$setGerSavedInventory(ListTag inv);
}
