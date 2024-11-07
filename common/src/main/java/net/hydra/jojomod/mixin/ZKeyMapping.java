package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.hydra.jojomod.access.IKeyMapping;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(KeyMapping.class)
public class ZKeyMapping implements IKeyMapping {
    @Shadow
    private InputConstants.Key key;

    @Unique
    @Override
    public InputConstants.Key roundabout$justTellMeTheKey(){
        return key;
    }
}
