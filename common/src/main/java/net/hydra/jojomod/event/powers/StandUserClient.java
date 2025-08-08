package net.hydra.jojomod.event.powers;


import net.hydra.jojomod.util.RotationAnimation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface StandUserClient {
    /**This is for EVERY client to individually save sounds and other client variables on entities clientside.
     * It's how we cancel out barrage sounds on a per entity basis.*/
    void roundabout$clientQueSound(byte soundChoice);
    void roundabout$clientPlaySound();
    void roundabout$clientPlaySoundIfNoneActive(byte soundChoice);

    void roundabout$clientQueSoundCanceling(byte soundChoice);

    void roundabout$clientSoundCancel();


    void roundabout$setRoundaboutRenderChest(@Nullable ItemStack chest);
    void roundabout$setRoundaboutRenderLegs(@Nullable ItemStack legs);

    void roundabout$setRoundaboutRenderBoots(@Nullable ItemStack boots);
    void roundabout$setRoundaboutRenderHead(@Nullable ItemStack head);
    void roundabout$setRoundaboutRenderMainHand(@Nullable ItemStack main);
    void roundabout$setRoundaboutRenderOffHand(@Nullable ItemStack main);

    @Nullable ItemStack roundabout$getRoundaboutRenderChest();
    @Nullable ItemStack roundabout$getRoundaboutRenderLegs();
    @Nullable ItemStack roundabout$getRoundaboutRenderBoots();
    @Nullable ItemStack roundabout$getRoundaboutRenderHead();
    @Nullable ItemStack roundabout$getRoundaboutRenderMainHand();
    @Nullable ItemStack roundabout$getRoundaboutRenderOffHand();
}
