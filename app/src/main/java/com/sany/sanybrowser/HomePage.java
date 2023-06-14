package com.sany.sanybrowser;

import static com.sany.sanybrowser.R.layout.activity_home_page;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    AlertDialog.Builder exit;
    Intent main;
    SearchView url;
    ArrayList<String> history;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home_page);
        url = findViewById(R.id.url1);
        url.setIconifiedByDefault(false);
        url.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                Toast.makeText(getApplicationContext(),s+"",Toast.LENGTH_SHORT).show();
                return false;
            }

            public boolean onQueryTextSubmit(String s) {
                if (s == "")
                    Toast.makeText(getApplicationContext(), "Enter URL", Toast.LENGTH_SHORT).show();
                else {
                    main = new Intent(HomePage.this, MainActivity.class);
                    main.putExtra("URL", s);
                    startActivity(main);
                }
                return false;
            }
        });
        //history = getIntent().getStringArrayListExtra("stack");
    }

    @Override
    public void onBackPressed() {
        // TODO: Implement this method
        exit = new AlertDialog.Builder(this);
        exit.setCancelable(false);
        exit.setTitle("Alert!");
        exit.setMessage("Are You sure you want to exit");
        exit.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        exit.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        exit.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hist:
                //Intent direct=new Intent(HomePage.this,History.class);
                //direct.putExtra("stack",history);
                //startActivity(direct);
                break;
            case R.id.download:
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                break;
            case R.id.exitm:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}