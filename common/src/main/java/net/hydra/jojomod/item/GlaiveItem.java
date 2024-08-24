package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

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

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.consume($$3);
    }


    @Override
    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        if ($$2 instanceof Player $$4) {
            int $$5 = this.getUseDuration($$0) - $$3;
            int itemTime = 10;
            if ($$5 >= itemTime) {
                if (!$$1.isClientSide){
                    $$1.playSound(null, $$2, ModSounds.GLAIVE_ATTACK_EVENT, SoundSource.PLAYERS, 1F, 1F);
                } else {

                    ModPacketHandler.PACKET_ACCESS.glaivePacket($$0, MainUtil.getTargetEntityId($$2));
                }
                InteractionHand interactionhand = $$2.getUsedItemHand();
                $$2.swing(interactionhand);
                $$4.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    public void glaiveAttack(ItemStack $$0, Level $$1, ServerPlayer player, Entity target){

        float power = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float $$2;
        if (target instanceof LivingEntity) {
            $$2 = EnchantmentHelper.getDamageBonus($$0, ((LivingEntity)target).getMobType());
        } else {
            $$2 = EnchantmentHelper.getDamageBonus($$0, MobType.UNDEFINED);
        }
        power += $$2;
        Roundabout.LOGGER.info(String.valueOf(power));
        power += 12;
        if (target.hurt(ModDamageTypes.of($$1, ModDamageTypes.GLAIVE), power)){
            $$0.hurt(1,$$1.getRandom(),player);
        }
    }

}
