package com.qwlabs.excel.read;

import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

import java.util.Map;
import java.util.function.Supplier;

public class ReadSheet {
    private final Map<Integer, Map<Integer, ReadCellData<?>>> byRow;
    private int maxRowIndex;
    private final Supplier<Map<Integer, Map<Integer, ReadCellData<?>>>> byColumn = Suppliers.memoize(this::reshape);
    private int internalMaxColumnIndex;
    private final Supplier<Integer> maxColumnIndex = Suppliers.memoize(()->{
        byColumn.get();
        return internalMaxColumnIndex;
    });

    private ReadSheet(int expectedSize) {
        this.byRow = Maps.newLinkedHashMapWithExpectedSize(expectedSize);
    }

    private Map<Integer, Map<Integer, ReadCellData<?>>> reshape() {
        Map<Integer, Map<Integer, ReadCellData<?>>> result = Maps.newLinkedHashMap();
        byRow.forEach((row, rowData) ->
            rowData.forEach((column, cellData) ->{
                result.computeIfAbsent(column, key -> Maps.newLinkedHashMap()).putIfAbsent(row, cellData);
                this.internalMaxColumnIndex = Ints.max(internalMaxColumnIndex, column);
            })
        );
        return result;
    }

    public ReadSheet put(int rowIndex, Map<Integer, ReadCellData<?>> data) {
        byRow.put(rowIndex, data);
        this.maxRowIndex = Ints.max(this.maxRowIndex, rowIndex);
        return this;
    }

    public void put(CellExtra extra) {
        if (extra.getType() == CellExtraTypeEnum.MERGE) {
            putMerge(extra);
        }
    }

    private void putMerge(CellExtra extra) {
        var cell = byRow.getOrDefault(extra.getFirstRowIndex(), Map.of()).get(extra.getFirstColumnIndex());
        if (cell == null) {
            return;
        }
        int rowIndex = extra.getFirstRowIndex();
        while (rowIndex <= extra.getLastRowIndex()) {
            int columnIndex = extra.getFirstColumnIndex();
            var cells = this.byRow.computeIfAbsent(rowIndex, key -> Maps.newLinkedHashMap());
            while (columnIndex <= extra.getLastColumnIndex()) {
                cells.put(columnIndex, cell);
                columnIndex++;
            }
            rowIndex++;
        }
    }

    public static ReadSheet of(int expectedSize) {
        return new ReadSheet(expectedSize);
    }

    public Map<Integer, ReadCellData<?>> loadRowByColumn(int index) {
        return byRow.get(index);
    }

    public Map<Integer, ReadCellData<?>> loadColumnByRow(int index) {
        return byColumn.get().get(index);
    }

    public int maxRowIndex(){
        return this.maxRowIndex;
    }

    public int maxColumnIndex(){
        return this.maxColumnIndex.get();
    }
}
