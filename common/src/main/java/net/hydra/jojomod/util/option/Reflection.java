package net.hydra.jojomod.util.option;
import net.hydra.jojomod.util.Config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class Reflection {
    public static <T extends Annotation> void forEachFieldByAnnotation(Object instance, Class<T> annotation, BiConsumer<Field, T> consumer) {
        forEachFieldByAnnotation(instance.getClass(), annotation, consumer);
    }

    public static void forEachAnnotatedField(Object instance, Consumer<Field> fieldConsumer) {
        forEachAnnotatedField(instance.getClass(), fieldConsumer);
    }

    public static Stream<Field> streamFields(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields());
    }

    private static Stream<Field> streamFieldsByAnnotation(Stream<Field> stream, Class<? extends Annotation> annotation) {
        return stream.filter(field -> field.isAnnotationPresent(annotation));
    }

    private static <T extends Annotation> void forEachFieldByAnnotation(Class<?> clazz, Class<T> annotation, BiConsumer<Field, T> consumer) {
        streamFieldsByAnnotation(streamFields(clazz), annotation).forEach(field -> consumer.accept(field, field.getAnnotation(annotation)));
    }

    private static void forEachAnnotatedField(Class<?> clazz, Consumer<Field> fieldConsumer) {
        streamFields(clazz).filter(field -> field.getAnnotations().length > 0).forEach(fieldConsumer);
    }

    public static <T extends Annotation> T getAnnotation(Config config, Class<T> annotation) {
        return config.getClass().getAnnotation(annotation);
    }

    public static Object accessField(Field field, Object instance) {
        Object value;
        try {
            if (!field.canAccess(instance)) {
                field.setAccessible(true);
            }
            value = field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    public static <T> T accessField(Field field, Object instance, Class<T> fieldClass) {
        return fieldClass.cast(accessField(field, instance));
    }

    public static void setField(Field field, Object instance, Object value) {
        try {
            if (!field.canAccess(instance)) {
                field.setAccessible(true);
            }
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object newInstance(Class<?> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}