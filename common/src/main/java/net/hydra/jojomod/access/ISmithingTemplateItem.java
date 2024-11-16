package net.hydra.jojomod.access;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface ISmithingTemplateItem {
    ResourceLocation roundabout$axe_slot();
    ResourceLocation roundabout$sword_slot();
    List<ResourceLocation> roundabout$createSwordIconList();
    List<ResourceLocation> roundabout$creatMaterialIconList();
    List<ResourceLocation> roundabout$createAxeIconList();
}
