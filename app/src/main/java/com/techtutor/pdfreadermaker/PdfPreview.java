package com.techtutor.pdfreadermaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;


import com.github.barteksc.pdfviewer.PDFView;

import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import com.github.barteksc.pdfviewer.util.FitPolicy;

import com.techtutor.pdfreadermaker.Design.CustormScroll;

import java.io.File;


public class PdfPreview extends AppCompatActivity  {
     PDFView pdfView;
    private Toolbar toolbar;
    String pdfFilename;
    int lastReadPageNumber;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);
        Intent intent=getIntent();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        path=intent.getStringExtra("pdf");
        String pdfname=intent.getStringExtra("name");


        pdfFilename=pdfname;
        getSupportActionBar().setTitle(pdfname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);
        pdfView=findViewById(R.id.pdfView);


        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        int lastReadPage = sharedPreferences.getInt("lastReadPage_" + pdfFilename, 0);

        displayPDF(path, lastReadPage);
    }

    private void displayPDF(String pdfFilePath, int lastReadPage) {
        // Load the PDF file from the file path

        pdfView.fromFile(new File(pdfFilePath))


                .defaultPage(lastReadPage)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)

                // allows to draw something on the current page, usually visible in the middle of the screen

                // allows to draw something on all pages, separately for every page. Called only for visible pages


                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                              lastReadPageNumber=page;
                    }
                })
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen

                .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .pageSnap(false) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(false)
                .scrollHandle(new CustormScroll(this))




                .load();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.pdfmenu,menu);
      return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                  if (item.getItemId()==R.id.sharePdf){
                        sharepdf();

                  }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("lastReadPage_" +pdfFilename, lastReadPageNumber);
        editor.apply();

    }

    void sharepdf(){

        File pdfFile = new File(path);
        Uri pdfFileUri = FileProvider.getUriForFile(this, "com.techtutor.pdfreadermaker.fileprovider", pdfFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdfFileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Send PDF via"));

    }


}