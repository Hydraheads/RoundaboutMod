package net.hydra.jojomod.entity.gold_experience;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface GELifeform {
    boolean isReflectingDamage();
    void setReflectingDamage(boolean reflecting);

    @Nullable
    LivingEntity getCreator();
    void setCreator(@Nullable LivingEntity creator);

    int getLifespan();
    void setLifespan(int ticks);

    ItemStack getSourceItem();
    void setSourceItem(ItemStack stack);
}