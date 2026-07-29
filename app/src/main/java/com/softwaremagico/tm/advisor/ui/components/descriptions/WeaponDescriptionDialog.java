package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;

public abstract class WeaponDescriptionDialog extends ElementDescriptionDialog<Weapon> {

    public WeaponDescriptionDialog(Weapon element) {
        super(element);
    }

    protected String getDamage(WeaponDamage weaponDamage) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(weaponDamage.getDamageWithoutArea());
        if (!weaponDamage.getDamageWithoutArea().endsWith("d")) {
            stringBuilder.append("d");
        }
        if (weaponDamage.getAreaMeters() > 0) {
            stringBuilder.append(" ");
            stringBuilder.append(weaponDamage.getAreaMeters());
        }
        return stringBuilder.toString();
    }
}
