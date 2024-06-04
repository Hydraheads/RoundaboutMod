package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeBlockEntityResumeTSPacket {

    private final Vec3i vec3i;
    public ForgeBlockEntityResumeTSPacket(Vec3i vec3i){
        this.vec3i = vec3i;
    }
    public ForgeBlockEntityResumeTSPacket(FriendlyByteBuf buf){
        this.vec3i = new Vec3i(buf.readInt(),buf.readInt(),buf.readInt());
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(vec3i.getX());
        buf.writeInt(vec3i.getY());
        buf.writeInt(vec3i.getZ());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                BlockEntity openedBlock = player.level().getBlockEntity(new BlockPos(vec3i.getX(),vec3i.getY(),vec3i.getZ()) );
                if (openedBlock != null){
                    ((TimeStop) player.level()).processTSBlockEntityPacket(openedBlock);
                }
            }
        });
        return true;
    }
}
