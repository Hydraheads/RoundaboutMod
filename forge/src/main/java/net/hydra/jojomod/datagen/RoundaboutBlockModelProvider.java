package net.hydra.jojomod.datagen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.block.FogCoatBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.zetalasis.hjson.JsonValue;
import net.zetalasis.hjson.Stringify;

import java.io.InputStreamReader;
import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.Map;

import static net.hydra.jojomod.DataGenerators.*;
import static net.hydra.jojomod.registry.ForgeBlocks.*;

public class RoundaboutBlockModelProvider extends BlockStateProvider {
    public RoundaboutBlockModelProvider(PackOutput packOutput, String modId, ExistingFileHelper existingFileHelper) {
        super(packOutput,modId,existingFileHelper);
    }


    @Override
    protected void registerStatesAndModels() {
        for(Map.Entry<ResourceLocation,Block> me : fogBlocks.entrySet()) {
			Block b = me.getValue();
            ResourceLocation i = me.getKey();
			Roundabout.LOGGER.info(me.getKey().toString());
            String OGPath = i.getPath().replace("fog_","").replace("_coating","");
			Roundabout.LOGGER.info("Generating blockstate for " + i.getPath());
			JsonObject main = new JsonObject();
			JsonObject variants = new JsonObject();
			JsonObject falseVariant = new JsonObject();
			falseVariant.addProperty("model",Roundabout.MOD_ID+ ":block/fog_block_invisible");
			InputStreamReader assetResource = getJsonResourceStream("blockstates/" + i.getPath().replace("fog_", "")).get();
			variants.add("in_fog=false",falseVariant);
			try {
				net.zetalasis.hjson.JsonObject OGVariants = JsonValue.readJSON(assetResource).asObject().get("variants").asObject();
				for (net.zetalasis.hjson.JsonObject.Member member : OGVariants) {
					if (member.getName().isEmpty()) {
						variants.add("in_fog=true", JsonParser.parseString(member.getValue().toString(Stringify.PLAIN)));
						continue;
					}
					variants.add("in_fog=true," + member.getName(), JsonParser.parseString(member.getValue().toString(Stringify.PLAIN)));
				}
				main.add("variants", variants);
				registeredBlocks.put(b, new GennedBlockState(main));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			Roundabout.LOGGER.info("Generating item model for " + i.getPath());
			main = new JsonObject();
			Roundabout.LOGGER.info("blockstates/" + i.getPath().replace("fog_", ""));
			main.addProperty("parent",getModelFromBStateStream(getJsonResourceStream("blockstates/" + i.getPath().replace("fog_", "")).get()).get());
			itemModels().generatedModels.put(i,new GennedItemModel(new ResourceLocation(Roundabout.MOD_ID,"item/"+i.getPath()),itemModels().existingFileHelper,main));





            
        }
		for(Map.Entry<ResourceLocation,Block> me : fogCoatingBlocks.entrySet()){
			Block b = me.getValue();
			ResourceLocation i = me.getKey();
			Roundabout.LOGGER.info(me.getKey().toString());
			String OGPath = i.getPath().replace("fog_","").replace("_coating","");
			InputStreamReader assetResource = getJsonResourceStream("blockstates/" + i.getPath().replace("fog_", "").replace("_coating","")).get();
			String blockTex = getTexturefromModelName(getModelFromBStateStream(assetResource).get()).get();
			//Generate the model
			Roundabout.LOGGER.info("Generating model for " + i.getPath());
			JsonObject main = new JsonObject();
			JsonObject textures = new JsonObject();
			main.addProperty("parent","roundabout:block/fog_coating_source");
			if(!blockTex.isEmpty()) {
				textures.addProperty("texture", blockTex);
				main.add("textures", textures);
			} else{
				Roundabout.LOGGER.info("Couldn't find texture for "+OGPath+". Guessing texture name");
				textures.addProperty("texture","minecraft:block/"+ OGPath);
				main.add("textures", textures);
			}
			models().generatedModels.put(i,new GennedBlockModel(new ResourceLocation(Roundabout.MOD_ID,"block/"+i.getPath()),models().existingFileHelper,main));

			Roundabout.LOGGER.info("Generating blockstate for " + i.getPath());
			String name = i.getPath();
			registeredBlocks.put(b,new GennedBlockState(JsonParser.parseString(String.format("""
										{
										  "variants": {
										    "in_fog=true,facing=east": {
										      "model": "roundabout:block/%s",
										      "x": 270,
										      "y": 270
										    },
										    "in_fog=true,facing=north": {
										      "model": "roundabout:block/%s",
										      "x": 270,
										      "y": 180
										    },
										    "in_fog=true,facing=south": {
										      "model": "roundabout:block/%s",
										      "x": 270
										    },
										    "in_fog=true,facing=west": {
										      "model": "roundabout:block/%s",
										      "x": 270,
										      "y": 90
										    },
										    "in_fog=true,facing=up": {
										      "model": "roundabout:block/%s"
										    },
										    "in_fog=true,facing=down": {
										      "model": "roundabout:block/%s",
										      "x": 180
										    },
										    "in_fog=false": {
										      "model": "roundabout:block/fog_block_invisible"
										    }
										  }
										}
										
										""",name,name,name,name,name,name)).getAsJsonObject()));


			Roundabout.LOGGER.info("Generating item model for " + i.getPath());
			itemModels().generatedModels.put(i,new GennedItemModel(new ResourceLocation(Roundabout.MOD_ID,"item/"+i.getPath()),itemModels().existingFileHelper,JsonParser.parseString("""
						{
							"parent": "roundabout:item/fog_coating",
							"textures": {
						  "layer0":\s""" + "\"" + blockTex + "\"" + """
							}
						}
						""").getAsJsonObject()));
			continue;
		}


    }
}
