package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.RipperEyesProjectile;
import net.hydra.jojomod.entity.projectile.UltravioletProjectile;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class UltravioletBlasterItem extends Item implements Vanishable {
    public UltravioletBlasterItem(Properties $$0) {
        super($$0);
    }
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$0.playSound((Player)null, $$1.getX(), $$1.getY(), $$1.getZ(), ModSounds.UV_BLAST_EVENT, SoundSource.NEUTRAL, 1F, (float)(0.96F+Math.random()*0.08f));
        $$1.getCooldowns().addCooldown(this, 20);
        if (!$$0.isClientSide) {
            $$3.hurtAndBreak(1, $$1, ($$1x) -> $$1x.broadcastBreakEvent($$2));
            UltravioletProjectile bubble = new UltravioletProjectile($$1,$$1.level());
            bubble.absMoveTo($$1.getX(), $$1.getY(), $$1.getZ());
            bubble.setUser($$1);
            bubble.setOwner($$1);
            bubble.shootThis($$1);
            $$1.level().addFreshEntity(bubble);
        }

        $$1.awardStat(Stats.ITEM_USED.get(this));

        return InteractionResultHolder.sidedSuccess($$3, $$0.isClientSide());
    }
    public static int durability = 100;
}
