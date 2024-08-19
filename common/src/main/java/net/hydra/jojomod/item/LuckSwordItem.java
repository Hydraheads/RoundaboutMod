package net.hydra.jojomod.item;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Shadow;

public class LuckSwordItem extends SwordItem {

    public LuckSwordItem(Tier $$0, float $$1, float $$2, Properties $$3) {
        super($$0, (int) $$1, $$2, $$3);
    }



    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }

        /*
    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.consume($$3);
    }
        */

}
