package net.hydra.jojomod.access;

public interface IDayInterpolationClientLevelData {
    long sba$getSBADayTimeActual();
    long sba$getSBADayTimeTarget();
    void sba$setSBADayTimeActual(long sbaDayTimeActual);
    void sba$setSBADayTimeTarget(long sbaDayTimeTarget);
    boolean sba$getSBAInterpolatingDaytime();
    void sba$setSBAInterpolatingDaytime(boolean sbaTimeStopInitialized);
    long sba$getSBADayTimeMinecraft();
    void sba$setSBATimeStopInitialized(boolean sbaTimeStopInitialized);
    boolean sba$getSBATimeStopInitialized();

    void sba$sbaInitializeTS();
    void sba$setInterpolationMaxStep(long value);
    long sba$getInterpolationMaxStep();

}
