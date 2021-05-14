package com.softwaremagico.tm.advisor.ui.components.descriptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.races.Race;

import java.util.stream.Collectors;

public class ElementDescriptionDialog<T extends Element<T>> extends DialogFragment {
    protected static final String TABLE_STYLE = "border:1px solid black;margin-left:auto;margin-right:auto;font-size:60%";
    protected static final int TABLE_PADDING = 2;
    private final T element;

    public ElementDescriptionDialog(T element) {
        super();
        this.element = element;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.description_window, container, false);
        WebView content = root.findViewById(R.id.content);
        Button closeButton = root.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss());
        updateContent(content, element);
        return root;
    }

    private void updateContent(WebView content, T element) {
        content.loadData(setContent(element), "text/html", "UTF-8");
    }

    private String getHeader(T element) {
        return "<h2>" + element.getName() + "</h2>";
    }

    private String getBody(T element) {
        return "<p>" + element.getDescription() + "</p>";
    }

    private String getRestrictions(T element) {
        StringBuilder restrictions = new StringBuilder();
        if (element.getRestrictedToRaces() != null && !element.getRestrictedToRaces().isEmpty()) {
            restrictions.append("<p><b>").append(getString(R.string.restricted_races)).append("</b> ")
                    .append(element.getRestrictedToRaces().stream().map(Race::getName)
                            .collect(Collectors.joining(", "))).append("</p>");
        }
        if (element.getRestrictedToFactionGroup() != null) {
            restrictions.append("<p><b>").append(getContext().getResources().getIdentifier("preference_option_" +
                    element.getRestrictedToFactionGroup().name().toLowerCase(), "string", getContext().getPackageName()))
                    .append("</b> ").append(element.getRestrictedToFactionGroup()).append("</p>");
        }
        if (element.isRestricted()) {
            restrictions.append("<p><b><font color=\"" + getColor(R.color.restricted) + "\">").append(getString(R.string.restricted))
                    .append("</font></b></p>");
        }
        return restrictions.toString();
    }

    private String setContent(T element) {
        return "<html><body style='text-align:justify;font-size:14px;'>" +
                getHeader(element) +
                getBody(element) +
                getDetails(element) +
                getRestrictions(element) +
                "</body></html>";
    }

    protected String getDetails(T element) {
        return "";
    }

    protected String getColor(int color) {
        int labelColor = ContextCompat.getColor(getContext(), color);
        //Removing Alpha value
        return String.format("%X", labelColor).substring(2);
    }
}
