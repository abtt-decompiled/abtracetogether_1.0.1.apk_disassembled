package com.worklight.common.internal;

import com.worklight.common.Logger;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class JULHandler extends Handler {
    public void close() throws SecurityException {
        String str = "close";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void flush() {
        String str = "flush";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void publish(LogRecord logRecord) {
        String str = "publish";
        Logger.enter(getClass().getSimpleName(), str);
        Logger instance = Logger.getInstance(logRecord.getLoggerName());
        if (logRecord.getLevel().equals(Level.SEVERE)) {
            instance.error(logRecord.getMessage(), logRecord.getThrown());
        } else if (logRecord.getLevel().equals(Level.WARNING)) {
            instance.warn(logRecord.getMessage());
        } else if (logRecord.getLevel().equals(Level.INFO)) {
            instance.info(logRecord.getMessage());
        } else if (logRecord.getLevel().equals(Level.CONFIG)) {
            instance.log(logRecord.getMessage());
        } else if (logRecord.getLevel().equals(Level.FINE)) {
            instance.debug(logRecord.getMessage());
        } else {
            instance.trace(logRecord.getMessage());
        }
        Logger.exit(getClass().getSimpleName(), str);
    }
}
