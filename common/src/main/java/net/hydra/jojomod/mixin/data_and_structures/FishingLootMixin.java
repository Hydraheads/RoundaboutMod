package net.hydra.jojomod.mixin.data_and_structures;

import net.hydra.jojomod.access.ILootTable;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(FishingHook.class)
public abstract class FishingLootMixin extends Entity {

    @Shadow @Nullable public abstract Player getPlayerOwner();

    public FishingLootMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "retrieve",at = @At("HEAD"))
    private void sba$fuckYouCase(ItemStack $$0, CallbackInfoReturnable<Integer> cir) {
        LootTable table = this.level().getServer().getLootData().getLootTable(BuiltInLootTables.FISHING);
    }

    @Inject(method = "retrieve",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/critereon/FishingRodHookedTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/projectile/FishingHook;Ljava/util/Collection;)V",
                    ordinal = 1
            ))
    private void sba$HOWRUDE(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (this.getPlayerOwner() instanceof ServerPlayer player
                && ((StandUser) player).sba$getStandPowers() instanceof PowersGoldExperience ge) {
            ge.onFishCaught();
        }
    }
}