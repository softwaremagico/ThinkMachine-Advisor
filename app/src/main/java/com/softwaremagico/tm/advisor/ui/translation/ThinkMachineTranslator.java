/*
 *  Copyright (C) 2020 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.translation;

import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public final class ThinkMachineTranslator {
    private static final String TRANSLATOR_FILE = "character_sheet.xml";
    private static ITranslator translator;

    private ThinkMachineTranslator() {

    }

    public static String getTranslatedText(String tag) {
        return getTranslator().getTranslatedText(tag);
    }

    public static ITranslator getTranslator() {
        if (translator == null) {
            translator = LanguagePool.getTranslator(TRANSLATOR_FILE, null);
        }
        return translator;
    }
}
