package net.hydra.jojomod;

import net.hydra.jojomod.datagen.RoundaboutBlockModelProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.zetalasis.hjson.JsonObject;
import net.zetalasis.hjson.JsonValue;

import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    public static Optional<InputStreamReader> getJsonResourceStream(String resource){

        try {
            return Optional.of(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResource("assets/minecraft/"+resource+".json").openStream()));
        } catch (Exception e) {
            Roundabout.LOGGER.info("Failed to get resource stream: " + resource);
            return Optional.empty();
        }

    }


    public static Optional<String> getModelFromBStateStream(InputStreamReader is){
        try {
            net.zetalasis.hjson.JsonObject obj =  JsonValue.readJSON(is).asObject().get("variants").asObject();
            for(JsonObject.Member mb : obj ){
                try{
                    String model = "";
                    try {
                        model = mb.getValue().asObject().get("model").asString();
                        return Optional.of(model);
                    } catch (Exception e) {
                        model = mb.getValue().asArray().get(0).asObject().get("model").asString();
                        return Optional.of(model);
                    }
                } catch (Exception e){
                    Roundabout.LOGGER.info("Fail1");
                    continue;
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
           // return Optional.empty();
        }
        Roundabout.LOGGER.info("?");
        return Optional.empty();
    }
    public static Optional<String> getTexturefromModelName(String mname){
        try{
            InputStreamReader assetResource = getJsonResourceStream("models/block/"+mname.replace("minecraft:block/","")).get();
            net.zetalasis.hjson.JsonObject obj =  JsonValue.readJSON(assetResource).asObject().get("textures").asObject();
            for(JsonObject.Member mb : obj ){
                try{
                    String model = "";
                    try {
                        try {
                            model = mb.getValue().asObject().get("texture").asString();
                            return Optional.of(model);
                        } catch (Exception e) {
                            model = mb.getValue().asArray().get(0).asObject().get("texture").asString();
                            return Optional.of(model);
                        }
                    } catch (Exception e) {
                        model = mb.getValue().asString();
                        return Optional.of(model);
                    }
                } catch (Exception e){
                    continue;
                }
            }
        } catch (Exception e) {
            Roundabout.LOGGER.info("Fuck");
            return Optional.empty();
        }
        Roundabout.LOGGER.info("Fuck");
        return Optional.empty();
    }
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeClient(), new RoundaboutBlockModelProvider(packOutput,Roundabout.MOD_ID,existingFileHelper));

    }
}
