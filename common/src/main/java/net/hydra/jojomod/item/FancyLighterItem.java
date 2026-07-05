package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FancyLighterItem extends BlockItem {

    public FancyLighterItem(Block $$0, Properties $$1) {
        super($$0, $$1);
    }

    @Nullable
    public LivingEntity lighterOwner = null;

    public void setLighterOwner(LivingEntity liv){lighterOwner = liv;}

    public boolean isLit = true;
    public void setLit(boolean lit){isLit = lit;}
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        BlockPos abovePos = blockPos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (!aboveState.isAir()) return InteractionResult.FAIL;

        if (level.isClientSide) {
            System.out.println(lighterOwner);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if(!$$1.isInWaterOrRain()) {
            if (!isLit) {
                setLit(true);
            } else {
                setLit(false);
            }
        }else {
            setLit(false);
            return InteractionResultHolder.fail($$3);
        }
        return InteractionResultHolder.fail($$3);
    }

    public void tick(){
        System.out.println(isLit);
    }

    public float getIsLitOrNot(Level level) {
        if (level != null) {
            if (!isLit) {
                return 0.2f;
            }
        }
        return 0.0f;
    }
}
