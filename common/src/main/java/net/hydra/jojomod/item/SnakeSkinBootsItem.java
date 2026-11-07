package net.hydra.jojomod.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum SnakeSkinBootsItem implements ArmorMaterial {
    SNAKE_ARMOR("snake_skin_armor", 5, new int[]{1 , 2, 3, 1}, 9,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(ModItems.SNAKE_SKIN));

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    SnakeSkinBootsItem(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> 13;
            case LEGGINGS -> 13;
            case CHESTPLATE -> 13;
            case HELMET -> 13;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.protectionAmounts[type.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() { return this.enchantmentValue; }

    @Override
    public SoundEvent getEquipSound() { return this.equipSound; }

    @Override
    public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }

    @Override
    public String getName() { return "roundabout:" + this.name; }

    @Override
    public float getToughness() { return this.toughness; }

    @Override
    public float getKnockbackResistance() { return this.knockbackResistance; }
}