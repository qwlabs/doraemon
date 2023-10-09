package com.qwlabs.jackson.datatype;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.util.VersionUtil;

public class PackageVersion implements Versioned {
    public static final Version VERSION = VersionUtil.parseVersion(
        "0.2.0", "com.qwlabs.doraemom", "jackson");
    @Override
    public Version version() {
        return VERSION;
    }
}
