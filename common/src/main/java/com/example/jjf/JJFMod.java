package com.example.jjf;

import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;

public final class JJFMod {

    public static final String MODID = "jjf";

    private static Path configDirectory =
            Path.of("config")
                    .toAbsolutePath()
                    .normalize();

    private JJFMod() {
    }

    public static ResourceLocation id(
            String path
    ) {
        return new ResourceLocation(
                MODID,
                path
        );
    }

    public static void setConfigDirectory(
            Path path
    ) {
        if (path == null) {
            return;
        }

        configDirectory =
                path.toAbsolutePath()
                        .normalize();
    }

    public static Path configDirectory() {
        return configDirectory;
    }
}
