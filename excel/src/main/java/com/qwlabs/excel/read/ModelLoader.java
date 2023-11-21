package com.qwlabs.excel.read;

import com.qwlabs.excel.validation.Violations;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Objects;

public class ModelLoader<T> {
    private final ReadOptions<T> options;
    private final ExcelDataReader dataReader;
    private final ReadFieldLoaders<T> fieldLoaders;
    private final Violations violations;
    private final List<T> data;

    protected ModelLoader(ReadOptions<T> options, ExcelDataReader dataReader) {
        this.options = options;
        this.dataReader = dataReader;
        this.fieldLoaders = new ReadFieldLoaders<>(options, dataReader);
        this.violations = new Violations();
        this.data = Lists.newArrayList();
    }

    public void load() {
        this.fieldLoaders.init(this.violations);
        var dataIndex = options.getDataStartIndex();
        var limit = options.getDataLimit();
        while (!dataReader.isTail(dataIndex) && limit >= 0) {
            var entry = loadEntry(dataIndex);
            if (Objects.nonNull(entry)) {
                data.add(entry);
            }
            dataIndex++;
            limit--;
        }
    }

    private T loadEntry(int dataIndex) {
        var cells = dataReader.by(dataIndex);







        return null;
    }

    public List<T> getData() {
        return data;
    }
}
