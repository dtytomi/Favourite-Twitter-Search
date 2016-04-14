package com.example.oo162802.favoritetwittersearches;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Arrays;

public class FavoriteTwitterSearches extends AppCompatActivity {

    private SharedPreferences savedSearches; // user's favorite searches
    private TableLayout queryTableLayout; // shows the search button
    private EditText queryEditText; //where the user enters query
    private EditText tagEditText; //where the user enters a query's tag


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        savedSearches = getSharedPreferences("Searches", MODE_PRIVATE);

        queryTableLayout = (TableLayout) findViewById(R.id.queryTableLayout);

        queryEditText = (EditText) findViewById(R.id.queryEditText);
        tagEditText = (EditText) findViewById(R.id.tagEditText);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);
        Button clearTagsButton = (Button) findViewById(R.id.clearTagButton);

        refreshButtons(null);

    }

    private void refreshButtons(String newTag) {

        String[] tags = savedSearches.getAll().keySet().toArray(new String[0]);

        if (newTag != null ) {
            makeTagGUI(newTag, Arrays.binarySearch(tags, newTag));
        }
        else {
            for (int index = 0; index < tags.length; ++index) {
                makeTagGUI(tags[index], index);
            }
        }
    }

    private  void makeTag( String query, String tag) {

        String originalQuery = savedSearches.getString(tag, null);

        SharedPreferences.Editor preferencesEditor =  savedSearches.edit();
        preferencesEditor.putString(tag, query);
        preferencesEditor.apply();

        if (originalQuery == null ) {
            refreshButtons(tag);
        }
    }

    private void makeTagGUI(String tag, int index){

        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newTagView = inflater.inflate(R.layout.new_tag_view, null);

        Button newTagButton = (Button) newTagView.findViewById(R.id.newTagButton);
        newTagButton.setText(tag);
        newTagButton.setOnClickListener(queryButtonListener);

        Button newEditButton = (Button) newTagView.findViewById(R.id.newEditButton);
        newEditButton.setOnClickListener(editButtonListener);

        queryTableLayout.addView(newTagView, index);
    }

    private void clearTags() {

        queryTableLayout.removeAllViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public OnClickListener saveButtonListener  =  new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (queryEditText.getText().length() > 0 && tagEditText.getText().length() > 0 ) {
                makeTag(queryEditText.getText().toString(), tagEditText.getText().toString());
                queryEditText.setText("");
                tagEditText.setText("");

                ((InputMethodManager) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE)).hideSoftInputFromWindow(
                        tagEditText.getWindowToken(),0);
            }
            else {

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(FavoriteTwitterSearches.this);
                builder.setTitle(R.string.missingTitle);

                builder.setPositiveButton(R.string.OK, null);

                builder.setMessage(R.string.missingMessages);

                AlertDialog errorDialog = builder.create();
                errorDialog.show();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
