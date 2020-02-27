package com.softwaremagico.tm.advisor.log;

import org.apache.log4j.Level;
import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class ConfigureLogger {
    static {
        final LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator
                .setLogCatPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %p:%r:%c:%m%n");
        logConfigurator.setUseFileAppender(false);
        logConfigurator.setUseLogCatAppender(true);
       // logConfigurator.setFileName(Environment.getExternalStorageDirectory() + "advisor.log");
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();
    }
}
