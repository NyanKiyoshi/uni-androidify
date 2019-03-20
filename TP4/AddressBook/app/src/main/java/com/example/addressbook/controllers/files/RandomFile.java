package com.example.addressbook.controllers.files;

import java.io.File;
import java.util.UUID;

public final class RandomFile {
    public static File fromBase(File base) {
        return fromBase(base.getAbsoluteFile());
    }

    public static File fromBase(String base) {
        return new File(base, UUID.randomUUID().toString());
    }

    public static String sfromBase(String base) {
        return fromBase(base).getAbsolutePath();
    }

    public static String sfromBase(File base) {
        return fromBase(base).getAbsolutePath();
    }
}
