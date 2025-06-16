package net.hydra.jojomod.util.option;
import net.hydra.jojomod.util.config.annotation.BooleanOption;
import net.hydra.jojomod.util.config.annotation.FloatOption;
import net.hydra.jojomod.util.config.annotation.IntOption;
import net.hydra.jojomod.util.config.annotation.NestedOption;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

public class ConfigOptionReference {

    private final Object instance;
    private final Field field;
    private Object value;

    protected ConfigOptionReference(Object instance, Field field) {
        this.instance = instance;
        this.field = field;
        this.value = Reflection.accessField(field, instance);
    }

    public static ConfigOptionReference of(Object instance, Field field) {
        ConfigOptionReference reference = new ConfigOptionReference(instance, field);
        reference.validateAnnotations();
        return reference;
    }

    public static Optional<String> readGroup(Field field) {
        return Optional.of(switch (FieldType.of(field.getType())) {
            case BOOLEAN -> field.getAnnotation(BooleanOption.class).group();
            case INTEGER -> field.getAnnotation(IntOption.class).group();
            case FLOAT -> field.getAnnotation(FloatOption.class).group();
            case NESTED -> field.getAnnotation(NestedOption.class).group();
        });
    }

    private void validateAnnotations() {
        Annotation[] annotations = field.getAnnotations();
        if (annotations.length == 0)
            throw new IllegalStateException("Field \"" + field.getName() + "\" has no annotations.");
        boolean hasType = false;
        for (Annotation annotation : annotations) {
            if (annotation instanceof BooleanOption || annotation instanceof IntOption || annotation instanceof FloatOption || annotation instanceof NestedOption) {
                if (hasType)
                    throw new IllegalStateException("Field \"" + field.getName() + "\" has multiple type annotations.");
                hasType = true;
            }
        }
        if (!hasType) throw new IllegalStateException("Field \"" + field.getName() + "\" has no type annotation.");
        validateOptionType();
    }

    private void validateOptionType() {
        FieldType fieldType = getType();
        Class<? extends Annotation> expectedAnnotation = fieldType.expectedAnnotation();
        Annotation annotation = field.getAnnotation(expectedAnnotation);
        if (annotation == null)
            throw new IllegalStateException("Field \"" + field.getName() + "\" has invalid type annotation. Expected @" + expectedAnnotation.getSimpleName());
    }

    private void validateValueType(Class<?> clazz) {
        if (value == null) return;
        if (clazz == null) throw new IllegalArgumentException("Class cannot be null.");
        if (!clazz.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Invalid type " + clazz.getName() + " for " + value.getClass()
                    .getName());
        }
    }

    public <T> void setConfigValue(T value) {
        if (value == null) throw new IllegalArgumentException("Value cannot be null.");
        validateValueType(value.getClass());
        Reflection.setField(field, instance, value);
        this.value = value;
    }

    private <T> T getValueAsType(Class<T> clazz) {
        validateValueType(clazz);
        return clazz.cast(value);
    }

    public boolean isValueNull() {
        return value == null;
    }

    public String getFormattedName() {
        StringBuilder builder = new StringBuilder();
        String name = field.getName();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                builder.append('.');
                builder.append(Character.toLowerCase(c));
            } else builder.append(c);
        }
        return builder.toString();
    }

    public boolean isOf(String type) {
        return getOptionType().equals(type);
    }

    public String getOptionType() {
        validateOptionType();
        return switch (getType()) {
            case BOOLEAN -> field.getAnnotation(BooleanOption.class).group();
            case INTEGER -> field.getAnnotation(IntOption.class).group();
            case FLOAT -> field.getAnnotation(FloatOption.class).group();
            case NESTED -> field.getAnnotation(NestedOption.class).group();
        };
    }

    public boolean isNested() {
        return getType() == FieldType.NESTED;
    }

    public FieldType getType() {
        return FieldType.of(field.getType());
    }

    public void booleanValue(Boolean value) {
        setConfigValue(value);
    }

    public void intValue(Integer value) {
        setConfigValue(value);
    }

    public void floatValue(Float value) {
        setConfigValue(value);
    }

    public Boolean booleanValue() {
        return getValueAsType(Boolean.class);
    }

    public Integer intValue() {
        return getValueAsType(Integer.class);
    }

    public Float floatValue() {
        return getValueAsType(Float.class);
    }

    public Object nestedValue() {
        return getValueAsType(Object.class);
    }

    private Number minValue() {
        return switch (getType()) {
            case FLOAT -> field.getAnnotation(FloatOption.class).min();
            case INTEGER -> field.getAnnotation(IntOption.class).min();
            case BOOLEAN, NESTED -> throw new IllegalArgumentException("Cannot get min value for type " + getType());
        };
    }

    private Number maxValue() {
        return switch (getType()) {
            case FLOAT -> field.getAnnotation(FloatOption.class).max();
            case INTEGER -> field.getAnnotation(IntOption.class).max();
            case BOOLEAN, NESTED -> throw new IllegalArgumentException("Cannot get max value for type " + getType());
        };
    }

    public Float minFloatValue() {
        return (Float) minValue();
    }

    public Float maxFloatValue() {
        return (Float) maxValue();
    }

    public Integer minIntValue() {
        return (Integer) minValue();
    }

    public Integer maxIntValue() {
        return (Integer) maxValue();
    }


    @Override
    public int hashCode() {
        return Objects.hash(field);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ConfigOptionReference other)) return false;
        return Objects.equals(field, other.field);
    }

    @Override
    public String toString() {
        return "ConfigOptionReference[" + "field=" + field + ", value=" + value + ']';
    }

    public enum FieldType {
        BOOLEAN,
        INTEGER,
        FLOAT,
        NESTED;

        public static FieldType of(Class<?> clazz) {
            if (Boolean.class.isAssignableFrom(clazz)) return BOOLEAN;
            if (Integer.class.isAssignableFrom(clazz)) return INTEGER;
            if (Float.class.isAssignableFrom(clazz)) return FLOAT;
            if (Object.class.isAssignableFrom(clazz)) return NESTED;
            throw new IllegalArgumentException("Unknown type " + clazz.getName());
        }

        private Class<? extends Annotation> expectedAnnotation() {
            return switch (this) {
                case BOOLEAN -> BooleanOption.class;
                case INTEGER -> IntOption.class;
                case FLOAT -> FloatOption.class;
                case NESTED -> NestedOption.class;
            };
        }
    }
}