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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.persistence.CharacterEntity;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;

import java.util.ArrayList;
import java.util.Locale;

public class LoadCharacter extends DialogFragment {
    private RecyclerView mRecyclerView;
    private CharacterRecyclerViewAdapter mAdapter;
    private static final String LOG_TAG = "CardViewActivity";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View rootView = inflater.inflate(R.layout.character_loader, container, false);

        //RECYCLER
        mRecyclerView = rootView.findViewById(R.id.character_recycler_loader);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //ADAPTER
        mAdapter = new CharacterRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addClosePopUpListener(() -> dismiss());

        return rootView;
    }

    private ArrayList<CharacterEntity> getDataSet() {
        final ArrayList results = new ArrayList<CharacterEntity>();
        for (int index = 0; index < 3; index++) {
            try {
                final CharacterPlayer characterPlayer = new CharacterPlayer(Locale.getDefault().getLanguage(), PathManager.DEFAULT_MODULE_FOLDER);
                final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, DifficultLevelPreferences.HARD);
                randomizeCharacter.createCharacter();
                final CharacterEntity characterEntity = new CharacterEntity(characterPlayer);
                results.add(index, characterEntity);
            } catch (DuplicatedPreferenceException e) {
                e.printStackTrace();
            } catch (TooManyBlessingsException e) {
                e.printStackTrace();
            } catch (InvalidXmlElementException e) {
                e.printStackTrace();
            } catch (InvalidRandomElementSelectedException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
