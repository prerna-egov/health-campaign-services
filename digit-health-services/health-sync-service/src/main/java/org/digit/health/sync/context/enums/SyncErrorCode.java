package org.digit.health.sync.context.enums;

import org.digit.health.sync.context.step.SyncStep;

public enum SyncErrorCode {
    STEP_ALREADY_HANDLED("Step is already handled"),
    INVALID_JSON_FILE("Invalid message"),
    INVALID_FILE("Invalid file"),
    INVALID_CHECKSUM("Checksum did not match the checksum received"),
    INVALID_CHECKSUM_ALGORITHM("Checksum algorithm is invalid"),
    ERROR_IN_DECOMPRESSION("Error during decompression of file"),
    ERROR_IN_MAPPING_JSON("Error in mapping json to java"),
    ERROR_IN_REST_CALL("Error while calling another service");


    String message;
    SyncErrorCode(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }

    public String message(String errorMessage) {
        return String.join(":", message, errorMessage);
    }

    public String message(Class<? extends SyncStep> clazz) {
        return String.join(":", clazz.getName(), this.message);
    }
}