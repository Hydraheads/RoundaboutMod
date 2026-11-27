package net.hydra.jojomod.access;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface ISmithingTemplateItem {
    ResourceLocation sba$axe_slot();
    ResourceLocation sba$sword_slot();
    List<ResourceLocation> sba$createSwordIconList();
    List<ResourceLocation> sba$creatMaterialIconList();
    List<ResourceLocation> sba$createAxeIconList();
}
