package com.softwaremagico.tm.advisor.ui.components;

import androidx.fragment.app.Fragment;

public abstract class CustomFragment extends Fragment {
    // flag bit to determine whether the data is initialized
    private boolean isInitialized = false;

    public CustomFragment() {
        super();
    }

    @Override
    public void onResume() {
        super.onResume();
        lazyInitData();
    }

    /**
     * Lazy Loading Method
     */
    public void lazyInitData() {
        if (!isInitialized) {
            isInitialized = true;
            initData();
        }
    }

    /**
     * Method of loading data, implemented by subclasses
     */
    protected abstract void initData();

}
