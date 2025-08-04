package net.hydra.jojomod.access;

public interface IDayInterpolationClientLevelData {
    long roundabout$getRoundaboutDayTimeActual();
    long roundabout$getRoundaboutDayTimeTarget();
    void roundabout$setRoundaboutDayTimeActual(long roundaboutDayTimeActual);
    void roundabout$setRoundaboutDayTimeTarget(long roundaboutDayTimeTarget);
    boolean roundabout$getRoundaboutInterpolatingDaytime();
    void roundabout$setRoundaboutInterpolatingDaytime(boolean roundaboutTimeStopInitialized);
    long roundabout$getRoundaboutDayTimeMinecraft();
    void roundabout$setRoundaboutTimeStopInitialized(boolean roundaboutTimeStopInitialized);
    boolean roundabout$getRoundaboutTimeStopInitialized();

    void roundabout$roundaboutInitializeTS();

}
