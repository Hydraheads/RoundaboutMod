package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.IVillagerAccess;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MemoryChessPieceItem extends Item implements Vanishable {
    public MemoryChessPieceItem(Properties $$0) {
        super($$0.defaultDurability(3));
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.consume($$3);
    }
    /**Default 72000*/
    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }
    @Override
    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        if ($$2 instanceof Player $$4) {
            int $$5 = this.getUseDuration($$0) - $$3;
            int itemTime = 7;
            if ($$5 >= itemTime) {

                if (!$$1.isClientSide) {
                    String comp = $$0.getOrCreateTag().getString("vicName");
                    if (comp != null && !comp.isBlank()){
                        $$1.playSound(null, $$4.getX(), $$4.getY(), $$4.getZ(), ModSounds.CINDERELLA_SPARKLE_EVENT, SoundSource.NEUTRAL, 1F, 1F);
                        $$4.displayClientMessage(Component.translatable("text.roundabout.memory."+
                                $$0.getOrCreateTag().getInt("stealType")).withStyle(ChatFormatting.AQUA), true);
                    }
                }
            }
        }
    }
    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    public static ItemStack initializePiece(ItemStack stack, Entity victim, int stealType){
        if (victim != null){
            stack.getOrCreateTag().putUUID("victim",victim.getUUID());
            stack.getOrCreateTag().putInt("stealType",stealType);
            stack.getOrCreateTag().putInt("swings",0);
            stack.getOrCreateTag().putBoolean("activated",true);
            stack.getOrCreateTag().putString("vicName", victim.getName().getString());
            if (stealType == 11 && victim instanceof Villager vg){
                Roundabout.LOGGER.info("1");
                GossipContainer gossips = vg.getGossips();
                Tag gossipTag = gossips.store(NbtOps.INSTANCE);
                stack.getOrCreateTag().put("StoredGossip", gossipTag);
                ((IVillagerAccess)vg).roundabout$clearGossips();
            }
        }
        return stack;
    }
    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        String comp = $$0.getOrCreateTag().getString("vicName");
        boolean comp2 = $$0.getOrCreateTag().hasUUID("victim");
        if (comp != null && !comp.isBlank()){
            ChatFormatting formatting = ChatFormatting.LIGHT_PURPLE;
             $$2.add(Component.literal(comp).withStyle(formatting));
            //$$2.add(Component.translatable("text.roundabout.memory."+
            //        $$0.getOrCreateTag().getInt("stealType")).withStyle(ChatFormatting.BLUE));
            if (!$$0.getOrCreateTag().getBoolean("activated")){
                $$2.add(Component.translatable("text.roundabout.inactive_piece").withStyle(ChatFormatting.AQUA));
            }
        }
    }



    public static void attackThePerson(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack != null && !(stack.getItem() instanceof MemoryChessPieceItem)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }
        player.swing(InteractionHand.MAIN_HAND,true);

        CompoundTag tag = stack.getTag();
        if (tag == null){
            return;
        }
        boolean activated = tag.getBoolean("activated");
        if (!tag.hasUUID("victim") || !activated) {
            return;
        }

        UUID uuid = tag.getUUID("victim");

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Entity entity = serverLevel.getEntity(uuid);

        if (entity != null) {
            if (!entity.isAlive()) {
                if (tag.hasUUID("victim") &&
                        entity.getUUID().equals(tag.getUUID("victim"))) {

                    tag.putBoolean("activated",false);
                    stack.setDamageValue(0);
                    player.setItemSlot(EquipmentSlot.MAINHAND, stack);
                }
            } else if (entity instanceof LivingEntity living && living.hurtTime <= 7) {
                float dmg = 3;
                if (living instanceof Player pl){
                    dmg = 1.5F;
                }
                if (living.hurt(ModDamageTypes.of(living.level(), ModDamageTypes.CHESS_STRIKE, player), dmg) && !living.isAlive()) {
                    player.getMainHandItem().hurtAndBreak(4, player, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                    if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof MemoryChessPieceItem){
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    }
                } else {
                    int destroy = tag.getInt("swings");
                    if (!player.getAbilities().instabuild) {
                        destroy++;
                    }
                    MainUtil.makeBleed(living,0,200,player);
                    player.getMainHandItem().hurtAndBreak(1, player, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));

                    if (destroy >= 3 && !player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof MemoryChessPieceItem){
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        if (!player.getAbilities().instabuild) {
                            tag.putInt("swings", destroy);
                        }
                    }
                }
                if (!player.getMainHandItem().isEmpty()){
                    if (player.level() instanceof ServerLevel sl){
                        sl.playSound(null, player.blockPosition(),
                                ModSounds.CKB_ATTACK_EVENT, SoundSource.PLAYERS, 1F,
                                (float) (0.99f + Math.random() * 0.02f));
                    }
                }

            }
        } else {
            tag.putBoolean("activated",false);
            stack.setDamageValue(0);
            player.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }
    }
}
