package net.hydra.jojomod.networking;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.hydra.jojomod.access.IPacketAccess;
import net.hydra.jojomod.networking.c2s.*;
import net.hydra.jojomod.networking.s2c.*;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.Networking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ForgePackets implements IPacketAccess {


    @Override
    public void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i) {
        ForgePacketHandler.sendToClient(new ForgeBlockEntityResumeTSPacket(vec3i), sp);
    }


    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create();



    @Override
    @SuppressWarnings("deprecation") // markWorldsDirty
    public void sendNewDynamicWorld(ServerPlayer sp, String name, ServerLevel level, @Nullable ServerPlayer player) {
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(level));
        level.getServer().markWorldsDirty();

        int id = -1;
        if (player != null)
            id = player.getId();

        ForgePacketHandler.sendToClient(new ForgeDynamicWorldSync(name), sp);
    }


    @Override
    public void ejectPRunning(ServerPlayer sp) {
        ForgePacketHandler.sendToClient(new ForgeEjectPRunning(), sp);
    }

}
