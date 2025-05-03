package net.hydra.jojomod;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class JojoModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		//FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		//pack.addProvider(ModLootTableProvider::new);
	}
}
