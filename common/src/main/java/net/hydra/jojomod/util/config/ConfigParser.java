package net.hydra.jojomod.util.config;

import net.hydra.jojomod.util.config.annotation.BooleanOption;
import net.hydra.jojomod.util.config.annotation.CommentedOption;
import net.hydra.jojomod.util.config.annotation.FloatOption;
import net.hydra.jojomod.util.config.annotation.IntOption;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Class for serializing config classes supporting the CommentedOption annotation and allowing for HJSON improvements. */
public class ConfigParser {

    public static <T> List<String> parse(T object) {
        return parse(object, 0, new HashSet<>());
    }

    private static <T> List<String> parse(T object, int tabLevel, Set<Integer> visited) {
        List<String> parsed = new ArrayList<>();

        if (object == null) {
            parsed.add(indent(tabLevel) + "null");
            return parsed;
        }

        int identity = System.identityHashCode(object);
        if (visited.contains(identity)) {
            parsed.add(indent(tabLevel) + "\" -- circular reference -- \"");
            return parsed;
        }

        visited.add(identity);
        Class<?> clazz = object.getClass();
        parsed.add(indent(tabLevel) + "{");

        Field[] fields = clazz.getFields();
        for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
            Field field = fields[fieldIndex];
            field.setAccessible(true);
            String baseIndent = indent(tabLevel + 1);
            String fieldName = field.getName();

            try {
                Object value = field.get(object);

                /* Add the comment before the actual field */
                CommentedOption comment = field.getAnnotation(CommentedOption.class);
                if (comment != null) {
                    parsed.add(baseIndent + "/* " + comment.comment() + " */");
                }

                if (value == null) {
                    parsed.add(baseIndent + "\"" + fieldName + "\": null,");
                    continue;
                }

                Class<?> fieldType = field.getType();

                String closeNested = baseIndent + "}" + ((fieldIndex == fields.length - 1) ? "" : ",");

                if (field.get(object) instanceof HashSet<?> hashSet) {
                    /* Add field info */
                    // TODO: add support for HashSet<String> to getAnnotationTypeComment, preferably by adding a ListOption annotation?
                    //parsed.add(baseIndent + getAnnotationTypeComment(field));
                    parsed.add(baseIndent + "\"" + toSnakeCase(fieldName) + "\": [");

                    Object[] elements = hashSet.toArray();
                    for (int objIndex = 0; objIndex < elements.length; objIndex++) {
                        parsed.add(baseIndent + indent(1) + "\"" + elements[objIndex].toString() + "\"" +
                                // handle entry commas as well
                                (objIndex == elements.length - 1 ? "" : ","));
                    }

                    // close up the array
                    parsed.add(baseIndent + "]" + (fieldIndex == fields.length - 1 ? "" : ","));
                    // Cave Johnson. We're done here.
                    continue;
                }

                /* Primitive types are types like boolean, string, int, etc...
                * We check this so we can support class recursion to create better grouped configs */
                if (isPrimitiveLike(fieldType)) {
                    /* Add field info */
                    parsed.add(baseIndent + getAnnotationTypeComment(field));

                    // ternary for removing trailing commas
                    parsed.add(baseIndent + "\"" + toSnakeCase(fieldName) + "\": " + formatPrimitive(value) + ((fieldIndex == fields.length - 1) ? "" : ","));
                } else {
                    parsed.add(baseIndent + "\"" + toSnakeCase(fieldName) + "\": {");
                    List<String> nestedParsed = parse(value, tabLevel + 1, visited);

                    // remove first and last curly braces to inline the nested object
                    if (nestedParsed.size() >= 2) {
                        parsed.addAll(nestedParsed.subList(1, nestedParsed.size() - 1));
                        // ternary for removing trailing commas
                        parsed.add(closeNested);
                    } else {
                        parsed.addAll(nestedParsed);
                    }
                }

            } catch (IllegalAccessException e) {
                parsed.add(baseIndent + "\"" + fieldName + "\": \" -- error accessing field -- \",");
            }
        }

        parsed.add(indent(tabLevel) + "}");
        return parsed;
    }

    private static boolean isPrimitiveLike(Class<?> type) {
        return type.isPrimitive() ||
                type == String.class ||
                Number.class.isAssignableFrom(type) ||
                type == Boolean.class ||
                type == Character.class;
    }

    private static String formatPrimitive(Object value) {
        if (value instanceof String || value instanceof Character) {
            return "\"" + value.toString() + "\"";
        }
        return value.toString();
    }

    private static String indent(int level) {
        return "    ".repeat(level);
    }

    /* Get the comment for min/max & default values */
    private static String getAnnotationTypeComment(Field field)
    {
        BooleanOption booleanOption = field.getAnnotation(BooleanOption.class);
        FloatOption floatOption = field.getAnnotation(FloatOption.class);
        IntOption intOption = field.getAnnotation(IntOption.class);

        if (booleanOption == null && floatOption == null && intOption == null)
            return "/* -- error reading type info -- */";

        if (booleanOption != null)
            return String.format("/* Default Value: %s */", booleanOption.value());
        if (floatOption != null)
            return String.format("/* Minimum Value: %s | Maximum Value: %s | Default Value: %s */", floatOption.min(), floatOption.max(), floatOption.value());
        // we can ensure intOption is the last one remaining
        return String.format("/* Minimum Value: %s | Maximum Value: %s | Default Value: %s */", intOption.min(), intOption.max(), intOption.value());
    }

    /* Camel case is like testTestTest, Snake Case is what the config expects to validate (i.e. test_test_test) */
    public static String toSnakeCase(String input) {
        return input
                .replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .replaceAll("([A-Z])([A-Z][a-z])", "$1_$2")
                .toLowerCase();
    }
}