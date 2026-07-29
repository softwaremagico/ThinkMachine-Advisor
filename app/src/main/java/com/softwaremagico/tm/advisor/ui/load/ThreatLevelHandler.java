package com.softwaremagico.tm.advisor.ui.load;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.ThreatLevel;
import com.softwaremagico.tm.character.creation.CharacterProgressionStatus;

public class ThreatLevelHandler {
    private static final String HEXADECIMAL_FORMAT = "#%06x";
    private final int threatLevel;

    public ThreatLevelHandler(int threatLevel) {
        this.threatLevel = threatLevel;
    }

    public String getThreatLevel() {
        if (threatLevel < 40) {
            return "☮☮☮☮";
        } else if (threatLevel < 60) {
            return "☮☮☮";
        } else if (threatLevel < 80) {
            return "☮☮";
        } else if (threatLevel < 100) {
            return "☮";
        } else if (threatLevel < 120) {
            return "☠";
        } else if (threatLevel < 150) {
            return "☠☠";
        } else if (threatLevel < 175) {
            return "☠☠☠";
        } else if (threatLevel < 200) {
            return "☠☠☠☠";
        } else if (threatLevel < 250) {
            return "☠☠☠☠☠";
        }
        return "☠☠☠☠☠☠";
    }

    public String getColor(Context context) {
        if (threatLevel < 100) {
            return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatPeace) & 0xffffff);
        } else if (threatLevel < 125) {
            return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatLow) & 0xffffff);
        } else if (threatLevel < 150) {
            return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatMedium) & 0xffffff);
        } else if (threatLevel < 200) {
            return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatHigh) & 0xffffff);
        }
        return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatExtreme) & 0xffffff);
    }
}
