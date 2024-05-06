package net.hydra.jojomod;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.item.ModItemGroups;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.EventInit;
import net.hydra.jojomod.util.ModLootTableModifiers;
import net.hydra.jojomod.util.PlayerCopyHandler;
import net.hydra.jojomod.world.gen.ModWorldGeneration;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.KillCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RoundaboutMod implements ModInitializer {
	/**This logger is used to write text to the console and the log file.*/
    public static final String MOD_ID = "roundabout";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/** This code runs as soon as Minecraft is in a mod-load-ready state.
	 * However, some things (like resources) may still be uninitialized.*/
	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModLootTableModifiers.modifyLootTables();
		ModMessages.registerC2SPackets();
		ModSounds.registerSoundEvents();
		ModEntities.registerModEntities();
		ModWorldGeneration.generateWorldGen();
		RoundaboutCommands.register();
		EventInit.init();


		ServerPlayerEvents.COPY_FROM.register(new PlayerCopyHandler());
		//ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerRespawnHandler());

		LOGGER.info("Hello Fabric world!");
	}


}