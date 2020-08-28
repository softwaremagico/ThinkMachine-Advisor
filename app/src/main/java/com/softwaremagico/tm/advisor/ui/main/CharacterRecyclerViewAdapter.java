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

import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.core.DateUtils;
import com.softwaremagico.tm.advisor.persistence.CharacterEntity;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.txt.CharacterSheet;

import java.util.ArrayList;
import java.util.List;

public class CharacterRecyclerViewAdapter extends RecyclerView
        .Adapter<CharacterRecyclerViewAdapter.CharacterEntityViewHolder> {

    private List<CharacterEntity> dataset;
    private int selectedPosition = RecyclerView.NO_POSITION;

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
    public CharacterEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharacterEntityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.character_card, parent, false));
    }

    @Override
    public void onBindViewHolder(CharacterEntityViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPosition == position);
        CharacterEntity characterEntity = dataset.get(position);
        holder.update(characterEntity);

        // Here the selection style.
        ((CardView) holder.itemView).setCardElevation(selectedPosition == position ? 30 : 4);
        holder.itemView.setBackgroundResource(selectedPosition == position ? R.color.colorSelected : Color.TRANSPARENT);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public CharacterEntity getSelectedItem() {
        return selectedPosition == RecyclerView.NO_POSITION ? null : dataset.get(selectedPosition);
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }


    class CharacterEntityViewHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        private ViewGroup linearLayoutDetails;
        private ImageView imageViewExpand;
        private static final int DURATION = 250;

        CharacterEntity characterEntity;
        View itemView;
        Toolbar description;
        TextView completeDescription;
        TextView race;
        TextView faction;

        public CharacterEntityViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            description = itemView.findViewById(R.id.character_description);
            completeDescription = itemView.findViewById(R.id.character_description_skills);

            //Card description
            linearLayoutDetails = itemView.findViewById(R.id.linearLayoutDetails);
            imageViewExpand = itemView.findViewById(R.id.imageViewExpand);

            LinearLayout description = itemView.findViewById(R.id.description_layout);
            description.setOnClickListener(view -> toggleDetails(view));

            //Toolbar toolbar = itemView.findViewById(R.id.selector_toolbar);
            //setSupportActionBar(toolbar);

            Toolbar toolbarCard = itemView.findViewById(R.id.character_description);
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
        }

        public void update(CharacterEntity characterEntity) {
            this.characterEntity = characterEntity;
            description.setTitle(characterEntity.getCharacterPlayer().getCompleteNameRepresentation());
            description.setSubtitle(DateUtils.formatTimestamp(characterEntity.getUpdateTime()));
        }

        @Override
        public void onClick(View view) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            notifyItemChanged(selectedPosition);
            selectedPosition = getLayoutPosition();
            itemView.setSelected(selectedPosition == getLayoutPosition());
            notifyItemChanged(selectedPosition);
        }

        public void toggleDetails(View view) {
            TextView completeDescription = itemView.findViewById(R.id.character_description_skills);
            if (linearLayoutDetails.getVisibility() == View.GONE) {
                //No sheet set yet, default text loading...
                if (completeDescription.getText().length() < 50) {
                    final CharacterSheet characterSheet = new CharacterSheet(characterEntity.getCharacterPlayer());
                    CharacterJsonManager.toJson(characterEntity.getCharacterPlayer());
                    completeDescription.setText(characterSheet.toString());
                }
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
