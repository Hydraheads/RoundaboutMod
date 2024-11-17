package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class GlaiveItem extends SwordItem {
    /**Glaives inspired by the Harken Scythe mod, with sound design from the Hexxit modpack.
     * I like the way it functioned there, but I added my own bleed effect as a part of
     * the mechanism of how it works*/
    private final float chargeDamage;

    public GlaiveItem(Tier $$0, float $$1, float $$2, Properties $$3, float chargeDamage) {
        super($$0, (int) $$1, $$2, $$3);
        this.chargeDamage =chargeDamage;
    }

    public GlaiveItem(Tier $$0, int $$1, float $$2, Properties $$3, float chargeDamage) {
        super($$0, $$1, $$2, $$3);
        this.chargeDamage =chargeDamage;
    }



    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if ($$1.getAttackStrengthScale(1) >= 1F) {
            $$1.startUsingItem($$2);
                if ($$1.getUseItem() == $$3 && $$1.getUseItemRemainingTicks() == $$1.getUseItem().getUseDuration()) {
                    if ($$0.isClientSide) {
                        ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_GLAIVE_START_SOUND);
                    }
                    return InteractionResultHolder.success($$3);
                }
        }
        return InteractionResultHolder.fail($$3);
    }


    @Override
    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        if ($$2 instanceof Player $$4) {
            int $$5 = this.getUseDuration($$0) - $$3;
            int itemTime = 20;
            if ($$1.isClientSide) {
                ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_ITEM_STOP_SOUND);
            }
            if ($$5 >= itemTime) {
                if ($$1.isClientSide){
                    float glaiveReach = 3F;
                    if (((Player)$$2).isCreative()){
                        glaiveReach = 5F;
                    }
                    ModPacketHandler.PACKET_ACCESS.glaivePacket($$0, MainUtil.getTargetEntityId($$2, glaiveReach));
                }
                InteractionHand interactionhand = $$2.getUsedItemHand();
                $$2.swing(interactionhand);
                ((Player) $$2).resetAttackStrengthTicker();
                $$4.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    public void glaiveAttack(ItemStack $$0, Level $$1, ServerPlayer player, Entity target){
        if (player.getInventory().contains($$0)) {
            $$1.playSound(null, player, ModSounds.GLAIVE_ATTACK_EVENT, SoundSource.PLAYERS, 1F, 1F);
            if (target != null) {
                float power = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                float $$2;
                if (target instanceof LivingEntity) {
                    $$2 = EnchantmentHelper.getDamageBonus($$0, ((LivingEntity) target).getMobType());
                } else {
                    $$2 = EnchantmentHelper.getDamageBonus($$0, MobType.UNDEFINED);
                }
                power += $$2;
                power += this.chargeDamage;
                if (target.hurt(ModDamageTypes.of($$1, ModDamageTypes.GLAIVE, player), power)) {
                    if (!player.isCreative()) {
                        ItemStack item = player.getInventory().getItem((player.getInventory().findSlotMatchingItem($$0)));
                        item.hurt(1, $$1.getRandom(), player);
                    }
                    if (target instanceof LivingEntity) {
                        ((LivingEntity) target).knockback(0.35f, player.getX() - target.getX(), player.getZ() - target.getZ());
                        if (MainUtil.getMobBleed(target)) {
                            if ($$0.getItem() instanceof GlaiveItem GI && GI.getTier() == Tiers.WOOD){
                                MainUtil.makeBleed(target,0,400, player);
                            } else {
                                MainUtil.makeBleed(target,1,400, player);
                                MainUtil.makeMobBleed(target);
                            }
                        }
                    }
                } else {
                    if (target instanceof LivingEntity) {
                        MainUtil.knockShield(target, 200);
                    }
                }
            }
        }
    }

}
