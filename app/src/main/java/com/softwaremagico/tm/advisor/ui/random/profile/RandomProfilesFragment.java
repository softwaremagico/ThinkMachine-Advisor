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

package com.softwaremagico.tm.advisor.ui.random.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSelector;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CharacterProgressionStatus;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.predefined.profile.RandomProfile;
import com.softwaremagico.tm.random.predefined.profile.RandomProfileFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RandomProfilesFragment extends CharacterCustomFragment {
    private View root;
    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
    private final Set<ElementSelector<RandomProfile>> optionsAvailable = new HashSet<>();
    private final Map<String, Set<ElementSelector<RandomProfile>>> optionsAvailableByGroup = new HashMap<>();
    private FloatingActionButton randomButton;
    private View generationOverlay;
    private ProgressBar generationProgress;
    private volatile boolean generationInProgress;

    public static RandomProfilesFragment newInstance(int index) {
        final RandomProfilesFragment fragment = new RandomProfilesFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setCharacter(View root, CharacterPlayer character) {

    }

    @Override
    protected void initData() {
        final LinearLayout linearLayout = root.findViewById(R.id.profiles_container);
        if (linearLayout == null) {
            return;
        }
        linearLayout.removeAllViews();
        optionsAvailable.clear();
        optionsAvailableByGroup.clear();

        RandomProfileFactory.getInstance().getGroups(Locale.getDefault().getLanguage(),
                ModuleManager.DEFAULT_MODULE).stream().sorted().forEach(group -> {
            optionsAvailableByGroup.computeIfAbsent(group, g -> new HashSet<>());
            try {
                addSection(getContext().getResources().getString(getContext().getResources().getIdentifier(getOptionTranslation(group), "string",
                        getContext().getPackageName())), linearLayout);
            } catch (Exception e) {
                addSection(group, linearLayout);
            }
            RandomProfileFactory.getInstance().getByGroup(group).stream().sorted().forEach(randomProfile -> {
                ElementSelector<RandomProfile> randomProfileSelector = new ElementSelector<>(getContext(), randomProfile);
                randomProfileSelector.setOnCheckedChangeListener((compoundButton, checked) -> {
                    //Unselect all other radio buttons.
                    if (checked && !Objects.equals(group, "specials")) {
                        optionsAvailableByGroup.get(group).stream().filter(npcElementRadio -> !Objects.equals(npcElementRadio, randomProfileSelector)).
                                forEach(npcElementRadio -> npcElementRadio.setChecked(false));
                    }
                });
                linearLayout.addView(randomProfileSelector);
                optionsAvailable.add(randomProfileSelector);
                optionsAvailableByGroup.get(group).add(randomProfileSelector);
            });
            addSpace(linearLayout);
        });

        addFinalSpace(linearLayout);

        setCharacter(root, CharacterManager.getSelectedCharacter());
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        // Nothing yet.
    }

    private String getOptionTranslation(String groupName) {
        return "profile_" + groupName.toLowerCase() + "_section";
    }

    public static <T extends Enum<T>> T getInstance(final String value, final Class<T> enumClass) {
        return Enum.valueOf(enumClass, value);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.random_profiles_fragment, container, false);

        randomButton = root.findViewById(R.id.random);
        generationOverlay = root.findViewById(R.id.random_generation_overlay);
        generationProgress = root.findViewById(R.id.random_generation_progress);
        randomButton.setOnClickListener(view -> {
            if (generationInProgress) {
                return;
            }
            if (CharacterManager.getCostCalculator().getStatus().ordinal() > CharacterProgressionStatus.IN_PROGRESS.ordinal()) {
                SnackbarGenerator.getWarningMessage(root, R.string.message_random_character_already_finished,
                        R.string.action_new, action -> {
                            CharacterManager.addNewCharacter();
                            generateCharacter();
                        }).show();
            } else {
                generateCharacter();
            }
        });

        return root;
    }

    private void generateCharacter() {
        setGenerationInProgress(true);
        backgroundExecutor.execute(() -> {
            try {
                CharacterManager.randomizeCharacterUsingProfiles(getSelectedOptions());
                runOnUiThreadSafely(() -> {
                    setGenerationInProgress(false);
                    SnackbarGenerator.getInfoMessage(root, R.string.message_random_character_success).show();
                });
            } catch (InvalidXmlElementException | InvalidRandomElementSelectedException |
                     RestrictedElementException | UnofficialElementNotAllowedException e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
                runOnUiThreadSafely(() -> {
                    setGenerationInProgress(false);
                    SnackbarGenerator.getErrorMessage(root, R.string.message_random_character_error).show();
                });
            }
        });
    }

    private void setGenerationInProgress(boolean inProgress) {
        generationInProgress = inProgress;
        if (randomButton != null) {
            randomButton.setEnabled(!inProgress);
            randomButton.setAlpha(inProgress ? 0.55f : 1.0f);
        }
        if (generationOverlay != null) {
            generationOverlay.setVisibility(inProgress ? View.VISIBLE : View.GONE);
            generationOverlay.setClickable(inProgress);
        }
        if (generationProgress != null) {
            generationProgress.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        }
    }

    private void runOnUiThreadSafely(Runnable runnable) {
        final androidx.fragment.app.FragmentActivity activity = getActivity();
        if (activity == null || !isAdded()) {
            return;
        }
        activity.runOnUiThread(() -> {
            if (isAdded()) {
                runnable.run();
            }
        });
    }

    @Override
    public void onDestroy() {
        backgroundExecutor.shutdownNow();
        super.onDestroy();
    }

    public Set<RandomProfile> getSelectedOptions() {
        return optionsAvailable.stream().filter(ElementSelector::isChecked).map(ElementSelector::getSelection).collect(Collectors.toSet());
    }


}
