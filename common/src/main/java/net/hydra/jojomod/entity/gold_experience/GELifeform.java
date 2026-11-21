package net.hydra.jojomod.entity.gold_experience;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface GELifeform {
    boolean isReflectingDamage();
    void setReflectingDamage(boolean reflecting);

    @Nullable
    UUID getCreatorUUID();
    void setCreatorUUID(@Nullable UUID uuid);

    @Nullable
    LivingEntity getCreator();
    void setCreator(@Nullable LivingEntity creator);

    int getLifespan();
    void setLifespan(int ticks);

    ItemStack getSourceItem();
    void setSourceItem(ItemStack stack);

    int GROW_DURATION_TICKS = 10;

    default boolean isGrowing(LivingEntity self) {
        return self.tickCount < GROW_DURATION_TICKS;
    }

    default float getGrowthProgress(LivingEntity self, float partialTicks) {
        return Math.min(1.0F, (self.tickCount + partialTicks) / (float) GROW_DURATION_TICKS);
    }

    default float getGrowthScale(LivingEntity self, float partialTicks) {
        float progress = getGrowthProgress(self, partialTicks);
        return 0.05F + 0.95F * (1.0F - (float) Math.pow(1.0F - progress, 3.0));
    }

    default float getItemShrinkScale(LivingEntity self, float partialTicks) {
        float progress = getGrowthProgress(self, partialTicks);
        return Math.max(0.0F, 1.0F - progress);
    }
}