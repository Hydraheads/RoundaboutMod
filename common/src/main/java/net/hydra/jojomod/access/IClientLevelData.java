package net.hydra.jojomod.access;

public interface IClientLevelData {
    long getRoundaboutDayTimeActual();
    long getRoundaboutDayTimeTarget();
    void setRoundaboutDayTimeActual(long roundaboutDayTimeActual);
    void setRoundaboutDayTimeTarget(long roundaboutDayTimeTarget);
    boolean getRoundaboutInterpolatingDaytime();
    void setRoundaboutInterpolatingDaytime(boolean roundaboutTimeStopInitialized);
    long getRoundaboutDayTimeMinecraft();
    void setRoundaboutTimeStopInitialized(boolean roundaboutTimeStopInitialized);
    boolean getRoundaboutTimeStopInitialized();

    void roundaboutInitializeTS();

}
