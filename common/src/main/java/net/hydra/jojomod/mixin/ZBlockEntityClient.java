package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IBlockEntityClientAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class ZBlockEntityClient implements IBlockEntityClientAccess {
    /**Store extra data on block entities*/

    private float roundaboutPrevTick;

    @Override
    public float getPreTSTick() {
        return this.roundaboutPrevTick;
    }

    @Override
    public void setPreTSTick() {
        Minecraft mc = Minecraft.getInstance();
        roundaboutPrevTick = mc.getFrameTime();
    }
    @Override
    public void resetPreTSTick() {
        roundaboutPrevTick = 0;
    }

}
