package net.hydra.jojomod.item;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.block.FancyLighterBlock;
import net.hydra.jojomod.block.FancyLighterBlockEntity;
import net.hydra.jojomod.block.handBlock.AbstractHandBlock;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.UUID;

public class HandBlockItem extends BlockItem {
    public HandBlockItem(Block $$1, Properties $$2) {
        super($$1, $$2);
    }


    public Component getName(ItemStack p_42977_) {
        if (p_42977_.is(Items.PLAYER_HEAD) && p_42977_.hasTag()) {
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
                return Component.translatable(this.getDescriptionId() + ".named", s);
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
