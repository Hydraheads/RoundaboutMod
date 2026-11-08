package net.hydra.jojomod.access;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface IInstrumentItemAccess {
    //Mojang likes to make every method private so I need to unprivate some methods for Black Sabbath. -Signed, 14Kacper
    Optional<? extends Holder<Instrument>> roundabout$GetInstrument(ItemStack p_220135_);
}