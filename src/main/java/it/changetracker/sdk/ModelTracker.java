package it.changetracker.sdk;

import it.changetracker.sdk.core.FieldMapper;
import it.changetracker.sdk.core.Tracker;
import it.changetracker.sdk.utils.StringHelper;
import it.changetracker.sdk.utils.TypeHelper;
import it.changetracker.sdk.models.Field;
import it.changetracker.sdk.models.Row;
import it.changetracker.sdk.models.Table;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class ModelTracker {
    public static <T> ModelMapper<T> createMap(T model) {
        return new ModelMapper<T>(model);
    }

    public static <T> ModelMapper<T> mapAll(T model) {
        return createMap(model).mapAll();
    }

    public static <T, F> ModelMapper<T> map(T model, Function<T, F> func, String fieldName) {
        return map(model, func, fieldName, null);
    }

    public static <T, F> ModelMapper<T> map(T model, Function<T, F> func, String fieldName, String format) {
        return createMap(model).map(func, fieldName, format);
    }

    public static <T> ModelMapper<T> map(T model, String mapping) {
        return map(model, mapping, null, null);
    }

    public static <T> ModelMapper<T> map(T model, String mapping,
                                         String fieldName) {
        return map(model, mapping, fieldName, null);
    }

    public static <T> ModelMapper<T> map(T model, String mapping, String fieldName, String format) {
        return createMap(model).map(mapping, fieldName, format);
    }

    public static Table toTable(String tableName, List<Row> rows) {
        return new Table(tableName, Optional.ofNullable(rows).map(list -> list.stream().toList()).orElse(new ArrayList<>()));
    }

    public static class ModelMapper<T> {
        private final T model;
        private final HashMap<String, Function<T, ?>> fields;
        private final HashMap<String, String> fieldFormats;

        public ModelMapper(T model) {
            if (model == null) throw new InvalidParameterException("model");

            this.model = model;
            fields = new HashMap<>();
            fieldFormats = new HashMap<>();
        }

        public ModelMapper<T> mapAll() {
            var type = model.getClass();

            var properties = type.getDeclaredFields();

            for (var property : properties) {
                var annotation = getModelTrackerMappingAnnotation(property);
                var fieldName = property.getName();
                var assignedName = fieldName;

                if (annotation != null) {
                    if (annotation.ignore()) continue;

                    assignedName = StringHelper.isNullOrEmpty(annotation.name()) ? assignedName : annotation.name();

                    if (!StringHelper.isNullOrEmpty(annotation.format()))
                        fieldFormats.put(assignedName, annotation.format());

                    if (parseAttributeMapping(annotation, assignedName)) continue;
                }

                if (!TypeHelper.isSimpleType(property.getType())) continue;

                fields.put(assignedName, generateAccessor(Function.identity(), fieldName));
            }

            return this;
        }

        private boolean parseAttributeMapping(Tracker annotation, String name) {
            //Nel caso in cui il mapping sia vuoto provo a recuperare il dato partendo dalla property
            //Se il mapping è presente ma errato escludo il campo dai risultati
            if (annotation == null || annotation.ignore() || StringHelper.isNullOrEmpty(annotation.mapping()))
                return false;

            parseMapping(annotation.mapping(), name);
            return true;
        }

        private void parseMapping(String mapping, String name) {
            var split = Arrays.stream(mapping.split("\\.")).filter(el -> !StringHelper.isNullOrEmpty(el)).toList();

            if (split.isEmpty()) return;

            Function<T, ?> lambda = Function.identity();

            for (var token : split)
                lambda = generateAccessor(lambda, token);

            fields.put(name, lambda);
        }

        public <F> ModelMapper<T> map(Function<T, F> mapping, String fieldName) {
            return map(mapping, fieldName, null);
        }

        public ModelMapper<T> map(Function<T, ?> mapping, String fieldName, String format) {
            if (mapping == null || StringHelper.isNullOrEmpty(fieldName))
                throw new InvalidParameterException(String.format("ChangeTracker, ModelMapper.map, Error: invalid mapping or fieldName (%s)", fieldName));

            fields.put(fieldName, mapping);
            fieldFormats.put(fieldName, format);

            return this;
        }

        public ModelMapper<T> map(String mapping) {
            return map(mapping, null, null);
        }

        public ModelMapper<T> map(String mapping, String fieldName) {
            return map(mapping, fieldName, null);
        }

        public ModelMapper<T> map(String mapping, String fieldName, String format) {
            if (StringHelper.isNullOrEmpty(mapping)) return this;

            fieldName = Optional.ofNullable(fieldName).orElse(extractFieldNameFromMapping(mapping));

            parseMapping(mapping, fieldName);

            if (!StringHelper.isNullOrEmpty(format))
                fieldFormats.put(fieldName, format);

            return this;
        }

        public ModelMapper<T> ignore(String fieldName) {
            if (StringHelper.isNullOrEmpty(fieldName)) throw new InvalidParameterException("fieldName");

            fields.remove(fieldName);

            return this;
        }

        public List<Field> toList() {
            var res = new ArrayList<Field>();
            fields.forEach((key, generator) ->
            {
                String value;
                Class<?> fieldType = String.class;
                var fieldFormat = fieldFormats.getOrDefault(key, null);

                try {
                    var exprValue = (Object) generator.apply(model);
                    fieldType = exprValue != null ? exprValue.getClass() : fieldType;
                    value = FieldMapper.convertValue(exprValue, fieldFormat);
                } catch (Exception e) {
                    value = null;
                }

                var fieldModel = new Field();
                fieldModel.setName(key);
                fieldModel.setFormat(fieldFormat);
                fieldModel.setPrevValue(value);
                fieldModel.setType(fieldType);

                res.add(fieldModel);
            });

            return res;
        }

        public Row toRow(String rowKey) {
            return toRow(rowKey, null);
        }

        public Row toRow(String rowKey, List<Table> linkedTables) {
            var row = new Row(rowKey);
            row.setFields(toList());

            if (linkedTables != null && !linkedTables.isEmpty())
                row.setTables(linkedTables);

            return row;
        }

        private static String extractFieldNameFromMapping(String mapping) {
            //extra check to ignore exceptions
            if (mapping == null) return null;

            var matcher = Pattern.compile("\\..").matcher(mapping);
            var stringBuilder = new StringBuilder();

            while (matcher.find())
                matcher.appendReplacement(stringBuilder, mapping.substring(matcher.start(), matcher.end()).replace(".", "").toUpperCase());

            matcher.appendTail(stringBuilder);
            var res = stringBuilder.toString();

            //remove trailing dots
            res = res.replace(".", "");
            return res;
        }
    }

    public static <T> Function<T, ?> generateAccessor(Function<T, ?> supplier, String fieldName) {
        return (T instance) -> {
            if (instance == null) return null;
            try {
                var model = supplier.apply(instance);

                if (model == null) return null;

                var type = model.getClass();
                final java.lang.reflect.Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);

                return field.get(model);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("ChangeTracker, error accessing to field named:" + fieldName + " origin: " + instance.getClass());
                return null;
            }
        };
    }

    private static Tracker getModelTrackerMappingAnnotation(java.lang.reflect.Field property) {
        return property.getAnnotation(Tracker.class);
    }
}

