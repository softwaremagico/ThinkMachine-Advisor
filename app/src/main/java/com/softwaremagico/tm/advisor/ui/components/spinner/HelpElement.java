package com.softwaremagico.tm.advisor.ui.components.spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.ElementComponent;
import com.softwaremagico.tm.advisor.ui.components.descriptions.ArmourDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.BeneficeDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.BlessingDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.CyberneticDeviceDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.ElementDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.MeleeWeaponDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.OccultismDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.RangeWeaponDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.ShieldDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.occultism.OccultismPower;

public abstract class HelpElement<T extends Element<T>> extends ElementComponent<T> {
    private ImageView helpButton;

    public HelpElement(Context context) {
        this(context, null);
    }

    public HelpElement(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void initComponents(AttributeSet attrs) {
        final TextView tagText = findViewById(R.id.translated_tag);
        final TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.TranslatedEditText, 0, 0);
        final String tag = attributes.getString(R.styleable.TranslatedEditText_translation);
        if (tag != null) {
            tagText.setText(ThinkMachineTranslator.getTranslatedText(tag) + " ");
        }
        tagText.setTextAppearance(R.style.CharacterInfo);
        attributes.recycle();

        helpButton = findViewById(R.id.button_help);
        if (helpButton != null) {
            helpButton.setOnClickListener(v -> openDescriptionWindow(getSelection()));
        }
    }

    protected ImageView getHelpButton() {
        return helpButton;
    }

    public abstract T getSelection();

    protected void openDescriptionWindow(T element) {
        if (element != null) {
            if (element instanceof AvailableBenefice) {
                new BeneficeDescriptionDialog((AvailableBenefice) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            } else if (element instanceof Blessing) {
                new BlessingDescriptionDialog((Blessing) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            } else if (element instanceof Shield) {
                new ShieldDescriptionDialog((Shield) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            } else if (element instanceof Armour) {
                new ArmourDescriptionDialog((Armour) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            } else if (element instanceof Weapon) {
                if (((Weapon) element).isRangedWeapon()) {
                    new RangeWeaponDescriptionDialog((Weapon) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
                } else {
                    new MeleeWeaponDescriptionDialog((Weapon) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
                }
            } else if (element instanceof CyberneticDevice) {
                new CyberneticDeviceDescriptionDialog((CyberneticDevice) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            } else if (element instanceof OccultismPower) {
                new OccultismDescriptionDialog((OccultismPower) element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            } else {
                new ElementDescriptionDialog<>(element).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
            }
        }
    }
}
