package net.hydra.jojomod.access;

public interface IFireworkRocketAccess {
    int sba$GetFireworkRemainingLifeTicks();
    void sba$SetFireworkRemainingLifeTicks(int life);

    void sba$Explode();

    void setIsHattanProj(boolean hattanProj);

    void setIsShotFromCrossbow(boolean isCrossbowF);
}
