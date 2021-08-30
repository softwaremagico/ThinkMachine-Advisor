package com.softwaremagico.tm.advisor.utils;

import android.content.Context;

import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import dalvik.system.DexFile;

public class ClassManager {
    public final static String THINK_MACHINE_BASE_PACKAGE = "com.softwaremagico.tm";

    public static Set<Class<? extends IRandomPreference>> getClassesOfPackage(Context context, String packageName) {
        Set<Class<? extends IRandomPreference>> classes = new HashSet<>();
        try {
            String packageCodePath = context.getPackageCodePath();
            DexFile df = new DexFile(packageCodePath);
            for (Enumeration<String> classIterator = df.entries(); classIterator.hasMoreElements(); ) {
                String className = classIterator.nextElement();
                if (className.contains(packageName)) {
                    try {
                        Class<?> act = Class.forName(className);
                        if (IRandomPreference.class.isAssignableFrom(act)) {
                            classes.add((Class<? extends IRandomPreference>) act);
                        }
                    } catch (ClassNotFoundException e) {
                        AdvisorLog.errorMessage(ClassManager.class.getName(), e);
                    }
                }
            }
        } catch (IOException e) {
            AdvisorLog.errorMessage(ClassManager.class.getName(), e);
        }

        return classes;
    }
}
