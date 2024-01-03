package com.qwlabs.excel;

import java.io.InputStream;

public final class Excels {
    private Excels() {
    }

    public static SingleRowReader singleRowReader(InputStream inputStream) {
        return new SingleRowReader(inputStream);
    }

    public static DataReader dataReader(InputStream inputStream) {
        return new DataReader(inputStream);
    }
}
