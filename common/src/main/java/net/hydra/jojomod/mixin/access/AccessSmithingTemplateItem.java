package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.ISmithingTemplateItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(SmithingTemplateItem.class)
public class AccessSmithingTemplateItem implements ISmithingTemplateItem {

    /**There is no reason for these to be private or protected, we should be able to tap into them.*/
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
    public ResourceLocation roundabout$axe_slot(){
        return EMPTY_SLOT_AXE;
    }

    @Override
    @Unique
    public List<ResourceLocation> roundabout$createAxeIconList() {
        return List.of(EMPTY_SLOT_AXE);
    }
    @Override
    @Unique
    public List<ResourceLocation> roundabout$creatMaterialIconList() {
        return List.of(EMPTY_SLOT_INGOT);
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow
    @Final
    private static ResourceLocation EMPTY_SLOT_SWORD;
    @Shadow
    @Final
    private static ResourceLocation EMPTY_SLOT_AXE;
    @Shadow
    @Final
    private static ResourceLocation EMPTY_SLOT_INGOT;
}
