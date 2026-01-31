package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandArrowItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class AnubisAbstractIllagerMixin {

    @Inject(method = "mobInteract",at=@At(value = "HEAD"))
    private void roundabout$giveAnubis(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {
        if (!$$0.level().isClientSide) {
            ItemStack stack = $$0.getItemInHand($$1);
            if ($$0.isCrouching() && stack.getItem() instanceof AnubisItem) {
                if (((Mob) (Object) this) instanceof AbstractIllager AI) {
                    if (!((StandUser) AI).roundabout$hasAStand()) {
                        $$0.setItemInHand($$1,new ItemStack(Items.AIR));
                        $$0.level().playSound(null,$$0.blockPosition(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS,1F,1F);

                        ItemStack itemStack = new ItemStack(ModItems.STAND_DISC_ANUBIS);
                        CompoundTag tag = itemStack.getOrCreateTagElement("Special");
                        tag.putByte("Type",(byte)1);


                        StandArrowItem.grantStand(itemStack, AI);
                        AI.setTarget($$0);

                    }
                }
            }
        }
    }

}
