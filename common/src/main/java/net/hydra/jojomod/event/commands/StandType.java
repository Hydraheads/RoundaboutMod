package net.hydra.jojomod.event.commands;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import java.util.function.IntFunction;

public enum StandType implements StringRepresentable {
    INVALID(-1, "invalid", null),
    NONE(0, "none", null),
    STAR_PLATINUM(1, "the_world", ModItems.STAND_DISC_STAR_PLATINUM),
    THE_WORLD(2, "the_world", ModItems.STAND_DISC_THE_WORLD),
    JUSTICE(3, "justice", ModItems.STAND_DISC_JUSTICE);

    public static final StandType DEFAULT_TYPE = NONE;
    public static final StringRepresentable.EnumCodec<StandType> CODEC = StringRepresentable.fromEnum(StandType::values);
    private static final IntFunction<StandType> BY_ID = ByIdMap.continuous(StandType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    private static final int NOT_SET = -1;
    private final int id;
    private final String name;
    private final Item item;
    private final Component translatedName;

    private StandType(int $$0, String $$1, Item standDisc) {
        this.id = $$0;
        this.name = $$1;
        this.item = standDisc;
        this.translatedName = Component.translatable("item.roundabout."+$$1+"_disc.desc" );
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    public ItemStack getItem() {
        if (this.item !=null){
            return this.item.getDefaultInstance();
        } else {
            return ItemStack.EMPTY;
        }
    }
    @Override
    public String getSerializedName() {
        return this.name;
    }

    public Component getTranslatedName() {
        return this.translatedName;
    }

    public static StandType byId(int $$0) {
        return BY_ID.apply($$0);
    }

    public static StandType byName(String $$0) {
        return byName($$0, NONE);
    }

    @Nullable
    @Contract("_,!null->!null;_,null->_")
    public static StandType byName(String $$0, @Nullable StandType $$1) {
        StandType $$2 = CODEC.byName($$0);
        return $$2 != null ? $$2 : $$1;
    }

    public static int getNullableId(@Nullable StandType $$0) {
        return $$0 != null ? $$0.id : -1;
    }

    @Nullable
    public static StandType byNullableId(int $$0) {
        return $$0 == -1 ? null : byId($$0);
    }
}

