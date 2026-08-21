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

package com.softwaremagico.tm.advisor.ui.load;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.persistence.CharacterEntity;
import com.softwaremagico.tm.advisor.persistence.CharacterHandler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadCharacter extends DialogFragment {

    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View rootView = inflater.inflate(R.layout.character_loader, container, false);

        Button closeButton = rootView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss());

        //RECYCLER
        RecyclerView mRecyclerView = rootView.findViewById(R.id.character_recycler_loader);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setVisibility(View.GONE);

        final ProgressBar progressBar = rootView.findViewById(R.id.character_loader_progress);
        progressBar.setVisibility(View.VISIBLE);

        //Reading every saved character from the database and deserializing its JSON payload is
        //potentially expensive (it grows with the number of saved characters), so it must not be
        //done on the UI thread or the dialog would visibly freeze while opening.
        final Context applicationContext = getContext() != null ? getContext().getApplicationContext() : null;
        backgroundExecutor.execute(() -> {
            List<CharacterEntity> characterEntities;
            try {
                characterEntities = CharacterHandler.getInstance().load(applicationContext);
            } catch (Exception e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
                characterEntities = Collections.emptyList();
            }
            final List<CharacterEntity> loadedCharacterEntities = characterEntities;
            if (getActivity() == null || !isAdded()) {
                return;
            }
            getActivity().runOnUiThread(() -> {
                if (!isAdded()) {
                    return;
                }
                //ADAPTER
                CharacterRecyclerViewAdapter mAdapter = new CharacterRecyclerViewAdapter(loadedCharacterEntities);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.addClosePopUpListener(this::dismiss);
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            });
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        backgroundExecutor.shutdownNow();
    }
}
