package net.hydra.jojomod.item;

import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FogBlockItem extends BlockItem {
    public final Block mimicking;
    public FogBlockItem(Block $$0, Properties $$1, Block mimicking) {
        super($$0, $$1);
        this.mimicking = mimicking;
    }
    @Override
    public Component getName(ItemStack $$0) {
        if (mimicking != null) {
            return Component.translatable("block.roundabout.fog_block.title").append(" ").
                    append(Component.translatable(mimicking.asItem().getDescriptionId(mimicking.asItem().getDefaultInstance())));
        } else {
            return super.getName($$0);
        }
    }

    @Override
    public void inventoryTick(ItemStack $$0, Level $$1, Entity $$2, int $$3, boolean $$4) {
        if ($$2 instanceof Player PE){
            if (!(((StandUser)PE).roundabout$getStandPowers() instanceof PowersJustice) && !PE.isCreative()){
                $$0.setCount(0);
            }
        }
    }
}
