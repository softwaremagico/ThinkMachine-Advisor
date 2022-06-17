package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.Component;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculatorModificationHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FirebirdsCounter extends Component {
    private static final int DURATION = 1000;
    private ImageView coinImage;
    private TextView valueText;
    private float currentValue = 0;
    private CostCalculatorModificationHandler.ICurrentFirebirdSpendListener listener;
    private CostCalculatorModificationHandler.IInitialFirebirdsUpdated initialListener;
    private NumberFormat decimalFormat = new DecimalFormat("##.##");
    private boolean unitHidden = false;


    public FirebirdsCounter(Context context) {
        this(context, null);
    }

    public FirebirdsCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.firebirds_counter, this);
        initComponents(attrs);
        setColor();
    }

    protected void initComponents(AttributeSet attrs) {
        coinImage = findViewById(R.id.coin);
        valueText = findViewById(R.id.value);
    }

    public void setValue(float value, boolean animation) {
        if (isUnitHidden()) {
            valueText.setText(String.format("%s", decimalFormat.format(value)));
        } else {
            valueText.setText(String.format("%s %s", decimalFormat.format(value), getResources().getString(R.string.firebird_abbrev)));
        }
        if (animation) {
            if (currentValue != value) {
                rotate(45f * (float) (value - currentValue) / 50, coinImage);
            }
        }
        setCurrentValue(value);
        setColor();
    }

    protected float getCurrentValue() {
        return currentValue;
    }

    protected void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    private void setColor() {
        if (currentValue < 0) {
            valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.counterError));
            coinImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.counterError), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.counterFirebirdsText));
            coinImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.counterFirebirds), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }


    public void setCharacter(CharacterPlayer character) {
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().removeFirebirdSpendListeners(listener);
        listener = CharacterManager.getCostCalculator().getCostCharacterModificationHandler().addFirebirdSpendListeners(value -> {
            setValue((character.getInitialMoney() - CharacterManager.getCostCalculator().getFireBirdsExpend()), false);
        });
        CharacterManager.getCostCalculator().getCostCharacterModificationHandler().removeInitialFirebirdListeners(initialListener);
        initialListener = CharacterManager.getCostCalculator().getCostCharacterModificationHandler().addInitialFirebirdListeners(value -> {
            setValue((character.getInitialMoney() - CharacterManager.getCostCalculator().getFireBirdsExpend()), false);
        });
        setValue((character.getInitialMoney() - CharacterManager.getCostCalculator().getFireBirdsExpend()), false);
    }


    private void rotate(float angle, ImageView gearImage) {
        final Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(DURATION);
        gearImage.startAnimation(animation);
    }

    public boolean isUnitHidden() {
        return unitHidden;
    }

    public void setUnitHidden(boolean unitHidden) {
        this.unitHidden = unitHidden;
    }
}