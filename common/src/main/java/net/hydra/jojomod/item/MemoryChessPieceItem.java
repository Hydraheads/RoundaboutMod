package net.hydra.jojomod.item;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

public class MemoryChessPieceItem extends Item implements Vanishable {
    public MemoryChessPieceItem(Properties $$0) {
        super($$0.defaultDurability(6));
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);

        return InteractionResultHolder.sidedSuccess($$3, $$0.isClientSide());
    }

    public static ItemStack initializePiece(ItemStack stack, Entity victim, int stealType){
        if (victim != null){
            stack.getOrCreateTag().putUUID("victim",victim.getUUID());
            stack.getOrCreateTag().putInt("stealType",stealType);
        }
        return stack;
    }
}
