package net.hydra.jojomod.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.hydra.jojomod.client.models.HandRenderer;
import net.hydra.jojomod.client.models.substand.renderers.CloneRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SkinUtil {

    static final Map<UUID, SkinData> skins = new ConcurrentHashMap<>();
    static final Set<UUID> requestedSkins = ConcurrentHashMap.newKeySet();

    public record SkinData(ResourceLocation texture, boolean slim) {
    }

    static public SkinData getSkin(GameProfile profile) {

        if (profile == null) return new SkinData(DefaultPlayerSkin.getDefaultSkin(), false);
        UUID id = profile.getId();
        SkinData current = skins.computeIfAbsent(id, ignored -> new SkinData(
                DefaultPlayerSkin.getDefaultSkin(id), "slim".equals(DefaultPlayerSkin.getSkinModelName(id))));
        if (requestedSkins.add(id)) {
            Minecraft.getInstance().getSkinManager().registerSkins(profile, (type, location, texture) -> {
                if (type == MinecraftProfileTexture.Type.SKIN) {
                    skins.put(id, new SkinData(location, "slim".equals(texture.getMetadata("model"))));
                }
            }, false);
        }
        return current;
    }


}
