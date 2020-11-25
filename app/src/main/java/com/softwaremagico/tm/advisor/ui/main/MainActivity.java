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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.core.FileUtils;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.persistence.CharacterHandler;
import com.softwaremagico.tm.advisor.ui.load.LoadCharacter;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.TextVariablesManager;
import com.softwaremagico.tm.file.modules.ModuleLoaderEnforcer;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.json.InvalidJsonException;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.log.MachineLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_EXPORT_EXTENSION = "tma";
    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        Translator.setLanguage(Locale.getDefault().getLanguage());
        //Preload all data in a secondary thread.
        new Thread(() -> {
            ModuleLoaderEnforcer.loadAllFactories(Locale.getDefault().getLanguage(), ModuleManager.DEFAULT_MODULE);
        }).start();

        super.onCreate(savedInstanceState);

        setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        final AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_sheet)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        final View parentLayout = findViewById(android.R.id.content);
        switch (menuItem.getItemId()) {
            case R.id.settings_load:
                showDialog();
                return true;
            case R.id.settings_save:
                saveCurrentCharacter(parentLayout);
                return true;
            case R.id.settings_new:
                newCharacter();
                return true;
            case R.id.settings_export_file:
                try {
                    exportJson(parentLayout);
                } catch (IOException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
                return true;
            case R.id.settings_import_file:
                importJson(parentLayout);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void importJson(View parentLayout) {

    }

    // Request code for selecting a PDF document.
    private static final int PICK_TMA_FILE = 0;

    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        // Provide read access to files and sub-directories in the user-selected
        // directory.
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_TMA_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_TMA_FILE
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    CharacterManager.setSelectedCharacter(CharacterJsonManager.fromJson(FileUtils.readFile(uri)));
                } catch (InvalidJsonException e) {
                    SnackbarGenerator.getErrorMessage(this.getCurrentFocus(), R.string.invalid_json_file).show();
                }
            }
        }
    }


    private void exportJson(View view) throws IOException {
        final File exportsPath = new File(view.getContext().getCacheDir(), "export");
        File characterExport = new File(exportsPath, CharacterManager.getSelectedCharacter().getCompleteNameRepresentation().length() > 0 ?
                CharacterManager.getSelectedCharacter().getCompleteNameRepresentation() + "_sheet." + FILE_EXPORT_EXTENSION :
                "export_sheet." + FILE_EXPORT_EXTENSION);
        final Uri contentUri = FileProvider.getUriForFile(view.getContext(), "com.softwaremagico.tm.advisor", characterExport);

        if (contentUri != null) {
            exportsPath.mkdir();
            String jsonContent = CharacterJsonManager.toJson(CharacterManager.getSelectedCharacter());
            FileOutputStream stream = new FileOutputStream(characterExport);
            try {
                stream.write(jsonContent.getBytes());
            } finally {
                stream.close();
            }

            final Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setType(this.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + (CharacterManager.getSelectedCharacter().getCompleteNameRepresentation().length() > 0 ?
                    ": " + CharacterManager.getSelectedCharacter().getCompleteNameRepresentation() : ""));
            shareIntent.putExtra(Intent.EXTRA_TEXT, TextVariablesManager.replace(getString(R.string.export_body)));

            final Intent chooser = Intent.createChooser(shareIntent, "Share File");
            final List<ResolveInfo> resInfoList = view.getContext().getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
            for (final ResolveInfo resolveInfo : resInfoList) {
                final String packageName = resolveInfo.activityInfo.packageName;
                view.getContext().grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(chooser);
        }
    }

    private void saveCurrentCharacter(View parentLayout) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CharacterHandler.getInstance().save(getApplicationContext(), CharacterManager.getSelectedCharacter());
                    SnackbarGenerator.getInfoMessage(parentLayout, R.string.message_character_saved_successfully).show();
                } catch (Exception e) {
                    SnackbarGenerator.getErrorMessage(parentLayout, R.string.message_character_saved_error).show();
                    MachineLog.errorMessage(this.getClass().getName(), e);
                }
            }
        });
    }

    private void newCharacter() {
        CharacterManager.addNewCharacter();
    }

    private void showDialog() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final LoadCharacter loadCharacter = new LoadCharacter();

        final boolean isLargeLayout = true;

        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            loadCharacter.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, loadCharacter)
                    .addToBackStack(null).commit();
        }
    }
}
