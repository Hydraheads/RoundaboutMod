package net.hydra.jojomod.access;

public interface IClientLevelData {
    long getRoundaboutDayTimeActual();
    long getRoundaboutDayTimeTarget();
    void setRoundaboutDayTimeActual(long roundaboutDayTimeActual);
    void setRoundaboutDayTimeTarget(long roundaboutDayTimeTarget);
    long getRoundaboutDayTimeMinecraft();
    void setRgundaboutTimeStopInitialized(boolean roundaboutTimeStopInitialized);
    boolean getRgundaboutTimeStopInitialized();

    void roundaboutInitializeTS();

}
