package net.hydra.jojomod.access;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.util.PlayerMaskSlots;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface IPlayerEntity {
    Inventory roundabout$GetInventory();
    boolean roundabout$getBlinded();
    byte roundabout$getModChest();
    float rdbt$mutualMiningSpeedFunction(BlockState $$0, StandPowers powers);
    void roundabout$setModChest(byte chestType);
    void roundabout$setBlinded(boolean blinded);
    void roundabout$SetPos(byte Pos);
    void roundabout$SetPos2(byte Pos);
    int roundabout$getAirTime();
    int roundabout$getClientDodgeTime();
    int roundabout$getControlling();
    void roundabout$setIsControlling(int pilot);
    int roundabout$getDodgeTime();
    void roundabout$addStandExp(int amt);
    int roundabout$getCameraHits();
    void roundabout$setCameraHits(int ticks);
    void roundabout$setClientDodgeTime(int dodgeTime);
    void roundabout$setDodgeTime(int dodgeTime);
    void roundabout$setAirTime(int airTime);
    void roundabout$setShapeShift(byte level);
    void roundabout$shapeShiftSilent();
    void roundabout$shapeShift();
    void rdbt$setRespawnStrategy(byte strat);
    byte rdbt$getRespawnStrategy();

    float rdbt$getHairColorX();
    float rdbt$getHairColorY();
    float rdbt$getHairColorZ();
    void rdbt$setHairColorX(float color);
    void rdbt$setHairColorY(float color);
    void rdbt$setHairColorZ(float color);

    void roundabout$setFate(byte style);
    byte roundabout$getFate();
    void roundabout$qmessage(int messageID);

    void rdbt$queryServerForCooldowns();
    boolean rdbt$getCooldownQuery();
    void rdbt$setCooldownQuery(boolean query);

    void roundabout$setShapeShiftExtraData(byte level);
    byte roundabout$getShapeShift();
    byte roundabout$getShapeShiftExtraData();
    byte roundabout$GetPos();
    byte roundabout$GetPos2();
    void roundabout$SetPoseEmote(byte Pos);
    byte roundabout$GetPoseEmote();
    void roundabout$addKnife();
    void roundabout$setKnife(byte knives);
    int roundabout$getKnifeCount();
    VoiceData roundabout$getVoiceData();
    void roundabout$setVoiceData(VoiceData vd);
    ItemStack roundabout$getMaskSlot();
    ItemStack roundabout$getMaskVoiceSlot();
    void roundabout$setMaskSlot(ItemStack stack);
    void roundabout$setMaskVoiceSlot(ItemStack stack);
    PlayerMaskSlots roundabout$getMaskInventory();
    void roundabout$setMaskInventory(PlayerMaskSlots pm);
    void roundabout$setStandLevel(byte level);
    byte roundabout$getStandLevel();
    void roundabout$setShowName(boolean boo);
    void roundabout$showExp(boolean keyIsDown);
    boolean roundabout$getDisplayExp();
    void roundabout$setStandExp(int level);
    int roundabout$getStandExp();
    boolean roundabout$getUnlockedBonusSkin();
    byte roundabout$getStandSkin();
    void roundabout$setStandSkin(byte level);
    void roundabout$setIdlePos(byte level);
    byte roundabout$getIdlePos();
    byte roundabout$getTeam();
    void roundabout$setTeamColor(byte color);
    byte roundabout$getTeamColor();
    void roundabout$setWatchStyle(byte style);
    byte roundabout$getWatchStyle();
    void roundabout$setUnlockedBonusSkin(boolean unlock);
    int roundabout$getAnchorPlace();
    int roundabout$getAnchorPlaceAttack();
    void roundabout$setAnchorPlaceAttack(int anchorPlace);
    void roundabout$setAnchorPlace(int anchorPlace);
    float roundabout$getDistanceOut();
    void roundabout$setDistanceOut(float distanceOut);
    float roundabout$getSizePercent();
    void roundabout$setSizePercent(float idleOpacity);
    float roundabout$getIdleRotation();
    void roundabout$setIdleRotation(float combatOpacity);
    float roundabout$getIdleYOffset();
    void roundabout$setIdleYOffset(float enemyOpacity);
    void roundabout$setLastVisage(ItemStack stack);
    AnimationState roundabout$getBubbleAim();
    int roundabout$getBubbleShotAimPoints();
    void roundabout$setBubbleShotAimPoints(int shotPoints);

    AnimationState roundabout$getAnubisUnsheath();
    AnimationState roundabout$getThirdPersonAnubisUnsheath();

    AnimationState roundabout$getSnubnoseAim();

    AnimationState roundabout$getBubbleShotAim();
    AnimationState roundabout$getOffsetCorrect();
    AnimationState getWry();
    AnimationState getGiorno();
    AnimationState getKoichi();
    AnimationState getOhNo();
    AnimationState getSitting();
    AnimationState getJoseph();
    AnimationState getTortureDance();
    AnimationState getWamuu();
    AnimationState getJonathan();
    AnimationState getJotaro();
    AnimationState getWatch();
    AnimationState getVampire();

}
