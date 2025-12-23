/*
 *  Copyright (C) 2024 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.visualization;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;

public class CharacterPdfViewModel extends ViewModel {

    protected byte[] generateCompletePdf() {
        final CharacterSheet characterSheet = new CharacterSheet(CharacterManager.getSelectedCharacter());
        try {
            return (characterSheet.generate());
        } catch (Exception e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
        return new byte[0];
    }

    protected byte[] generateSmallPdf() {
        final SmallCharacterSheet characterSheet = new SmallCharacterSheet(CharacterManager.getSelectedCharacter());
        try {
            return (characterSheet.generate());
        } catch (Exception e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
        return new byte[0];
    }
}
