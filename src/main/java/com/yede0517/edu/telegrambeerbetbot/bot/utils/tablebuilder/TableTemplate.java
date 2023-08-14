package com.yede0517.edu.telegrambeerbetbot.bot.utils.tablebuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Data;

@Data
public class TableTemplate<T> {
    private List<T> data;
    private List<String> rows;
    private Map<String, List<String>> columns;
    private Map<String, TableCellAlignmentType> columnAlignment;
    private Map<String, Function<T, String>> metadata;

    public TableTemplate(List<T> data) {
        this.data = data;
        this.columns = new LinkedHashMap<>();
        this.metadata = new LinkedHashMap<>();
        this.columnAlignment = new HashMap<>();
        this.rows = IntStream.range(0, data.size() + 1)
                .mapToObj(i -> " ")
                .collect(Collectors.toList());
    }

    public TableTemplate<T> addColumn(String name, Function<T, String> rowValueExtractor) {
        return addColumn(name, rowValueExtractor, TableCellAlignmentType.RIGHT);
    }

    public TableTemplate<T> addColumn(String name, Function<T, String> rowValueExtractor, TableCellAlignmentType type) {
        metadata.put(name, rowValueExtractor);
        columnAlignment.put(name, type);
        return this;
    }

    public String draw() {
        this.columns = new LinkedHashMap<>();
        for (Map.Entry<String, Function<T, String>> entry : metadata.entrySet()) {
            List<String> values = data.stream()
                    .map(entry.getValue())
                    .collect(Collectors.toList());
            values.add(0, entry.getKey());
            this.columns.put(entry.getKey(), values);
        }

        this.columns.forEach((column, values) -> {
            int maxValueLength = values.stream()
                    .mapToInt(String::length)
                    .max()
                    .orElseThrow(IllegalArgumentException::new);

            for (int i = 0; i < values.size(); i++) {
                String rowValue = rows.get(i);
                String newValue = align(values.get(i), maxValueLength, columnAlignment.get(column));
                rows.set(i, rowValue.concat(newValue + " "));
            }
        });

        return String.join("\n", this.rows);
    }

    public String align(String value, int width, TableCellAlignmentType type) {
        StringBuilder result = new StringBuilder(value);
        if (value.length() == width) return value;

        switch (type) {
            case LEFT:
                for (int i = 0; i < width - value.length(); i++) {
                    result.append(" ");
                }
                break;
            case RIGHT:
                for (int i = 0; i < width - value.length(); i++) {
                    result.insert(0, " ");
                }
                break;
            case CENTER:
                for (int i = 0; i < (width - value.length()) / 2; i++) {
                    result.append(" ");
                }
                for (int i = 0; i < (width - value.length()) / 2; i++) {
                    result.insert(0, " ");
                }
                break;

        }

        return result.toString();
    }
}
