package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.ISmithingTemplateItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SmithingTemplateItem.class)
public class ZSmithingTemplateItem implements ISmithingTemplateItem {


    @Shadow
    @Final
    private static ResourceLocation EMPTY_SLOT_SWORD;
    @Shadow
    @Final
    private static ResourceLocation EMPTY_SLOT_INGOT;

    @Override
    @Unique
    public ResourceLocation roundabout$sword_slot(){
        return EMPTY_SLOT_SWORD;
    }

    @Override
    @Unique
    public List<ResourceLocation> roundabout$createSwordIconList() {
        return List.of(EMPTY_SLOT_SWORD);
    }
    @Override
    @Unique
    public List<ResourceLocation> roundabout$creatMaterialIconList() {
        return List.of(EMPTY_SLOT_INGOT);
    }
}
