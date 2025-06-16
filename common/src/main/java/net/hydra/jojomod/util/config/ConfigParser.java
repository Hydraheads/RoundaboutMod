package net.hydra.jojomod.util.config;

import net.hydra.jojomod.util.annotation.CommentedOption;

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

                /* Primitive types are types like boolean, string, int, etc...
                * We check this so we can support class recursion to create better grouped configs */
                if (isPrimitiveLike(fieldType)) {
                    // ternary for removing trailing commas
                    parsed.add(baseIndent + "\"" + fieldName + "\": " + formatPrimitive(value) + ((fieldIndex == fields.length - 1) ? "" : ","));
                } else {
                    parsed.add(baseIndent + "\"" + fieldName + "\": {");
                    List<String> nestedParsed = parse(value, tabLevel + 1, visited);

                    // remove first and last curly braces to inline the nested object
                    if (nestedParsed.size() >= 2) {
                        parsed.addAll(nestedParsed.subList(1, nestedParsed.size() - 1));
                        // ternary for removing trailing commas
                        parsed.add(baseIndent + "}" + ((fieldIndex == fields.length - 1) ? "" : ","));
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
}