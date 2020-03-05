package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;

import java.util.List;

public class EnumAdapter<T extends Enum> extends ArrayAdapter<T> {
    private List<T> elements;

    public EnumAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        this.elements = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.element_list, parent, false);
        }

        Enum element = elements.get(position);

        if (element != null) {
            TextView name = (TextView) listItem.findViewById(R.id.selected_item);
            name.setText(ThinkMachineTranslator.getTranslatedText(element.name().toLowerCase()));
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

        Enum element = elements.get(position);

        if (element != null) {
            TextView elementName = (TextView) listItem.findViewById(R.id.selected_item);
            elementName.setText(ThinkMachineTranslator.getTranslatedText(element.name().toLowerCase()));
        }

        return listItem;
    }

    public int indexOf(T element){
        return elements.indexOf(element);
    }


}