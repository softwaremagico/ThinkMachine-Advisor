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

package com.softwaremagico.tm.advisor.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.softwaremagico.tm.advisor.R;

public class LoadCharacter extends DialogFragment {
    private ViewGroup linearLayoutDetails;
    private ImageView imageViewExpand;
    private static final int DURATION = 250;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.character_description, container, false);
        linearLayoutDetails = rootView.findViewById(R.id.linearLayoutDetails);
        imageViewExpand = rootView.findViewById(R.id.imageViewExpand);

        Toolbar toolbar = rootView.findViewById(R.id.selector_toolbar);

        LinearLayout description = rootView.findViewById(R.id.description_layout);
        description.setOnClickListener(view -> toggleDetails(view));

        //setSupportActionBar(toolbar);

        Toolbar toolbarCard = rootView.findViewById(R.id.character_description);
        toolbarCard.setTitle("Title");
        toolbarCard.setSubtitle("Subtitle");
        //toolbarCard.inflateMenu(R.menu.menu_card);
        Fragment parent = this;
        toolbarCard.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_option1:
                       Snackbar
                                .make(rootView, "Text1", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.action_option2:
                        Snackbar
                                .make(rootView, "Text2", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.action_option3:
                        Snackbar
                                .make(rootView, "Text3", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        Button cancelButton = rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    public void toggleDetails(View view) {
        if (linearLayoutDetails.getVisibility() == View.GONE) {
            ExpandAndCollapseViewUtil.expand(linearLayoutDetails, DURATION);
            imageViewExpand.setImageResource(R.drawable.ic_more);
            rotate(-180.0f);
        } else {
            ExpandAndCollapseViewUtil.collapse(linearLayoutDetails, DURATION);
            imageViewExpand.setImageResource(R.drawable.ic_less);
            rotate(180.0f);
        }
    }

    private void rotate(float angle) {
        Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(DURATION);
        imageViewExpand.startAnimation(animation);
    }
}
