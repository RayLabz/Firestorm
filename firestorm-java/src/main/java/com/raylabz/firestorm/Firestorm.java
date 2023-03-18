package com.raylabz.firestorm;

import java.util.UUID;

public final class Firestorm {

    /**
     * Returns a random RFC4122 ID
     * @return Returns a random string-based ID.
     */
    public static String randomID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
