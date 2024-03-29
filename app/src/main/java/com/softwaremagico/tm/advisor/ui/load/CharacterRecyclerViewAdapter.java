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

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.core.DateUtils;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.persistence.CharacterEntity;
import com.softwaremagico.tm.advisor.persistence.CharacterHandler;
import com.softwaremagico.tm.advisor.ui.animations.ExpandAndCollapseViewUtil;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.txt.CharacterSheet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterRecyclerViewAdapter extends RecyclerView
        .Adapter<CharacterRecyclerViewAdapter.CharacterEntityViewHolder> {

    private final List<CharacterEntity> dataSet;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private final Map<CharacterEntity, String> charactersDescriptions;
    private ClosePopUpListener closePopUpListener;

    public interface ClosePopUpListener {
        void dismiss();
    }

    public CharacterRecyclerViewAdapter(List<CharacterEntity> data) {
        this.dataSet = data;
        charactersDescriptions = new HashMap<>();
    }

    /**
     * Represent each character entity with a character card.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CharacterEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharacterEntityViewHolder(this, LayoutInflater.from(parent.getContext()).inflate(R.layout.character_card, parent, false));
    }

    @Override
    public void onBindViewHolder(CharacterEntityViewHolder holder, int position) {
        holder.cardView.setSelected(selectedPosition == position);
        holder.update(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public CharacterEntity getSelectedItem() {
        return selectedPosition == RecyclerView.NO_POSITION ? null : dataSet.get(selectedPosition);
    }

    public void addClosePopUpListener(ClosePopUpListener listener) {
        closePopUpListener = listener;
    }

    class CharacterEntityViewHolder extends RecyclerView.ViewHolder {
        private static final int DURATION = 250;

        private final ImageView imageViewExpand;
        private final ViewGroup detailLayout;
        private CharacterEntity characterEntity;
        private final View cardView;
        private final Toolbar characterTitle;
        private TextView characterPlayer;
        private TextView completeDescription;
        private final TextView sortDescription;
        private final RecyclerView.Adapter adapter;

        @SuppressLint("NonConstantResourceId")
        public CharacterEntityViewHolder(RecyclerView.Adapter adapter, View cardView) {
            super(cardView);
            this.cardView = cardView;
            this.adapter = adapter;
            completeDescription = cardView.findViewById(R.id.character_description_skills);
            sortDescription = cardView.findViewById(R.id.short_description);
            characterTitle = cardView.findViewById(R.id.character_title);
            characterPlayer = cardView.findViewById(R.id.character_player);
            detailLayout = cardView.findViewById(R.id.details_layout);
            imageViewExpand = cardView.findViewById(R.id.image_view_expand);
            imageViewExpand.setImageResource(R.drawable.ic_more);

            imageViewExpand.setOnClickListener(this::toggleDetails);

            characterTitle.inflateMenu(R.menu.character_selector_menu);
            characterTitle.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.character_load:
                        CharacterManager.setSelectedCharacter(characterEntity.getCharacterPlayer());
                        if (closePopUpListener != null) {
                            closePopUpListener.dismiss();
                        }
                        break;
                    case R.id.character_copy:
                        duplicate(characterEntity);
                        break;
                    case R.id.character_delete:
                        delete(characterEntity);
                        break;
                    default:
                        break;
                }
                return true;
            });
        }

        private void delete(final CharacterEntity characterEntity) {
            try {
                CharacterHandler.getInstance().delete(cardView.getContext(), characterEntity);
                int index = dataSet.indexOf(characterEntity);
                SnackbarGenerator.getInfoMessage(cardView, R.string.character_deleted_correctly, R.string.undo, v -> {
                    dataSet.add(index, characterEntity);
                    adapter.notifyDataSetChanged();
                }).show();
                dataSet.remove(characterEntity);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
                SnackbarGenerator.getErrorMessage(cardView, R.string.error_deleting_character).show();
            }
        }

        private void duplicate(CharacterEntity characterEntity) {
            try {
                CharacterEntity duplicatedCharacterEntity = new CharacterEntity(characterEntity.getCharacterPlayer().duplicate());
                CharacterHandler.getInstance().save(cardView.getContext(), duplicatedCharacterEntity);
                SnackbarGenerator.getInfoMessage(cardView, R.string.message_duplication_ok).show();
                dataSet.add(0, duplicatedCharacterEntity);
                adapter.notifyDataSetChanged();
            } catch (InvalidGeneratedCharacter invalidGeneratedCharacter) {
                SnackbarGenerator.getErrorMessage(cardView, R.string.error_duplicating_character).show();
            }
        }

        public void update(CharacterEntity characterEntity) {
            this.characterEntity = characterEntity;
            characterTitle.setTitle(characterEntity.getCharacterPlayer().getCompleteNameRepresentation());
            characterTitle.setSubtitle(DateUtils.formatTimestamp(characterEntity.getUpdateTime()));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sortDescription.setText(Html.fromHtml(createStatusText(characterEntity), Html.FROM_HTML_MODE_LEGACY));
            } else {
                sortDescription.setText(Html.fromHtml(createStatusText(characterEntity)));
            }
            if (characterEntity.getCharacterPlayer() != null) {
                final ImageView factionImageView = cardView.findViewById(R.id.image_view_faction);
                factionImageView.setMaxWidth(175);
                factionImageView.setMaxHeight(175);
                factionImageView.setImageResource(FactionLogoSelection.getLogo(cardView.getContext(), characterEntity.getCharacterPlayer().getFaction()));
            }
            if (characterEntity.getPlayer() == null || characterEntity.getPlayer().isEmpty()) {
                characterPlayer.setVisibility(View.GONE);
            } else {
                characterPlayer.setText(characterEntity.getPlayer());
                characterPlayer.setVisibility(View.VISIBLE);
            }
        }

        private String createStatusText(CharacterEntity characterEntity) {
            final CostCalculator costCalculator = new CostCalculator(characterEntity.getCharacterPlayer());
            final StringBuilder stringBuilder = new StringBuilder(100);
            if (characterEntity.getCharacterPlayer().getFaction() != null) {
                stringBuilder.append(characterEntity.getCharacterPlayer().getFaction().getName());
            }
            if (characterEntity.getCharacterPlayer().getRace() != null) {
                stringBuilder.append(" (").append(characterEntity.getCharacterPlayer().getRace().getName()).append(")");
            }
            stringBuilder.append("<br>");
            //Status label.
            stringBuilder.append(itemView.getContext().getString(R.string.character_progression_status)).append(" <font color=\"").append(CharacterStatusHandler.getStatusColor(cardView.getContext(), costCalculator.getStatus()))
                    .append("\"><b>").append(itemView.getContext().getString(CharacterStatusHandler.translateStatus(costCalculator.getStatus()))).append("</b></font><br>");
            //Threat
            final ThreatLevelHandler threatLevelHandler = new ThreatLevelHandler(characterEntity.getThreat());
            stringBuilder.append(itemView.getContext().getString(R.string.character_threat)).append(" <font color=\"").append(threatLevelHandler.getColor(cardView.getContext())).append("\"><b>")
                    .append(threatLevelHandler.getThreatLevel()).append("</b>");
            return stringBuilder.toString();
        }


        public void cardClick(View view) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                return;
            }
            notifyItemChanged(selectedPosition);
            selectedPosition = getLayoutPosition();
            notifyItemChanged(selectedPosition);
        }

        public void toggleDetails(View view) {
            completeDescription = cardView.findViewById(R.id.character_description_skills);
            if (detailLayout.getVisibility() == View.GONE) {
                completeDescription.setText(getExtendedDescription(characterEntity));
                ExpandAndCollapseViewUtil.expand(detailLayout, DURATION);
                imageViewExpand.setImageResource(R.drawable.ic_more);
                rotate(-180.0f);
            } else {
                ExpandAndCollapseViewUtil.collapse(detailLayout, DURATION);
                imageViewExpand.setImageResource(R.drawable.ic_less);
                rotate(180.0f);
            }
        }

        private String getExtendedDescription(CharacterEntity characterEntity) {
            if (charactersDescriptions.get(characterEntity) == null) {
                final CharacterSheet characterSheet = new CharacterSheet(characterEntity.getCharacterPlayer());
                CharacterJsonManager.toJson(characterEntity.getCharacterPlayer());
                charactersDescriptions.put(characterEntity, characterSheet.toString());
            }
            return charactersDescriptions.get(characterEntity);
        }

        private void rotate(float angle) {
            final Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setFillAfter(true);
            animation.setDuration(DURATION);
            imageViewExpand.startAnimation(animation);
        }
    }

}
