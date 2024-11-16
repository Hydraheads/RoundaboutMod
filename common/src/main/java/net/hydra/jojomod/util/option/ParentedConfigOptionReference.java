package net.hydra.jojomod.util.option;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ParentedConfigOptionReference extends ConfigOptionReference {
    private final ConfigOptionReference parent;

    private ParentedConfigOptionReference(ConfigOptionReference parent, Object instance, Field field) {
        super(instance, field);
        this.parent = parent;
    }

    public static ParentedConfigOptionReference of(ConfigOptionReference parent, Object instance, Field field) {
        return new ParentedConfigOptionReference(parent, instance, field);
    }

    @Override
    public boolean isOf(String type) {
        if (getOptionType().equals("inherit")) return parent.isOf(type);
        return super.isOf(type);
    }

    @Override
    public String getFormattedName() {
        return getParent().getFormattedName() + "." + super.getFormattedName();
    }

    public ConfigOptionReference getParent() {
        return parent;
    }
}
