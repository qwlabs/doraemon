package com.qwlabs.jackson.datatype;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class StringProcessModule extends SimpleModule {
    @Override
    public String getModuleName() {
        return "StringProcessModule";
    }

    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }

    @Override
    public void setupModule(SetupContext setupContext) {
        setupContext.addDeserializers(StringProcessDeserializers.INSTANCE);
    }
}
