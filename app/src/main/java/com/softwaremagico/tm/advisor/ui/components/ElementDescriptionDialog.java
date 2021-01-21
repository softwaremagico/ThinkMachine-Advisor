package com.softwaremagico.tm.advisor.ui.components;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.equipment.shields.Shield;

public class ElementDescriptionDialog<T extends Element<?>> extends DialogFragment {
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
        Typeface typeface = ResourcesCompat.getFont(root.getContext(), R.font.dejavu_sans);

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

    private String setContent(T element) {
        StringBuilder content = new StringBuilder();
        content.append("<html><body style='text-align:justify;font-size:14px;'>");
        content.append(getHeader(element));
        content.append(getBody(element));
        content.append(getDetails(element));
        content.append("</body></html>");
        return content.toString();
    }

    private String getDetails(T element) {

        if (AvailableBenefice.class.isInstance(element)) {
            return "<b>" + getString(R.string.cost) + "</b> " + ((AvailableBenefice) element).getCost();
        }

        if (Shield.class.isInstance(element)) {
            Shield shield = (Shield) element;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<table cellpadding=\"8\" style=\"border:1px solid black;margin-left:auto;margin-right:auto;\">");
            stringBuilder.append("<tr>" +
                    "<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                    "<th>" + ThinkMachineTranslator.getTranslatedText("impactForce") + "</th>" +
                    "<th>" + ThinkMachineTranslator.getTranslatedText("shieldHits") + "</th>" +
                    "</tr>");
            stringBuilder.append("<tr>" +
                    "<td style=\"text-align:center\">" + shield.getTechLevel() + "</td>" +
                    "<td style=\"text-align:center\">" + shield.getImpact() + "/" + shield.getForce() + "</td>" +
                    "<td style=\"text-align:center\" >" + shield.getHits() + "</td>" +
                    "</tr>");
            stringBuilder.append("</table>");
            stringBuilder.append("<br><b>" + getString(R.string.cost) + "</b> " + ((Shield) element).getCost() + " " + ThinkMachineTranslator.getTranslatedText("firebirds"));
            return stringBuilder.toString();
        }
        return "";
    }
}
