package net.hydra.jojomod.access;

public interface IFireworkRocketAccess {
    int roundabout$GetFireworkRemainingLifeTicks();
    void roundabout$SetFireworkRemainingLifeTicks(int life);

    void roundabout$Explode();

    void setIsHattanProj(boolean hattanProj);

    void setIsShotFromCrossbow(boolean isCrossbowF);
}
