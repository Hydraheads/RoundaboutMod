package com.example.jjf.launcher;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class JJFLauncherState {

    private ItemStack loadedItem =
            ItemStack.EMPTY;

    private long lastFireGameTime =
            Long.MIN_VALUE;

    public boolean hasLoadedItem() {
        return !loadedItem.isEmpty();
    }

    public ItemStack getLoadedItem() {
        return loadedItem.copy();
    }

    public int getLoadedCount() {
        return loadedItem.isEmpty()
                ? 0
                : loadedItem.getCount();
    }

    public void setLoadedItem(
            ItemStack stack
    ) {
        if (stack == null
                || stack.isEmpty()) {
            loadedItem =
                    ItemStack.EMPTY;
            return;
        }

        loadedItem =
                stack.copy();
    }

    public void clearLoadedItem() {
        loadedItem =
                ItemStack.EMPTY;
    }

    public boolean canFire(
            long gameTime,
            int interval
    ) {
        if (lastFireGameTime
                == Long.MIN_VALUE) {
            return true;
        }

        return gameTime
                - lastFireGameTime
                >= interval;
    }

    public void markFired(
            long gameTime
    ) {
        lastFireGameTime =
                gameTime;
    }

    public CompoundTag save() {
        CompoundTag tag =
                new CompoundTag();

        if (!loadedItem.isEmpty()) {
            tag.put(
                    "LoadedItem",
                    loadedItem.save(
                            new CompoundTag()
                    )
            );
        }

        return tag;
    }

    public void load(
            CompoundTag tag
    ) {
        clearLoadedItem();

        lastFireGameTime =
                Long.MIN_VALUE;

        if (tag.contains(
                "LoadedItem",
                Tag.TAG_COMPOUND
        )) {
            loadedItem =
                    ItemStack.of(
                            tag.getCompound(
                                    "LoadedItem"
                            )
                    );
        }
    }
}