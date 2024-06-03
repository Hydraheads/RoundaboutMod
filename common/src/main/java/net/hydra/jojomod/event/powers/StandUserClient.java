package net.hydra.jojomod.event.powers;

public interface StandUserClient {
    /**This is for EVERY client to individually save sounds and other client variables on entities clientside.
     * It's how we cancel out barrage sounds on a per entity basis.*/
    void clientQueSound(byte soundChoice);
    void clientPlaySound();
    boolean getSoundPlay();
    boolean getSoundCancel();
    void clientQueSoundCanceling();

    void clientSoundCancel();
    float getPreTSTickDelta();
    void setPreTSTickDelta();
    void resetPreTSTickDelta();
}
