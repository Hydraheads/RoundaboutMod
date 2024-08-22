package net.hydra.jojomod.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;

public class GlaiveItem extends SwordItem {

    public GlaiveItem(Tier $$0, float $$1, float $$2, Properties $$3) {
        super($$0, (int) $$1, $$2, $$3);
    }

    public GlaiveItem(Tier $$0, int $$1, float $$2, Properties $$3) {
        super($$0, $$1, $$2, $$3);
    }


    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
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
