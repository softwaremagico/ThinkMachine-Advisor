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
import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.TextVariablesManager;
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

        TextView txtDetails = (TextView) root.findViewById(R.id.character_text);
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
