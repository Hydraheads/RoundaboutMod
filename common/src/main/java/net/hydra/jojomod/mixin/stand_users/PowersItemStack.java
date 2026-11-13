package net.hydra.jojomod.mixin.stand_users;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.AnimalTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class PowersItemStack {

    /**Stand code to gain experience from mining blocks.*/

    @Inject(method = "mineBlock(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$mineBlock(Level $$0, BlockState $$1, BlockPos $$2, Player $$3, CallbackInfo ci) {
        StandPowers powers = ((StandUser) $$3).roundabout$getStandPowers();
        if (PowerTypes.hasStandActive($$3) && powers.canUseMiningStand()) {
            powers.gainExpFromSpecialMining($$1, $$2);
            ci.cancel();
        } else if (PowerTypes.isBrawling($$3)){
            ci.cancel();
        } else {
            powers.gainExpFromStandardMining($$1,$$2);
        }
    }
    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$fortHurt(int $$0, RandomSource $$1, ServerPlayer $$2, CallbackInfoReturnable<Boolean> cir) {
        if ($$2 != null){
            if ($$2.hasEffect(ModEffects.FORTIFICATION)){
                cir.setReturnValue(false);
            }
        }
    }
    @Inject(method = "hurtAndBreak", at = @At(value = "HEAD"), cancellable = true)
    protected <T extends LivingEntity> void roundabout$fortHurtAndBreak(int $$0, T $$1, Consumer<T> $$2, CallbackInfo ci) {
        if ($$1 != null){
            if ($$1.hasEffect(ModEffects.FORTIFICATION)){
                ci.cancel();
            }
        }
    }

    // Gold Experience item imbue start

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void roundabout$appendGEImbueTooltip(@Nullable Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir) {
        ItemStack self = (ItemStack) (Object) this;
        CompoundTag imbue = PowersGoldExperience.getImbueData(self);
        if (imbue == null) return;

        List<Component> lines = cir.getReturnValue();
        byte imbueType = imbue.getByte("Type");

        if (imbueType == PowersGoldExperience.IMBUE_ANIMAL) {
            byte animalId = imbue.getByte("AnimalId");
            Component animalName = getAnimalDisplayName(animalId);
            lines.add(1, Component.translatable("tooltip.roundabout.ge_imbue.animal", animalName)
                    .withStyle(ChatFormatting.GOLD));
        } else if (imbueType == PowersGoldExperience.IMBUE_TREE) {
            lines.add(1, Component.translatable("tooltip.roundabout.ge_imbue.tree")
                    .withStyle(ChatFormatting.DARK_GREEN));
        } else if (imbueType == PowersGoldExperience.IMBUE_DISGUISE) {
            lines.add(1, Component.translatable("tooltip.roundabout.ge_imbue.disguise")
                    .withStyle(ChatFormatting.AQUA));
        }
    }

    private static Component getAnimalDisplayName(byte animalId) {
        EntityType<?> entityType = PowersGoldExperience.getAnimalEntityType(animalId);
        if (entityType != null) {
            return entityType.getDescription();
        }
        return Component.literal("bugged lol");
    }

    // next 3 methods are all needed to make sure that the imbue triggers, regardless of what item is used
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void roundabout$onUseGEImbuedItem(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (PowersGoldExperience.getImbueData(self) != null) {
            if (!level.isClientSide()) {
                PowersGoldExperience.tryActivateImbuedItem(player, self);
            }
            cir.setReturnValue(InteractionResultHolder.sidedSuccess(self, level.isClientSide()));
        }
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void roundabout$onUseOnGEImbuedItem(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack self = (ItemStack) (Object) this;
        Player player = context.getPlayer();
        if (player != null && PowersGoldExperience.getImbueData(self) != null) {
            Level level = context.getLevel();
            if (!level.isClientSide()) {
                PowersGoldExperience.tryActivateImbuedItem(player, self);
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        }
    }

    @Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
    private void roundabout$onInteractEntityGEImbuedItem(Player player, LivingEntity target, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (PowersGoldExperience.getImbueData(self) != null) {
            Level level = player.level();
            if (!level.isClientSide()) {
                PowersGoldExperience.tryActivateImbuedItem(player, self);
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        }
    }

    // Gold Experience item imbue end
}
