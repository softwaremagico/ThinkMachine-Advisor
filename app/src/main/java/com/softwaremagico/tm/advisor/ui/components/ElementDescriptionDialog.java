package com.softwaremagico.tm.advisor.ui.components;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;

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
        TextView content = root.findViewById(R.id.content);
        Typeface typeface = ResourcesCompat.getFont(root.getContext(), R.font.dejavu_sans);
        content.setTypeface(typeface);
        content.setMovementMethod(LinkMovementMethod.getInstance());

        updateContent(content, element);
        return root;
    }

    private void updateContent(TextView content, T element) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            content.setText(Html.fromHtml(setContent(element), Html.FROM_HTML_MODE_LEGACY));
        } else {
            content.setText(Html.fromHtml(setContent(element)));
        }
    }

    private String getHeader(T element) {
        return "<h2>" + element.getName() + "<h2>";
    }

    private String getBody(T element) {
        return "<p>" + element.getDescription() + "</p>";
    }

    private String setContent(T element) {
        StringBuilder content = new StringBuilder();
        content.append(getHeader(element));
        content.append(getBody(element));
        content.append(getDetails(element));
        return content.toString();
    }

    private String getDetails(T element) {
        if (AvailableBenefice.class.isInstance(element)) {
            return "<b>" + getString(R.string.cost) + "</b> " + ((AvailableBenefice) element).getCost();
        }
        return "";
    }
}
