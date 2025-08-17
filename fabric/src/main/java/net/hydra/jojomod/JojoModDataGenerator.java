package net.hydra.jojomod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.zetalasis.hjson.JsonValue;
import net.zetalasis.hjson.Stringify;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Supplier;

public class JojoModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(RoundaboutModelProvider::new);

	}
	public static Optional<InputStreamReader> getJsonResourceStream(String resource){

		try {
			return Optional.of(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResource("assets/minecraft/"+resource+".json").openStream()));
		} catch (Exception e) {
			return Optional.empty();
		}

	}

	public static String getTextureNameFromBlockState(InputStreamReader bstateRes){
		String texture = "";
		try{
			Optional<InputStreamReader> asrs = Optional.empty();
			for(net.zetalasis.hjson.JsonObject.Member member : JsonValue.readJSON(bstateRes).asObject().get("variants").asObject()){
				String model = "";
				try {
					model = member.getValue().asObject().get("model").asString();
				} catch (Exception e) {
					model = member.getValue().asArray().get(0).asObject().get("model").asString();
				}
				try {
					asrs = getJsonResourceStream("models/block/" + model.replace("minecraft:block/", ""));
					for(net.zetalasis.hjson.JsonObject.Member mb2 : JsonValue.readJSON(asrs.get()).asObject().get("textures").asObject()){
						try {
							texture = mb2.getValue().asString();
							break;
						} catch (Exception e) {
							continue;
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

	} catch (IOException e) {
		throw new RuntimeException(e);
	}
		return texture;
	}

	public static class RoundaboutModelProvider extends FabricModelProvider {
		public RoundaboutModelProvider(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generateBlockStateModels(BlockModelGenerators bmodel) {
			for(Block b : BuiltInRegistries.BLOCK) {
				ResourceLocation i = BuiltInRegistries.BLOCK.getKey(b);
				if(!i.getNamespace().equals(Roundabout.MOD_ID) || !i.getPath().contains("fog_") || ModBlocks.dontGenState.contains(i.getPath().replace("_coating",""))){
					continue;
				}
				//For Coatings
				if(i.getPath().contains("_coating")){
					Roundabout.LOGGER.info("Generating Model for: " + i.toString());
					Optional<InputStreamReader> bstateRes = getJsonResourceStream("blockstates/"+ getOGResource(i));
					//asset resource but spelled differently
					Optional<InputStreamReader> asrs = Optional.empty();
					if(bstateRes.isPresent()){
                        try {


							for(net.zetalasis.hjson.JsonObject.Member member : JsonValue.readJSON(bstateRes.get()).asObject().get("variants").asObject()){
								try {
									String model = "";
									try {
										model = member.getValue().asObject().get("model").asString();
									} catch (Exception e) {
										model = member.getValue().asArray().get(0).asObject().get("model").asString();
									}
									asrs = getJsonResourceStream("models/block/" + model.replace("minecraft:block/", ""));
								} catch (Exception e) {
									continue;
								}
							}
							if(asrs.isEmpty()){
								//A final Attempt
								Roundabout.LOGGER.info("Couldn't find a Block Model for " + getOGResource(i) + ". Guessing the model name");
								asrs = getJsonResourceStream("models/block/" + getOGResource(i));
							}

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
						Roundabout.LOGGER.info("Couldn't find a Block Model for " + getOGResource(i) + ". Guessing the model name");
						asrs = getJsonResourceStream("models/block/" + getOGResource(i));
					}
					//And it was spelled weirdly to make this final variable
					Optional<InputStreamReader> asRes = asrs;
					Supplier<JsonElement> coatingJSON = () -> {
						JsonObject main = new JsonObject();
						JsonObject textures = new JsonObject();
						main.addProperty("parent","roundabout:block/fog_coating_source");
						if(asRes.isPresent()){
							try {
								for(net.zetalasis.hjson.JsonObject.Member member : JsonValue.readJSON(asRes.get()).asObject().get("textures").asObject()){
									textures.addProperty("texture",member.getValue().asString());
									break;
								}
								main.add("textures",textures);
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						} else{
							Roundabout.LOGGER.info("Couldn't find the texture file for " + getOGResource(i) + ". Guessing...");
							textures.addProperty("texture","minecraft:block/"+ getOGResource(i));
							main.add("textures",textures);
						}
						return main;
					};
					bmodel.modelOutput.accept(new ResourceLocation(Roundabout.MOD_ID,"block/" + i.getPath()),coatingJSON);


					Roundabout.LOGGER.info("Generating blockstate for: "+ i.toString());
					bmodel.blockStateOutput.accept(new BlockStateGenerator() {
						@Override
						public Block getBlock() {

							return b;
						}

						@Override
						public JsonElement get() {
							try {
								String name = i.getPath();
								return JsonParser.parseString(String.format("""
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
										
										""",name,name,name,name,name,name));
							} catch (Exception e){

								throw new RuntimeException(e);
							}


						}
					});


					continue;
				}
				//Non Coating
				Roundabout.LOGGER.info("Generating blockstate for: "+ i.toString());
				bmodel.blockStateOutput.accept(new BlockStateGenerator() {
					@Override
					public Block getBlock() {

						return b;
					}

					@Override
					public JsonElement get() {
						JsonObject main = new JsonObject();
						JsonObject variants = new JsonObject();
						JsonObject falseVariant = new JsonObject();
						falseVariant.addProperty("model",Roundabout.MOD_ID+ ":block/fog_block_invisible");
						try {
							variants.add("in_fog=false",falseVariant);
							net.zetalasis.hjson.JsonObject OGBlockVariants = JsonValue.readJSON(JojoModDataGenerator.getJsonResourceStream("blockstates/" + i.getPath().replace("fog_", "")).get()).asObject().get("variants").asObject();
							for(net.zetalasis.hjson.JsonObject.Member member : OGBlockVariants){
								if(member.getName().isEmpty()){
									variants.add("in_fog=true", JsonParser.parseString(member.getValue().toString(Stringify.PLAIN)));
									continue;
								}
								variants.add("in_fog=true,"+member.getName(), JsonParser.parseString(member.getValue().toString(Stringify.PLAIN)));
							}

							main.add("variants",variants);
							return main;
						} catch (Exception e){

							throw new RuntimeException(e);
						}


					}
				});



			}
		}

		private static @NotNull String getOGResource(ResourceLocation i) {
			return i.getPath().replace("fog_", "").replace("_coating", "");
		}


		@Override
		public void generateItemModels(ItemModelGenerators imodel) {
			//Generate Item Models
			for(Item i : ModBlocks.gennedFogItems){
				Roundabout.LOGGER.info("Generating item model for: "+BuiltInRegistries.ITEM.getKey(i).getPath());
				Optional<InputStreamReader> assetRes =  getJsonResourceStream("models/item/"+ getOGResource(BuiltInRegistries.ITEM.getKey(i)));
				Supplier<JsonElement> itemJson = () -> {
				if(assetRes.isPresent()){
					return JsonParser.parseReader(assetRes.get());
				}
				return JsonParser.parseString("""
						{
						  "parent":\s""" + "\"" + "minecraft:block/"+ getOGResource(BuiltInRegistries.ITEM.getKey(i)) + "\"" + """
						}
						""");

				};
				if(BuiltInRegistries.ITEM.getKey(i).getPath().contains("_coating")){
					Optional<InputStreamReader> asRs = getJsonResourceStream("blockstates/" + getOGResource(BuiltInRegistries.ITEM.getKey(i)));
					itemJson = () -> {
						String model;
						model = "\"" + "minecraft:block/"+ getOGResource(BuiltInRegistries.ITEM.getKey(i)) + "\"";
						if(asRs.isPresent()){
                            try {
								model = "\"" + getTextureNameFromBlockState(asRs.get()) + "\"";

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                        } else{
							Roundabout.LOGGER.info("Couldn't get parent texture for coating: " + BuiltInRegistries.ITEM.getKey(i).getPath() + ". Guessing.");
						}
						return JsonParser.parseString("""
						{
							"parent": "roundabout:item/fog_coating",
							"textures": {
						  "layer0":\s""" + model + """
							}
						}
						""");

					};
				}
				imodel.output.accept(new ResourceLocation(Roundabout.MOD_ID,"item/" + BuiltInRegistries.ITEM.getKey(i).getPath()),itemJson);


			}

		}
	}
}
