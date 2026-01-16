package net.hydra.jojomod.access;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.text.html.parser.Entity;

public interface IHumanoidArmorLayer {
    int getRdbt$killSwitch();
    void setRdbt$killSwitch(int rdbt$killSwitch);
}
