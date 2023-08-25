package com.techtutor.pdfreadermaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;



import android.net.Uri;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.github.barteksc.pdfviewer.PDFView;


import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.techtutor.pdfreadermaker.Design.CustormScroll;

import java.io.File;
import java.util.Map;


public class PdfPreview extends AppCompatActivity  {
     PDFView pdfView;
    private Toolbar toolbar;
    String pdfFilename;
    int lastReadPageNumber;
    String path;
    private boolean isToolbarVisible = true;
    private boolean isDarkModeEnabled = false;
    String filesize;
    String filedate;
    private boolean isBookmode=false;


    private FrameLayout adContainerView;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);
        Intent intent=getIntent();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adContainerView = findViewById(R.id.ad_view_container);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }

                // Start loading ads here...
                loadBanner();


            }
        });

        path=intent.getStringExtra("pdf");
        String pdfname=intent.getStringExtra("name");
        filedate=intent.getStringExtra("date");
        filesize=intent.getStringExtra("size");




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

                .onTap(new OnTapListener() {
                    @Override
                    public boolean onTap(MotionEvent e) {

                           if (isToolbarVisible){
                               getSupportActionBar().hide();
                               isToolbarVisible=false;
                           } else {
                                   // Show the toolbar
                                   getSupportActionBar().show();
                                   isToolbarVisible = true;
                               }
                        return false;
                    }
                })


                .load();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.pdfmenu,menu);
      return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
             if(item.getItemId()==R.id.darkmode){
                 isDarkModeEnabled = !isDarkModeEnabled;
                 applyDarkMode();
             }
             if(item.getItemId()==R.id.setting){
                 showBottomSheet();
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
        if (adView != null) {
            adView.pause();
        }


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

    void applyDarkMode(){

        if (isDarkModeEnabled) {
            // Apply dark mode settings to the PDF viewer
          pdfView.setNightMode(true);
          pdfView.loadPages();

            // You can adjust rendering settings for dark mode as needed
        } else {
            // Apply regular mode settings to the PDF viewer
           pdfView.setNightMode(false);
            pdfView.loadPages();
            // Reset any rendering settings for dark mode
        }


    }


    public void showBottomSheet(){
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(PdfPreview.this,R.style.BottomSheetDialogTheme);
        View bottomSheetView= LayoutInflater.from(this).inflate(R.layout.bottomshit_layout,(LinearLayout)findViewById(R.id.bottomshit_container));
        TextView title=bottomSheetView.findViewById(R.id.bottomshit_heading);
        title.setText(pdfFilename);
        TextView datetv=bottomSheetView.findViewById(R.id.bottomhshit_date);
        datetv.setText(filedate);
        TextView sizetv=bottomSheetView.findViewById(R.id.bottomshit_size);
        sizetv.setText(filesize);
        EditText pagnum=bottomSheetView.findViewById(R.id.gotopageEt);
        TextView gotopage=bottomSheetView.findViewById(R.id.gotopageBt);
        gotopage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagnum.getText().toString().isEmpty()){
                    pagnum.setError("Enter Number");
                }else{
                       int pageNumber=Integer.parseInt(pagnum.getText().toString());
                       pdfView.jumpTo(pageNumber);
                       bottomSheetDialog.dismiss();

                }
            }
        });

        LinearLayout share=bottomSheetView.findViewById(R.id.bottom_sharepdf);
        ImageView icon=bottomSheetView.findViewById(R.id.mode_icon);
        TextView modeName=bottomSheetView.findViewById(R.id.mode_name);

        if(isBookmode){
            icon.setImageResource(R.drawable.single_page);
            modeName.setText("Normal Mode");
        }else{


            icon.setImageResource(R.drawable.ic_baseline_menu_book_24);
            modeName.setText("Book Mode");

        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharepdf();
                bottomSheetDialog.dismiss();
            }
        });

        LinearLayout mode=bottomSheetView.findViewById(R.id.pagebybpage);
        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      isBookmode=!isBookmode;
                      if(isBookmode){
                          loadPdfFile(path,lastReadPageNumber);



                          bottomSheetDialog.dismiss();
                      }else{
                          displayPDF(path,lastReadPageNumber);

                          bottomSheetDialog.dismiss();
                      }



            }
        });


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }


     public void loadPdfFile(String pdfFilePath, int lastReadPage){

             pdfView.removeAllViews();

         pdfView.fromFile(new File(pdfFilePath))


                 .defaultPage(lastReadPage)
                 .enableSwipe(true) // allows to block changing pages using swipe
                 .swipeHorizontal(true)
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
                 .autoSpacing(true) // add dynamic spacing to fit each page on its own on the screen

                 .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                 .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                 .pageSnap(true) // snap pages to screen boundaries
                 .pageFling(true) // make a fling change only a single page like ViewPager
                 .nightMode(false)
                 .scrollHandle(new CustormScroll(this))

                 .onTap(new OnTapListener() {
                     @Override
                     public boolean onTap(MotionEvent e) {

                         if (isToolbarVisible){
                             getSupportActionBar().hide();
                             isToolbarVisible=false;
                         } else {
                             // Show the toolbar
                             getSupportActionBar().show();
                             isToolbarVisible = true;
                         }
                         return false;
                     }
                 })


                 .load();



     }



    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }


    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(getResources().getString(R.string.Admob_banner_ad_unit));
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }




}