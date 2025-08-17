package net.hydra.jojomod.datagen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GennedItemModel extends ItemModelBuilder {
    private final JsonObject object;
    public GennedItemModel(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper, JsonObject jsonObject) {
        super(outputLocation, existingFileHelper);
        object = jsonObject;
    }
    @Override
    public JsonObject toJson() {
        return object;
    }
}
