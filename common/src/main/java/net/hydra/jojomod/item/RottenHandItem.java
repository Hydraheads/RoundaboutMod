package net.hydra.jojomod.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RottenHandItem extends Item {
    public static final String TAG_HAND_OWNER = "HandOwner";

    public RottenHandItem(Properties $$0) {
        super($$0);
    }

    @Override
    public Component getName(ItemStack $$0) {
        if ($$0.is(ModItems.HAND) && $$0.hasTag()) {
            String $$1 = null;
            CompoundTag $$2 = $$0.getTag();
            if ($$2.contains(TAG_HAND_OWNER, 8)) {
                $$1 = $$2.getString(TAG_HAND_OWNER);
            } else if ($$2.contains(TAG_HAND_OWNER, 10)) {
                CompoundTag $$3 = $$2.getCompound(TAG_HAND_OWNER);
                if ($$3.contains("Name", 8)) {
                    $$1 = $$3.getString("Name");
                }
            }

            if ($$1 != null) {
                return Component.translatable(this.getDescriptionId() + ".named", new Object[]{$$1});
            }
        }

        return super.getName($$0);
    }
}
