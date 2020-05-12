package com.worklight.wlclient.api;

import com.worklight.common.Logger;

public enum WLErrorCode {
    UNEXPECTED_ERROR("Unexpected errorCode occurred. Please try again."),
    REQUEST_TIMEOUT("Request timed out."),
    AUTHORIZATION_FAILURE("Failure during authorization sequence."),
    CHALLENGE_HANDLING_CANCELED("Challenge handler operation was cancelled."),
    MISSING_CHALLENGE_HANDLER("Missing challenge handler for security check"),
    ILLEGAL_ARGUMENT_EXCEPTION("Illegal argument exception."),
    LOGIN_ALREADY_IN_PROCESS("Login already in process."),
    LOGOUT_ALREADY_IN_PROCESS("Logout already in process."),
    APPLICATION_NOT_REGISTERED("Application not registered yet."),
    SERVER_ERROR("Server returned fail response."),
    MINIMUM_SERVER("Server does not meet the minimum requirements"),
    APPLICATION_DISABLED("The application is disabled.");
    
    private final String description;

    private WLErrorCode(String str) {
        this.description = str;
    }

    public String getDescription() {
        String str = "getDescription";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.description;
    }
}
