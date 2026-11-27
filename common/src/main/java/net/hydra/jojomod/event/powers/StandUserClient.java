package net.hydra.jojomod.event.powers;


import net.hydra.jojomod.util.RotationAnimation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface StandUserClient {
    /**This is for EVERY client to individually save sounds and other client variables on entities clientside.
     * It's how we cancel out barrage sounds on a per entity basis.*/
    void sba$clientQueSound(byte soundChoice, boolean isVoice);
    void sba$clientPlaySound();
    void sba$clientPlaySoundIfNoneActive(byte soundChoice);

    void sba$clientQueSoundCanceling(byte soundChoice);

    void sba$clientSoundCancel();


    void sba$setSBARenderChest(@Nullable ItemStack chest);
    void sba$setSBARenderLegs(@Nullable ItemStack legs);

    void sba$setSBARenderBoots(@Nullable ItemStack boots);
    void sba$setSBARenderHead(@Nullable ItemStack head);
    void sba$setSBARenderMainHand(@Nullable ItemStack main);
    void sba$setSBARenderOffHand(@Nullable ItemStack main);

    @Nullable ItemStack sba$getSBARenderChest();
    @Nullable ItemStack sba$getSBARenderLegs();
    @Nullable ItemStack sba$getSBARenderBoots();
    @Nullable ItemStack sba$getSBARenderHead();
    @Nullable ItemStack sba$getSBARenderMainHand();
    @Nullable ItemStack sba$getSBARenderOffHand();
}
