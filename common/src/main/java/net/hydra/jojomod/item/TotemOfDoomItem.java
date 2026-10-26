package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.TotemOfDoomEntity;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraft.world.phys.Vec3;

public class TotemOfDoomItem  extends Item {
    public TotemOfDoomItem(Item.Properties $$0) {
        super($$0);
    }

    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$0.playSound((Player)null, $$1.getX(), $$1.getY(), $$1.getZ(), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 1F, 0.2F / ($$0.getRandom().nextFloat() * 0.2F + 0.9F));
        $$1.getCooldowns().addCooldown(this, 20);
        if (!$$0.isClientSide) {
            //main util has a thing already that can prevent this from spawning in walls
            Vec3 spawnPos = MainUtil.getAheadVec($$1, 0.5F).getLocation();
            TotemOfDoomEntity totemOfDoom = new TotemOfDoomEntity($$0, spawnPos.x, spawnPos.y - 0.4, spawnPos.z);
            totemOfDoom.setItem($$3);
            $$0.addFreshEntity(totemOfDoom);
        }

        $$1.awardStat(Stats.ITEM_USED.get(this));
        if (!$$1.getAbilities().instabuild) {
            $$3.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess($$3, $$0.isClientSide());
    }
}
