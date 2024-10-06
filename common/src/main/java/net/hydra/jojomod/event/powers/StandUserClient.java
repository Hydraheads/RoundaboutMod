package net.hydra.jojomod.event.powers;


public interface StandUserClient {
    /**This is for EVERY client to individually save sounds and other client variables on entities clientside.
     * It's how we cancel out barrage sounds on a per entity basis.*/
    void roundabout$clientQueSound(byte soundChoice);
    void roundabout$clientPlaySound();
    void roundabout$clientPlaySoundIfNoneActive(byte soundChoice);

    void roundabout$clientQueSoundCanceling(byte soundChoice);

    void roundabout$clientSoundCancel();
}
