package net.hydra.jojomod.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class IronBallCrossbowItem extends CrossbowItem {
    public IronBallCrossbowItem(Properties $$0) {
        super($$0);
    }

    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        int $$4 = this.getUseDuration($$0) - $$3;
        float $$5 = getPowerForTime($$4, $$0);
        if ($$5 >= 1.0F && !isCharged($$0) && tryLoadProjectiles($$2, $$0)) {
            setCharged($$0, true);
            SoundSource $$6 = $$2 instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            $$1.playSound((Player)null, $$2.getX(), $$2.getY(), $$2.getZ(), SoundEvents.CROSSBOW_LOADING_END, $$6, 1.0F, 1.0F / ($$1.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    private static float getPowerForTime(int $$0, ItemStack $$1) {
        float $$2 = (float)$$0 / (float)getChargeDuration($$1);
        if ($$2 > 1.0F) {
            $$2 = 1.0F;
        }

        return $$2;
    }

    private static boolean tryLoadProjectiles(LivingEntity $$0, ItemStack $$1) {
        int $$2 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, $$1);
        int $$3 = $$2 == 0 ? 1 : 3;
        boolean $$4 = $$0 instanceof Player && ((Player)$$0).getAbilities().instabuild;
        ItemStack $$5 = $$0.getProjectile($$1);
        ItemStack $$6 = $$5.copy();

        for(int $$7 = 0; $$7 < $$3; ++$$7) {
            if ($$7 > 0) {
                $$5 = $$6.copy();
            }

            if ($$5.isEmpty() && $$4) {
                $$5 = new ItemStack(Items.IRON_INGOT);
                $$6 = $$5.copy();
            }

            if (!loadProjectile($$0, $$1, $$5, $$7 > 0, $$4)) {
                return false;
            }
        }

        return true;
    }

    private static boolean loadProjectile(LivingEntity $$0, ItemStack $$1, ItemStack $$2, boolean $$3, boolean $$4) {
        if ($$2.isEmpty()) {
            return false;
        } else {
            boolean $$5 = $$4 && $$2.is(Items.IRON_INGOT);
            ItemStack $$6;
            if (!$$5 && !$$4 && !$$3) {
                $$6 = $$2.split(1);
                if ($$2.isEmpty() && $$0 instanceof Player) {
                    ((Player)$$0).getInventory().removeItem($$2);
                }
            } else {
                $$6 = $$2.copy();
            }

            addChargedProjectile($$1, $$6);
            return true;
        }
    }

    private static void addChargedProjectile(ItemStack $$0, ItemStack $$1) {
        CompoundTag $$2 = $$0.getOrCreateTag();
        ListTag $$3;
        if ($$2.contains("ChargedProjectiles", 9)) {
            $$3 = $$2.getList("ChargedProjectiles", 10);
        } else {
            $$3 = new ListTag();
        }

        CompoundTag $$5 = new CompoundTag();
        $$1.save($$5);
        $$3.add($$5);
        $$2.put("ChargedProjectiles", $$3);
    }
}
