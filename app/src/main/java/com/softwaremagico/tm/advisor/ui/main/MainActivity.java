/*
 *  Copyright (C) 2020 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.advisor.CharacterManager;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.persistence.CharacterHandler;
import com.softwaremagico.tm.file.modules.ModuleLoaderEnforcer;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.MachineLog;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        //Preload all data in a seconday thread.
        new Thread(() -> {
            try {
                ModuleLoaderEnforcer.loadAllFactories(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE);
            } catch (
                    InvalidXmlElementException e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
            }
        }).run();

        super.onCreate(savedInstanceState);

        setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_sheet)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        View parentLayout = findViewById(android.R.id.content);
        switch (menuItem.getItemId()) {
            case R.id.settings_load:
                showDialog();
                return true;
            case R.id.settings_save:
                saveCurrentCharacter(parentLayout);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void saveCurrentCharacter(View parentLayout) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CharacterHandler.getInstance().save(getApplicationContext(), CharacterManager.getSelectedCharacter());
                    Snackbar
                            .make(parentLayout, R.string.message_character_saved_successfully, Snackbar.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Snackbar
                            .make(parentLayout, R.string.message_character_saved_error, Snackbar.LENGTH_SHORT).show();
                    MachineLog.errorMessage(this.getClass().getName(), e);
                }
            }
        });
    }

/*    private void loadCharacterList(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.character_description, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

    }*/

    public void showDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        LoadCharacter loadCharacter = new LoadCharacter();

        boolean isLargeLayout = true;

        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            loadCharacter.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, loadCharacter)
                    .addToBackStack(null).commit();
        }
    }

}