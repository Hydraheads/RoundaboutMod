package net.hydra.jojomod.util.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.Networking;
import net.hydra.jojomod.util.config.annotation.BooleanOption;
import net.hydra.jojomod.util.config.annotation.FloatOption;
import net.hydra.jojomod.util.config.annotation.IntOption;
import net.hydra.jojomod.util.config.annotation.NestedOption;
import net.hydra.jojomod.util.option.ConfigOptionReference;
import net.hydra.jojomod.util.option.Reflection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.zetalasis.hjson.JsonValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ConfigManager {
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create();
    private static Path clientConfigPath;
    private static Path localConfigPath;
    private static Path serverConfigPath;
    private static Path advancedConfigPath;

    private static boolean loaded = false;

    public static Config getConfig() {
        if (Networking.isDedicated()) return Config.getServerInstance();
        return Config.getLocalInstance();
    }
    public static AdvancedConfig getAdvancedConfig() {
        return AdvancedConfig.getInstance();
    }
    public static ClientConfig getClientConfig() {
        return ClientConfig.getLocalInstance();
    }

    public static void loadConfigs(Path client,  Path advanced,
                                   //Path server
                                   Path actualClient) {
        clientConfigPath = actualClient;
        localConfigPath = client;
        serverConfigPath = client;
        advancedConfigPath = advanced;
        loadClientConfig();
        loadLocalConfig();
        loadServerConfig();
        loadAdvancedConfig();
        loaded = true;
    }

    public static void loadStandArrowPool()
    {
        if (getAdvancedConfig().standArrowPoolv2 != null)
        {
            ModItems.STAND_ARROW_POOL.clear();

            for (String disc : getAdvancedConfig().standArrowPoolv2)
            {
                String[] split = disc.split(":");

                if (split.length != 2)
                    continue;

                ResourceLocation identifier = new ResourceLocation(split[0], split[1]);

                Item i = BuiltInRegistries.ITEM.get(identifier);

                if (i.getClass() != StandDiscItem.class)
                    continue;

                ModItems.STAND_ARROW_POOL.add((StandDiscItem) i);
            }
        }
        if (getAdvancedConfig().naturalStandUserMobPoolv4 != null)
        {
            ModItems.STAND_ARROW_POOL_FOR_MOBS.clear();

            for (String disc : getAdvancedConfig().naturalStandUserMobPoolv4)
            {
                String[] split = disc.split(":");

                if (split.length != 2)
                    continue;

                ResourceLocation identifier = new ResourceLocation(split[0], split[1]);

                Item i = BuiltInRegistries.ITEM.get(identifier);

                if (i.getClass() != StandDiscItem.class)
                    continue;

                ModItems.STAND_ARROW_POOL_FOR_MOBS.add((StandDiscItem) i);
            }
        }
        if (getAdvancedConfig().humanoidOnlyStandUserMobPoolv2 != null)
        {
            ModItems.STAND_ARROW_POOL_FOR_HUMANOID_MOBS.clear();

            if (ModItems.STAND_ARROW_POOL_FOR_MOBS != null && !ModItems.STAND_ARROW_POOL_FOR_MOBS.isEmpty()){
                ModItems.STAND_ARROW_POOL_FOR_HUMANOID_MOBS.addAll(ModItems.STAND_ARROW_POOL_FOR_MOBS);
            }

            for (String disc : getAdvancedConfig().humanoidOnlyStandUserMobPoolv2)
            {
                String[] split = disc.split(":");

                if (split.length != 2)
                    continue;

                ResourceLocation identifier = new ResourceLocation(split[0], split[1]);

                Item i = BuiltInRegistries.ITEM.get(identifier);

                if (i.getClass() != StandDiscItem.class)
                    continue;

                ModItems.STAND_ARROW_POOL_FOR_HUMANOID_MOBS.add((StandDiscItem) i);
            }
        }

        if (getAdvancedConfig().standArrowSecondaryPoolv5 != null)
        {
            ModItems.STAND_ARROW_SECONDARY_STAND_POOL.clear();

            for (String disc : getAdvancedConfig().standArrowSecondaryPoolv5)
            {
                String[] split = disc.split(":");

                if (split.length != 2)
                    continue;

                ResourceLocation identifier = new ResourceLocation(split[0], split[1]);

                Item i = BuiltInRegistries.ITEM.get(identifier);

                if (i.getClass() != StandDiscItem.class)
                    continue;

                ModItems.STAND_ARROW_SECONDARY_STAND_POOL.add((StandDiscItem) i);
            }
        }
    }

    public static void saveServerConfig() {
        save(Config.getServerInstance(), serverConfigPath);
    }
    private static void loadClientConfig() {
        ClientConfig config = loadClient();
        validateFields(config);
        ClientConfig.updateLocal(config);
        saveClientConfig();
        Roundabout.LOGGER.info("Loaded local config");
    }
    private static void loadLocalConfig() {
        Config config = loadLocal();
        validateFields(config);
        Config.updateLocal(config);
        saveLocalConfig();
        Roundabout.LOGGER.info("Loaded local config");
    }

    private static void loadServerConfig() {
        Config config = loadServer();
        validateFields(config);
        Config.updateServer(config);
        Roundabout.LOGGER.info("Loaded server config");
    }
    private static void loadAdvancedConfig() {
        AdvancedConfig config = loadAdvanced();
        validateFields(config);
        AdvancedConfig.updateServer(config);
        saveAdvacedConfig();
        Roundabout.LOGGER.info("Loaded advanced config");
    }

    public static void validateFields(Object instance) {
        validateFloatFields(instance);
        validateIntFields(instance);
        validateBooleanFields(instance);
        validateNestedFields(instance);
    }

    private static void validateFloatFields(Object instance) {
        Reflection.forEachFieldByAnnotation(instance, FloatOption.class, (field, annotation) -> {
            ConfigOptionReference reference = ConfigOptionReference.of(instance, field);
            setIfNull(reference, annotation.value());
            if (reference.floatValue() < annotation.min()) reference.floatValue(annotation.min());
            if (reference.floatValue() > annotation.max()) reference.floatValue(annotation.max());
        });
    }

    private static void validateIntFields(Object instance) {
        Reflection.forEachFieldByAnnotation(instance, IntOption.class, (field, annotation) -> {
            ConfigOptionReference reference = ConfigOptionReference.of(instance, field);
            setIfNull(reference, annotation.value());
            if (reference.intValue() < annotation.min()) reference.intValue(annotation.min());
            if (reference.intValue() > annotation.max()) reference.intValue(annotation.max());
        });
    }

    private static void validateBooleanFields(Object instance) {
        Reflection.forEachFieldByAnnotation(instance, BooleanOption.class, (field, annotation) -> {
            ConfigOptionReference reference = ConfigOptionReference.of(instance, field);
            setIfNull(reference, annotation.value());
        });
    }

    private static void validateNestedFields(Object instance) {
        Reflection.forEachFieldByAnnotation(instance, NestedOption.class, (field, annotation) -> {
            Object nestedOption = Reflection.accessField(field, instance, Object.class);
            if (nestedOption == null) {
                nestedOption = Reflection.newInstance(field.getType());
                Reflection.setField(field, instance, nestedOption);
            }
            validateFields(nestedOption);
        });
    }

    private static void setIfNull(ConfigOptionReference reference, Object value) {
        if (reference.isValueNull()) {
            reference.setConfigValue(value);
        }
    }

    public static String serializeConfig() {
        return GSON.toJson(Config.getLocalInstance());
    }

    public static void deserializeConfig(String serialized) {
        Config.updateServer(GSON.fromJson(serialized, Config.class));
    }

    private static ClientConfig DEFAULT_CLIENT_CONFIG;
    private static Config DEFAULT_LOCAL_CONFIG;
    private static Config DEFAULT_SERVER_CONFIG;
    private static AdvancedConfig DEFAULT_ADVANCED_CONFIG;

    private static ClientConfig loadClient() {
        ClientConfig loaded = loadClient(new ClientConfig(), clientConfigPath);
        validateFields(loaded);
        ClientConfig.updateLocal(loaded);

        DEFAULT_CLIENT_CONFIG = loaded.clone();

        saveClientConfig();
        return loaded;
    }

    private static AdvancedConfig loadAdvanced() {
        AdvancedConfig loaded = loadAdvanced(new AdvancedConfig(), advancedConfigPath);
        validateFields(loaded);
        AdvancedConfig.updateServer(loaded);

        DEFAULT_ADVANCED_CONFIG = loaded.clone();

        saveAdvacedConfig();
        return loaded;
    }
    private static Config loadLocal() {
        Config loaded = load(new Config(), localConfigPath);
        validateFields(loaded);
        Config.updateLocal(loaded);
        saveLocalConfig();
        return loaded;
    }

    private static Config loadServer() {
        Config loaded = load(new Config(), serverConfigPath);
        validateFields(loaded);
        Config.updateServer(loaded);

        DEFAULT_SERVER_CONFIG = loaded.clone();

        return loaded;
    }

    private static Config load(Config defaultConfig, Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                return defaultConfig;
            }
            try {
                String fileContent = String.join(System.lineSeparator(), Files.readAllLines(path));
                JsonValue hjson = JsonValue.readHjson(fileContent);

                return GSON.fromJson(hjson.toString(), Config.class);
            } catch (Exception e) {
                Roundabout.LOGGER.error("Failed to parse defaultConfig file, using default config");
            }
        } catch (IOException e) {
            Roundabout.LOGGER.error("Failed to load config", e);
        }
        return defaultConfig;
    }

    private static AdvancedConfig loadAdvanced(AdvancedConfig defaultConfig, Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                return defaultConfig;
            }
            try {
                String fileContent = String.join(System.lineSeparator(), Files.readAllLines(path));
                JsonValue hjson = JsonValue.readHjson(fileContent);

                return GSON.fromJson(hjson.toString(), AdvancedConfig.class);
            } catch (Exception e) {
                Roundabout.LOGGER.error("Failed to parse defaultConfig file, using default config");
            }
        } catch (IOException e) {
            Roundabout.LOGGER.error("Failed to load config", e);
        }
        return defaultConfig;
    }

    private static ClientConfig loadClient(ClientConfig defaultConfig, Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                return defaultConfig;
            }
            try {
                String fileContent = String.join(System.lineSeparator(), Files.readAllLines(path));
                JsonValue hjson = JsonValue.readHjson(fileContent);

                return GSON.fromJson(hjson.toString(), ClientConfig.class);
            } catch (Exception e) {
                Roundabout.LOGGER.error("Failed to parse client config file, using default config");
            }
        } catch (IOException e) {
            Roundabout.LOGGER.error("Failed to load config", e);
        }
        return defaultConfig;
    }

    public static boolean loaded() {
        return loaded;
    }

    public static void saveClientConfig() {
        saveClient(ClientConfig.getLocalInstance(), clientConfigPath);
    }
    public static void saveAdvacedConfig() {
        saveAdvanced(AdvancedConfig.getInstance(), advancedConfigPath);
    }
    public static void saveLocalConfig() {
        validateFields(Config.getLocalInstance());
        save(Config.getLocalInstance(), localConfigPath);
    }

    private static void save(Config config, Path path) {
        try {
            String parsed = String.join(System.lineSeparator(), ConfigParser.parse(config));
            Files.write(path, parsed.getBytes());
        } catch (IOException e) {
            Roundabout.LOGGER.error("Failed to save config", e);
        }
    }
    private static void saveClient(ClientConfig config, Path path) {
        try {
            String parsed = String.join(System.lineSeparator(), ConfigParser.parse(config));
            Files.write(path, parsed.getBytes());
        } catch (IOException e) {
            Roundabout.LOGGER.error("Failed to save config", e);
        }
    }
    private static void saveAdvanced(AdvancedConfig config, Path path) {
        try {
            String parsed = String.join(System.lineSeparator(), ConfigParser.parse(config));
            Files.write(path, parsed.getBytes());
        } catch (IOException e) {
            Roundabout.LOGGER.error("Failed to save config", e);
        }
    }

    public static void resetLocal()
    {
        Config.updateLocal(Config.getDefaultInstance().clone());
    }

    public static void resetClient()
    {
        ClientConfig.updateLocal(ClientConfig.getDefaultInstance().clone());
    }

    public static void resetServer()
    {
    }
}
