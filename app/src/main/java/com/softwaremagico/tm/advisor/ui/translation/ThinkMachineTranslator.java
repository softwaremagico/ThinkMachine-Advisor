package com.softwaremagico.tm.advisor.ui.translation;

import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class ThinkMachineTranslator {
    private static final String TRANSLATOR_FILE = "character_sheet.xml";
    private static ITranslator translator;

    public static String getTranslatedText(String tag){
        return getTranslator().getTranslatedText(tag);
    }

    public static ITranslator getTranslator() {
        if (translator == null) {
            translator = LanguagePool.getTranslator(TRANSLATOR_FILE, (String) null);
        }
        return translator;
    }
}
