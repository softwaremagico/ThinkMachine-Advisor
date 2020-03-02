package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;

import java.util.ArrayList;
import java.util.List;

public class ElementAdapter<T extends Element<?>> extends ArrayAdapter<T> {
    protected static final String LANGUAGE_PREFIX = "info";
    private List<T> elements;
    private String tag;

    public ElementAdapter(@NonNull Context context, int resource, @NonNull List<T> objects, String tag) {
        super(context, resource, objects);
        this.elements = objects;
        this.tag = tag;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.element_list, parent, false);
        }

        Element element = elements.get(position);

        if (element != null) {
            TextView name = (TextView) listItem.findViewById(R.id.selected_item);
            name.setText(element.getName());
        }

        return listItem;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.element_list, parent, false);
        }

        Element element = elements.get(position);

        if (element != null) {
            if (tag != null) {
                //Get tag directly from ThinkMachine translations.
                TextView nameTag = (TextView) listItem.findViewById(R.id.selected_item_tag);
                nameTag.setText(ThinkMachineTranslator.getTranslatedText(
                        LANGUAGE_PREFIX + tag.substring(0, 1).toUpperCase() + tag.substring(1)) + " ");
            }

            TextView name = (TextView) listItem.findViewById(R.id.selected_item);
            name.setText(element.getName());

        }

        return listItem;
    }


}
