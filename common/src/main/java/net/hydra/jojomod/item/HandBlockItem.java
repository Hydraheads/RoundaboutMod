package net.hydra.jojomod.item;

import com.mojang.authlib.GameProfile;

import net.hydra.jojomod.block.handBlock.HandBlockEntity;
import net.minecraft.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;


import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class HandBlockItem extends BlockItem {
    public HandBlockItem(Block $$1, Properties $$2) {
        super($$1, $$2);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level,
                                                 @Nullable Player player, ItemStack stack, BlockState state) {

        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof HandBlockEntity hand) {
            hand.setStoredStack(stack);
        }

        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    public Component getName(ItemStack p_42977_) {
        if (p_42977_.is(ModItems.HAND) && p_42977_.hasTag()) {
            String s = null;
            CompoundTag compoundtag = p_42977_.getTag();
            if (compoundtag.contains("HandOwner", 8)) {
                s = compoundtag.getString("HandOwner");
            } else if (compoundtag.contains("HandOwner", 10)) {
                CompoundTag compoundtag1 = compoundtag.getCompound("HandOwner");
                if (compoundtag1.contains("Name", 8)) {
                    s = compoundtag1.getString("Name");
                }
            }

            if (s != null) {
                return Component.translatable(this.getDescriptionId() + ".named", new Object[]{s});
            }
        }

        return super.getName(p_42977_);
    }

    public void verifyTagAfterLoad(CompoundTag p_151179_) {
        super.verifyTagAfterLoad(p_151179_);
        if (p_151179_.contains("HandOwner", 8) && !Util.isBlank(p_151179_.getString("HandOwner"))) {
            GameProfile gameprofile = new GameProfile((UUID)null, p_151179_.getString("HandOwner"));
            SkullBlockEntity.updateGameprofile(gameprofile, (p_151177_) -> {
                p_151179_.put("HandOwner", NbtUtils.writeGameProfile(new CompoundTag(), p_151177_));
            });
        }
    }

}
