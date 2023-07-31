package com.absolute.changetracker.sdk.core;

import com.absolute.changetracker.sdk.models.Field;
import com.absolute.changetracker.sdk.models.Row;
import com.absolute.changetracker.sdk.models.RowStatus;
import com.absolute.changetracker.sdk.models.Table;
import com.absolute.changetracker.sdk.utils.ListHelper;
import com.absolute.changetracker.sdk.utils.StringHelper;
import com.absolute.changetracker.sdk.utils.TypeHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;

/**
 * StandardChangeTrackerCalculator - default ChangeTracker row diff calculator
 **/
public class StandardChangeTrackerCalculator implements ChangeTrackerCalculator {


    /**
     * diff - create a diff Row between prev and next
     * @param prev - row containing old values
     * @param next - row containing new values
     **/
    @Override
    public Row diff(Row prev, Row next) {
        var diff = new Row();

        if (prev != null) diff.setKey(prev.getKey());
        if (prev == null && next != null) diff.setKey(next.getKey());
        if (prev == null && next == null) return null;

        if (next == null) diff.setState(RowStatus.Deleted.toString());
        if (prev == null) diff.setState(RowStatus.New.toString());

        if (prev != null)
            for (var field : prev.getFields()) {
                if (RowStatus.Deleted.toString().equals(diff.getState()) &&
                        Objects.equals(field.getPrevValue(), getDefault(field))) continue;

                var diffField = new Field();
                diffField.setName(field.getName());
                diffField.setPrevValue(field.getPrevValue());

                diff.getFields().add(diffField);
            }

        if (next != null) {
            for (var field : next.getFields()) {
                if (RowStatus.New.toString().equals(diff.getState()) && Objects.equals(field.getPrevValue(), getDefault(field)))
                    continue;

                var diffField = diff.getFields().stream().filter(el ->
                                StringHelper.safeEqualsIgnoreCase(el.getName(), field.getName()))
                        .findFirst().orElse(null);

                if (diffField == null) {
                    diffField = new Field();
                    diffField.setName(field.getName());
                    diffField.setNextValue(field.getPrevValue());
                    diff.getFields().add(diffField);
                } else
                    diffField.setNextValue(field.getPrevValue());
            }
        }

        // Prende solo quelli differenti
        diff.setFields(diff.getFields().stream().filter(el -> !StringHelper.safeEqualsIgnoreCase(el.getPrevValue(), el.getNextValue())).toList());

        var diffState = diff.getState();
        if (StringHelper.isNullOrEmpty(diffState))
            diff.setState(Optional.ofNullable(diff.getFields()).filter(fields -> !fields.isEmpty()).map(fields -> RowStatus.Modified.toString()).orElse(RowStatus.Unchanged.toString()));

        if (prev != null && prev.getTables() != null && !prev.getTables().isEmpty())
            for (var table : prev.getTables()) {
                var addedTable = new Table(table.getName());
                diff.getTables().add(addedTable);

                for (var row : table.getRows()) {
                    var nextRow = Optional.ofNullable(next).flatMap(n -> ListHelper.safeFindFirst(n.getTables(), el -> StringHelper.safeEqualsIgnoreCase(el.getName(), table.getName()))).flatMap(t ->
                            ListHelper.safeFindFirst(t.getRows(), el -> Objects.equals(el.getKey(), row.getKey()))).orElse(null);

                    var diffRow = diff(row, nextRow);
                    if (diffRow != null && diffRow.isFilled()) addedTable.getRows().add(diffRow);
                }
            }

        if (next != null && next.getTables() != null && !next.getTables().isEmpty())
            for (var table : next.getTables()) {
                var addedTable = diff.getTables().stream().filter(el -> Objects.equals(el.getName(), table.getName())).findFirst().orElse(null);
                if (addedTable == null) {
                    addedTable = new Table(table.getName());
                    diff.getTables().add(addedTable);
                }

                for (var row : table.getRows()) {
                    var prevRow = Optional.ofNullable(prev).flatMap(p -> ListHelper.safeFindFirst(p.getTables(), el -> StringHelper.safeEqualsIgnoreCase(el.getName(), table.getName())).flatMap(t ->
                            ListHelper.safeFindFirst(t.getRows(), el -> Objects.equals(el.getKey(), row.getKey()))
                    )).orElse(null);

                    var diffRow = diff(prevRow, row);
                    var alreadyRow = addedTable.getRows().stream().filter(el -> Objects.equals(el.getKey(), row.getKey())).findFirst().orElse(null);

                    if (alreadyRow == null && diffRow != null && diffRow.isFilled())
                        addedTable.getRows().add(diffRow);
                }
            }

        diff.setTables(diff.getTables().stream().filter(el -> !el.getRows().isEmpty()).toList());

        return diff;
    }

    private <T> String getDefault(Field field) {
        var type = field.getType();
        var format = field.getFormat();
        if (type != null && TypeHelper.isSimpleType(type)) {
            try {
                return FieldMapper.convertValue(createDefaultInstance(type), format);
            } catch (Exception e) {
                return "";
            }
        }

        return "";
    }

    public static <T> T createDefaultInstance(Class<T> c) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return c.getConstructor().newInstance();
    }
}
