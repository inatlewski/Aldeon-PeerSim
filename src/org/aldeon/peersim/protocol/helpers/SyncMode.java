package org.aldeon.peersim.protocol.helpers;

public class SyncMode {
    public static final int BRANCH_HASH_WITH_SUGGEST = 1;
    public static final int BRANCH_HASH_WITHOUT_SUGGEST = 2;
    public static final int ALWAYS_GET_ALL = 3;

    public static int fromString(String syncMode) throws Exception {
        if (syncMode.equals("BRANCH_HASH_WITH_SUGGEST")) {
            return BRANCH_HASH_WITH_SUGGEST;
        } else if (syncMode.equals("BRANCH_HASH_WITHOUT_SUGGEST")) {
            return BRANCH_HASH_WITHOUT_SUGGEST;
        } else if (syncMode.equals("ALWAYS_GET_ALL")) {
            return ALWAYS_GET_ALL;
        } else {
            throw new Exception("Unsupported sync mode");
        }
    }

    public static String toString(int syncMode) throws Exception {
        if (syncMode == BRANCH_HASH_WITH_SUGGEST) {
            return "BRANCH_HASH_WITH_SUGGEST";
        } else if (syncMode == BRANCH_HASH_WITHOUT_SUGGEST) {
            return "BRANCH_HASH_WITHOUT_SUGGEST";
        } else if (syncMode == ALWAYS_GET_ALL) {
            return "ALWAYS_GET_ALL";
        } else {
            throw new Exception("Unsupported sync mode");
        }
    }
}
