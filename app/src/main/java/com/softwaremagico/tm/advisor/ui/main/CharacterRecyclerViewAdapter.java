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

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.persistence.CharacterEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CharacterRecyclerViewAdapter extends RecyclerView
        .Adapter<CharacterRecyclerViewAdapter.CharacterEntityViewHolder> {

    private List<CharacterEntity> dataset;

    public CharacterRecyclerViewAdapter(ArrayList<CharacterEntity> data) {
        this.dataset = data;
    }

    /**
     * Represent each character entity with a chracter card.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CharacterEntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CharacterEntityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.character_card, parent, false));
    }

    @Override
    public void onBindViewHolder(CharacterEntityViewHolder holder, int position) {
        CharacterEntity characterEntity = dataset.get(position);
        holder.description.setTitle(characterEntity.getCharacterPlayer().getCompleteNameRepresentation());
        holder.description.setSubtitle(formatTimestamp(characterEntity.getUpdateTime()));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    private String formatTimestamp(Timestamp timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp.getTime());
        return DateFormat.format("yyyy-MM-dd hh:mm:ss", cal).toString();
    }


    static class CharacterEntityViewHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        private ViewGroup linearLayoutDetails;
        private ImageView imageViewExpand;
        private static final int DURATION = 250;

        Toolbar description;
        TextView updatedAt;
        TextView race;
        TextView faction;

        public CharacterEntityViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            description = (Toolbar) itemView.findViewById(R.id.character_description);

            //Card description
            linearLayoutDetails = itemView.findViewById(R.id.linearLayoutDetails);
            imageViewExpand = itemView.findViewById(R.id.imageViewExpand);

            //Toolbar toolbar = itemView.findViewById(R.id.selector_toolbar);

            LinearLayout description = itemView.findViewById(R.id.description_layout);
            description.setOnClickListener(view -> toggleDetails(view));

            //setSupportActionBar(toolbar);

            Toolbar toolbarCard = itemView.findViewById(R.id.character_description);
            toolbarCard.setTitle("Title");
            toolbarCard.setSubtitle("Subtitle");
            //toolbarCard.inflateMenu(R.menu.menu_card);
            toolbarCard.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_option1:
                            Snackbar
                                    .make(itemView, "Text1", Snackbar.LENGTH_SHORT).show();
                            break;
                        case R.id.action_option2:
                            Snackbar
                                    .make(itemView, "Text2", Snackbar.LENGTH_SHORT).show();
                            break;
                        case R.id.action_option3:
                            Snackbar
                                    .make(itemView, "Text3", Snackbar.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });

/*            Button cancelButton = itemView.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //itemView.dismiss();
                }
            });*/
        }

        @Override
        public void onClick(View v) {
            // myClickListener.onItemClick(getAdapterPosition(), v);
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

}