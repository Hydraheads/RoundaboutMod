package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BodyBagItem extends Item {
    public BodyBagItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if ($$0.isClientSide) {
            ClientUtil.openCorpseBag($$3);
        }
         return InteractionResultHolder.pass($$3);
    }

}
