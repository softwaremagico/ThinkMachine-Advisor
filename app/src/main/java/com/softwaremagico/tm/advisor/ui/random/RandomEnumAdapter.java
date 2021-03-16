package com.softwaremagico.tm.advisor.ui.random;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.EnumAdapter;

import java.util.List;

public class RandomEnumAdapter<T> extends EnumAdapter<T> {

    public RandomEnumAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.element_list, parent, false);
        }

        final Object element = getElements().get(position);

        if (element != null) {
            final TextView name = listItem.findViewById(R.id.selected_item);
            if (element instanceof Enum) {
                try {
                    name.setText(getContext().getResources().getString(getContext().getResources().getIdentifier(getOptionTranslation((Enum) element),
                            "string", getContext().getPackageName())));
                } catch (Resources.NotFoundException e) {
                    AdvisorLog.warning(this.getClass().getName(), "No translation found for '" + getOptionTranslation((Enum) element) + "'.");
                }
            }
        }

        return listItem;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.element_list, parent, false);
        }

        final Object element = getElements().get(position);

        if (element != null) {
            final TextView elementName = listItem.findViewById(R.id.selected_item);
            if (element instanceof Enum) {
                try {
                    elementName.setText(getContext().getResources().getString(getContext().getResources().getIdentifier(getOptionTranslation((Enum) element),
                            "string", getContext().getPackageName())));
                } catch (Resources.NotFoundException e) {
                    AdvisorLog.warning(this.getClass().getName(), "No translation found for '" + getOptionTranslation((Enum) element) + "'.");
                }
            }
        }

        return listItem;
    }

    private String getOptionTranslation(Enum element) {
        return "preference_option_" + element.name().toLowerCase();
    }
}
