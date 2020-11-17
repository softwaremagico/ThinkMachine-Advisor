package com.softwaremagico.tm.advisor.core;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.creation.CharacterProgressionStatus;

public class CharacterStatusHandler {

    public static int translateStatus(CharacterProgressionStatus status) {
        switch (status) {
            case DRAFT:
                return R.string.character_status_draft;
            case NOT_STARTED:
                return R.string.character_status_not_started;
            case IN_PROGRESS:
                return R.string.character_status_in_progress;
            case FINISHED:
                return R.string.character_status_finished;
            case EXTENDED:
                return R.string.character_status_extended;
            case EQUIPPED:
                return R.string.character_status_equipped;
            case UNDEFINED:
                return R.string.character_status_undefined;
        }
        return R.string.character_status_undefined;
    }

    public static String getStatusColor(Context context, CharacterProgressionStatus status) {
        switch (status) {
            case DRAFT:
                return String.format("#%06x", ContextCompat.getColor(context, R.color.statusDraft) & 0xffffff);
            case NOT_STARTED:
                return String.format("#%06x", ContextCompat.getColor(context, R.color.statusNotStarted) & 0xffffff);
            case IN_PROGRESS:
                return String.format("#%06x", ContextCompat.getColor(context, R.color.statusInProgress) & 0xffffff);
            case FINISHED:
                return String.format("#%06x", ContextCompat.getColor(context, R.color.statusFinished) & 0xffffff);
            case EXTENDED:
                return String.format("#%06x", ContextCompat.getColor(context, R.color.statusExtended) & 0xffffff);
            case EQUIPPED:
                return String.format("#%06x", ContextCompat.getColor(context, R.color.statusEquipped) & 0xffffff);
            case UNDEFINED:
                return String.format("#%06x", ContextCompat.getColor(context, R.color.statusUndefined) & 0xffffff);
        }
        return String.format("#%06x", ContextCompat.getColor(context, R.color.statusUndefined) & 0xffffff);
    }
}
