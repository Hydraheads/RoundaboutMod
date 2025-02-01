package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.corpses.FallenZombie;
import net.minecraft.nbt.CompoundTag;
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

    public static int bodyLimit = 16;
    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if ($$0.isClientSide) {
            ClientUtil.openCorpseBag($$3);
        }
         return InteractionResultHolder.pass($$3);
    }

    public boolean fillWithBody(ItemStack stack, FallenMob fm){
        if (stack.getItem() instanceof BodyBagItem BBI){

            if (stack.is(ModItems.CREATIVE_BODY_BAG)){
                return true;
            }
            int bodyCount;
            CompoundTag $$1 = stack.getOrCreateTagElement("bodies");
            String str = fm.getData();
            bodyCount = $$1.getInt(str);
            bodyCount++;
            if (bodyCount <= bodyLimit){
                $$1.putInt(str,bodyCount);
                return true;
            }
        }
        return false;
    }

}
