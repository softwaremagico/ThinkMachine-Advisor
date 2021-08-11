package com.softwaremagico.tm.advisor.ui.components.spinner;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;

import java.util.ArrayList;
import java.util.List;

public class SearchableSpinner<E extends Element<E>> extends androidx.appcompat.widget.AppCompatSpinner implements View.OnTouchListener,
        SearchableListDialog.SearchableItem<E> {

    public static final int NO_ITEM_SELECTED = -1;
    private final Context context;
    private List<E> items;
    private SearchableListDialog<E> searchableListDialog;

    private boolean isDirty;
    private ArrayAdapter<E> arrayAdapter;
    private String hintText;
    private boolean isFromInit;

    public SearchableSpinner(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        for (int i = 0; i < attributes.getIndexCount(); ++i) {
            if (attributes.getIndex(i) == R.styleable.SearchableSpinner_hintText) {
                hintText = attributes.getString(attributes.getIndex(i));
            }
        }
        attributes.recycle();
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        items = new ArrayList<E>();
        searchableListDialog = SearchableListDialog.newInstance
                (items);
        searchableListDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);
        //searchableListDialog.setAdapter((ArrayAdapter<E>)getAdapter());

        arrayAdapter = (ArrayAdapter<E>) getAdapter();
        if (!TextUtils.isEmpty(hintText)) {
            ArrayAdapter<E> arrayAdapter = new ArrayAdapter(context, android.R.layout
                    .simple_list_item_1, new String[]{hintText});
            isFromInit = true;
            setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (searchableListDialog.isAdded()) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (null != arrayAdapter) {
                // Refresh content #6
                // Change Start
                // Description: The items were only set initially, not reloading the data in the
                // spinner every time it is loaded with items in the adapter.
                items.clear();
                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    items.add(arrayAdapter.getItem(i));
                }
                // Change end.
                searchableListDialog.show(scanForActivity(context).getFragmentManager(), "TAG");
                searchableListDialog.setAdapter((ArrayAdapter<E>) getAdapter());
            }
        }
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (!isFromInit) {
            arrayAdapter = (ArrayAdapter<E>) adapter;
            if (!TextUtils.isEmpty(hintText) && !isDirty) {
                ArrayAdapter<E> arrayAdapter = new ArrayAdapter(context, android.R.layout
                        .simple_list_item_1, new String[]{hintText});
                super.setAdapter(arrayAdapter);
            } else {
                super.setAdapter(adapter);
            }

        } else {
            isFromInit = false;
            super.setAdapter(adapter);
        }
    }

    public void notifyDataSetChanged() {
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchableItemClicked(E item, int position) {
        setSelection(items.indexOf(item));

        if (!isDirty) {
            isDirty = true;
            setAdapter(arrayAdapter);
            setSelection(items.indexOf(item));
        }
    }

    private Activity scanForActivity(Context cont) {
        if (cont == null) {
            return null;
        } else if (cont instanceof Activity) {
            return (Activity) cont;
        } else if (cont instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        }
        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(hintText) && !isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if (!TextUtils.isEmpty(hintText) && !isDirty) {
            return null;
        } else {
            return super.getSelectedItem();
        }
    }
}
