package com.sany.sanybrowser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder dload;
    ProgressBar progres;
    ArrayList<String> history=new ArrayList<String>();
    RelativeLayout lat;
    SearchView url;
    WebView web;
    Button back,forward,home,retry;
    ArrayAdapter adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        back=findViewById(R.id.back);
        forward=findViewById(R.id.forward);
        home=findViewById(R.id.home);
        retry=findViewById(R.id.retry);
        //url=findViewById(R.id.url2);
        String s=getIntent().getStringExtra("URL");
        progres=findViewById(R.id.progressBar);
        web=(WebView)findViewById(R.id.wb);
        progres.setVisibility(View.GONE);
        web.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                history.add(url);
                return false; // continue with loading of url!!
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                progres.setVisibility(View.VISIBLE);
                progres.setProgress(progress);
                if(progress == 100)
                {
                    progres.setVisibility(View.GONE);
                }
                else
                    progres.setVisibility(View.VISIBLE);
            }
        });

        WebSettings st=web.getSettings();
        st.setJavaScriptEnabled(true);
        web.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(final String url, final String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                dload=new AlertDialog.Builder(MainActivity.this);
                dload.setCancelable(false);
                dload.setTitle("Download");
                final String filename = URLUtil.guessFileName(url,contentDisposition,mimetype);
                dload.setMessage(filename);
                dload.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                        String cookie= CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("Cookie",cookie);
                        request.addRequestHeader("User-Agent",userAgent);

                        request.allowScanningByMediaScanner();

                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                        DownloadManager downloadManager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                        try {

                                Toast.makeText(getApplicationContext(),Environment.isExternalStorageEmulated()+"",Toast.LENGTH_SHORT).show();
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, filename);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                        downloadManager.enqueue(request);
                    }
                });
                dload.setNegativeButton("NO",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dialog.dismiss();
                    }
                });
                dload.create().show();
            }
        });
        if(s.contains("."))
        {
            if(s.contains("www."))
                s="https://"+s;
            else
                s="https://www."+s;
            web.loadUrl(s);
        }
        else
        { s="https://www.google.com/search?q="+s;

            web.loadUrl(s);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem searchbar=menu.findItem(R.id.searc);
        url=(SearchView)searchbar.getActionView();
        url.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        url.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String s;

                s=query;
                if(s.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter URL",Toast.LENGTH_SHORT).show();
                }
                else if(s.contains("."))
                {
                    if(s.contains("www."))
                        s="https://"+s;
                    else
                        s="https://www."+s;
                    web.loadUrl(s);
                }
                else
                { s="https://www.google.com/search?q="+s;

                    web.loadUrl(s);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.back:
                if(web.canGoBack())
                    web.goBack();
                break;
            case R.id.forward:
                if(web.canGoForward())
                    web.goForward();
                break;
            case R.id.home:
                Intent homepage=new Intent(MainActivity.this,HomePage.class);
                homepage.putExtra("stack",history);
                homepage.putExtra("fl",1);
                startActivity(homepage);
                break;
            case R.id.retry:
                web.reload();
                break;
        }
    }
    public void onBackPressed()
    {
        if(web.canGoBack())
        {
            web.goBack();
        }
        else
            super.onBackPressed();
    }
}