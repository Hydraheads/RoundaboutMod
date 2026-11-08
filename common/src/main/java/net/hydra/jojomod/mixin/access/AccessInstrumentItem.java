package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.IInstrumentItemAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Optional;

@Mixin(InstrumentItem.class)
public abstract class AccessInstrumentItem implements IInstrumentItemAccess {
    //Mojang likes to make every method private so I need to unprivate some methods for Black Sabbath. -Signed, 14Kacper

    @Override
    public Optional<? extends Holder<Instrument>> roundabout$GetInstrument(ItemStack p_220135_) {
        return this.getInstrument(p_220135_);
    }

    /// SHADOWS, IGNORE
    @Shadow
    private Optional<? extends Holder<Instrument>> getInstrument(ItemStack p_220135_) {
        return null;
    }
}
