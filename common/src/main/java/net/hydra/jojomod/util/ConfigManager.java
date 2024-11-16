package net.hydra.jojomod.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.util.annotation.BooleanOption;
import net.hydra.jojomod.util.annotation.FloatOption;
import net.hydra.jojomod.util.annotation.IntOption;
import net.hydra.jojomod.util.annotation.NestedOption;
import net.hydra.jojomod.util.option.ConfigOptionReference;
import net.hydra.jojomod.util.option.Reflection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create();
    private static Path localConfigPath;
    private static Path serverConfigPath;

    private static boolean loaded = false;

    public static Config getConfig() {
        if (Networking.isDedicated()) return Config.getServerInstance();
        return Config.getLocalInstance();
    }

    public static void loadConfigs(Path client, Path server) {
        localConfigPath = client;
        serverConfigPath = server;
        loadLocalConfig();
        loadServerConfig();
        loaded = true;
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
        saveServerConfig();
        Roundabout.LOGGER.info("Loaded server config");
    }

    private static void validateFields(Object instance) {
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
            ConfigOptionReference reference = ConfigOptionReference.of(instance, field);
            setIfNull(reference, Reflection.newInstance(field.getType()));

            Object nestedOption = Reflection.accessField(field, instance, Object.class);
            validateFields(nestedOption);
        });
    }

    private static void setIfNull(ConfigOptionReference reference, Object value) {
        if (reference.isValueNull()) {
            reference.setConfigValue(value);
        }
    }

    public static String serializeConfig() {
        return GSON.toJson(Config.getServerInstance());
    }

    public static void deserializeConfig(String serialized) {
        Config.updateServer(GSON.fromJson(serialized, Config.class));
        saveServerConfig();
    }

    private static Config loadLocal() {
        return load(Config.getLocalInstance(), localConfigPath);
    }

    private static Config loadServer() {
        return load(Config.getServerInstance(), serverConfigPath);
    }

    private static Config load(Config defaultConfig, Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                return defaultConfig;
            }
            try {
                return GSON.fromJson(Files.newBufferedReader(path), Config.class);
            } catch (JsonSyntaxException e) {
                Roundabout.LOGGER.error("Failed to parse defaultConfig file, using default config");
            }
        } catch (IOException e) {
            Roundabout.LOGGER.error("Failed to load config", e);
        }
        return defaultConfig;
    }

    public static boolean loaded() {
        return loaded;
    }

    public static void saveLocalConfig() {
        save(Config.getLocalInstance(), localConfigPath);
    }

    public static void saveServerConfig() {
        save(Config.getServerInstance(), serverConfigPath);
    }

    private static void save(Config config, Path path) {
        try {
            Files.write(path, GSON.toJson(config).getBytes());
        } catch (IOException e) {
            Roundabout.LOGGER.error("Failed to save config", e);
        }
    }
}
