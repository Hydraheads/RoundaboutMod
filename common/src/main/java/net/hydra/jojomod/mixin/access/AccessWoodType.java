package net.hydra.jojomod.mixin.access;

import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WoodType.class)
public interface AccessWoodType {

    @Invoker("register")
    static WoodType roundabout$register(WoodType woodType) {
        throw new AssertionError();
    }
}