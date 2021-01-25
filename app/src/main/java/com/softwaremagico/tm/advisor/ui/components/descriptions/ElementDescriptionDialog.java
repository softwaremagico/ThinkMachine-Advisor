package com.softwaremagico.tm.advisor.ui.components.descriptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;

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

    private String setContent(T element) {
        return "<html><body style='text-align:justify;font-size:14px;'>" +
                getHeader(element) +
                getBody(element) +
                getDetails(element) +
                "</body></html>";
    }

    protected String getDetails(T element) {
        return "";
    }
}
