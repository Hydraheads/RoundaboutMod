package net.hydra.jojomod.entity.mobs;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Piranha extends AbstractSchoolingFish {
    public Piranha(EntityType<? extends Piranha> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSchoolingFish.createAttributes().add(Attributes.ATTACK_DAMAGE, 3.0D);
    }
}
