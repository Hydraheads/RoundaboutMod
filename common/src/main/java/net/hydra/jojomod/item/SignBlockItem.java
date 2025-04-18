package net.hydra.jojomod.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class SignBlockItem extends BlockItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public SignBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> $$4 = ImmutableMultimap.builder();
        $$4.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 8.5, AttributeModifier.Operation.ADDITION));
        $$4.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.7, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = $$4.build();
    }


    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot $$0) {
        return $$0 == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers($$0);
    }
    public float getDamage() {
        return 9.2F;
    }
    @Override
    public boolean hurtEnemy(ItemStack $$0, LivingEntity $$1, LivingEntity $$2) {
        if (!$$1.level().isClientSide()) {
            if (MainUtil.getMobBleed($$1)){
                MainUtil.makeBleed($$1,1,100,$$2);
            }
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            ctd++;
            if (ctd > 2) {
                $$1.level().playSound(null, $$1.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS,
                        1F, 1);
                $$0.shrink(1);
            } else {
                $$1.level().playSound(null, $$1.blockPosition(), ModSounds.SIGN_HIT_EVENT, SoundSource.PLAYERS,
                        1F, 1);
                ct.putInt("damaged", ctd);
            }
        }
        return true;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
}
