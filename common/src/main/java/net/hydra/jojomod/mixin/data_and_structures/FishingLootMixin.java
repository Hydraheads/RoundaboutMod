package net.hydra.jojomod.mixin.data_and_structures;

import net.hydra.jojomod.access.ILootTable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public abstract class FishingLootMixin extends Entity {


    public FishingLootMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "retrieve",at = @At("HEAD"))
    private void roundabout$fuckYouCase(ItemStack $$0, CallbackInfoReturnable<Integer> cir) {
        LootTable table = this.level().getServer().getLootData().getLootTable(BuiltInLootTables.FISHING);
    }
}
