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

package com.softwaremagico.tm.advisor.ui.visualization;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.TextVariablesManager;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.txt.CharacterSheet;

import java.io.IOException;

public class TextVisualizationFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static TextVisualizationFragment newInstance(int index) {
        TextVisualizationFragment fragment = new TextVisualizationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.visualization_text_fragment, container, false);

        TextView txtDetails = root.findViewById(R.id.character_text);
        final CharacterSheet characterSheet = new CharacterSheet(CharacterManager.getSelectedCharacter());
        txtDetails.setText(characterSheet.toString());
        txtDetails.setMovementMethod(new ScrollingMovementMethod());

        FloatingActionButton fab = root.findViewById(R.id.share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    shareText();
                } catch (IOException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            }
        });

        return root;
    }

    protected void shareText() throws IOException {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + (CharacterManager.getSelectedCharacter().getCompleteNameRepresentation().length() > 0 ?
                ": " + CharacterManager.getSelectedCharacter().getCompleteNameRepresentation() : ""));
        final CharacterSheet characterSheet = new CharacterSheet(CharacterManager.getSelectedCharacter());
        shareIntent.putExtra(Intent.EXTRA_TEXT, TextVariablesManager.replace(getString(R.string.share_body) + "\n\n" + characterSheet.toString()));
        Intent chooser = Intent.createChooser(shareIntent, "Share File");
        startActivity(shareIntent);
    }
}
