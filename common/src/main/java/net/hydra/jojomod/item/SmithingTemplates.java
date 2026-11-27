package net.hydra.jojomod.item;

import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.ISmithingTemplateItem;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class SmithingTemplates {
    public static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
    public static final Component LUCK_UPGRADE_APPLIES_TO = Component.translatable(
                    String.valueOf(new ResourceLocation(SBA.MOD_ID, "item.sba.smithing_template.luck_upgrade.applies_to"))
            )
            .withStyle(DESCRIPTION_FORMAT);
    public static final Component EXECUTION_UPGRADE_APPLIES_TO = Component.translatable(
                    String.valueOf(new ResourceLocation(SBA.MOD_ID, "item.sba.smithing_template.execution_upgrade.applies_to"))
            )
            .withStyle(DESCRIPTION_FORMAT);
    public static final Component LUCK_UPGRADE_INGREDIENTS = Component.translatable(
                    String.valueOf(new ResourceLocation(SBA.MOD_ID, "item.sba.smithing_template.luck_upgrade.ingredients"))
            )
            .withStyle(DESCRIPTION_FORMAT);
    public static final Component EXECUTION_UPGRADE_INGREDIENTS = Component.translatable(
                    String.valueOf(new ResourceLocation(SBA.MOD_ID, "item.sba.smithing_template.execution_upgrade.ingredients"))
            )
            .withStyle(DESCRIPTION_FORMAT);
    public static final Component LUCK_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(
            String.valueOf(new ResourceLocation(SBA.MOD_ID, "item.sba.smithing_template.luck_upgrade.base_slot_description"))
    );
    public static final Component EXECUTION_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(
            String.valueOf(new ResourceLocation(SBA.MOD_ID, "item.sba.smithing_template.execution_upgrade.base_slot_description"))
    );
    public static final Component LUCK_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(
            String.valueOf(new ResourceLocation(SBA.MOD_ID, "item.sba.smithing_template.luck_upgrade.additions_slot_description"))
    );
    public static final Component EXECUTION_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(
            String.valueOf(new ResourceLocation(SBA.MOD_ID, "item.sba.smithing_template.execution_upgrade.additions_slot_description"))
    );
    public static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;

    public static final Component LUCK_UPGRADE = Component.translatable(
            String.valueOf(new ResourceLocation(SBA.MOD_ID, "upgrade.sba.luck_upgrade"))).withStyle(TITLE_FORMAT);


    public static final Component EXECUTION_UPGRADE = Component.translatable(
            String.valueOf(new ResourceLocation(SBA.MOD_ID, "upgrade.sba.execution_upgrade"))).withStyle(TITLE_FORMAT);


    public static List<ResourceLocation> createLuckUpgradeIconList() {
        return List.of(((ISmithingTemplateItem) Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).sba$sword_slot());
    }
    public static List<ResourceLocation> createLuckMatIconList() {
        return ((ISmithingTemplateItem) Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).sba$creatMaterialIconList();
    }
    public static List<ResourceLocation> createExecutionUpgradeIconList() {
        return List.of(((ISmithingTemplateItem) Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).sba$axe_slot());
    }
    public static List<ResourceLocation> createExecutionMatIconList() {
        return ((ISmithingTemplateItem) Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).sba$creatMaterialIconList();
    }
}
