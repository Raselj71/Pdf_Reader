package com.techtutor.pdfreadermaker;


import static android.content.ContentValues.TAG;
import static com.techtutor.pdfreadermaker.MyApp.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;


import androidx.core.content.FileProvider;

import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.AlertDialog;

import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.techtutor.pdfreadermaker.Adapter.PdfData;
import com.techtutor.pdfreadermaker.Fragment.AllPdfFile;
import com.techtutor.pdfreadermaker.Permission.Utils;

import com.techtutor.pdfreadermaker.RoomDatabase.BookMarkPdf;
import com.techtutor.pdfreadermaker.RoomDatabase.RecentPdf;
import com.techtutor.pdfreadermaker.ViewpagerAdapter.ViewPagerAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.util.Collections;
import java.util.Date;

import java.util.List;
import java.util.Locale;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {




    public static List<PdfData> pdfList;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

     private SearchView searchView;

    private BillingClient billingClient;
    private PurchasesUpdatedListener purchasesUpdatedListener;
    private ImageView premiumImage;

    public ViewPagerAdapter adapter;
    private ImageView morebutton;

    ReviewInfo reviewInfo;
    ReviewManager reviewManager;

    private ConsentInformation consentInformation;
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        premiumImage=findViewById(R.id.premium);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewpager);
        morebutton=findViewById(R.id.more);

        loadPermission();




       // db= Room.databaseBuilder(getApplicationContext(),DatabaseForPDF.class,"PdfFile").build();


        //  getAllPdfFile(Environment.getExternalStorageDirectory());

        requestReviewInfo();


        purchasesUpdatedListener=new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

            }
        };

        billingClient=BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases().build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if(billingResult.getResponseCode()== BillingClient.BillingResponseCode.OK){
                    // BillingClient is ready
                    // Now you can query SKU details and check for existing purchases


                }else{

                }

            }
        });


        premiumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view=getLayoutInflater().inflate(R.layout.premium_dialog_layout,null);
                Dialog dialog=new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                dialog.setContentView(view);
                dialog.show();

                ImageView imageView=view.findViewById(R.id.premium_cancel_button);
                Button button=view.findViewById(R.id.premium_button);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"Remove button is click",Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        morebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(MainActivity.this,R.style.BottomSheetDialogTheme);
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.homelayout,(LinearLayout)findViewById(R.id.moreContiner));
                LinearLayout contact,share,rate,policy;

                contact=view.findViewById(R.id.contact_us);
                share=view.findViewById(R.id.share_app);
                rate=view.findViewById(R.id.rate_app);
                policy=view.findViewById(R.id.policy);


                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        contactus();
                        bottomSheetDialog.dismiss();
                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareap();
                        bottomSheetDialog.dismiss();
                    }
                });

                rate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rateus();
                        bottomSheetDialog.dismiss();
                    }
                });

                policy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        policy();
                        bottomSheetDialog.dismiss();
                    }
                });




                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();


            }
        });




    }

    private void loadPermission() {

        if(!Utils.isPermissonGranted(this)){
            new AlertDialog.Builder(this)
                    .setTitle("All files Permission")
                    .setMessage("Due to Android 11 restriction, this app requires all files permission")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            takePermission();
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        }else{
            // Toast.makeText(this,"Permission Already Granted", Toast.LENGTH_SHORT).show();
            init();
        }



    }

    private void filterlist(String newText) {

        List<PdfData> filterList=new ArrayList<>();
        for (PdfData pdfData:pdfList){
            if(pdfData.getName().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(pdfData);
            }
        }

        if(filterList.isEmpty()){
            Toast.makeText(this,"No data found",Toast.LENGTH_SHORT).show();
        }else{
           AllPdfFile.setfilterdata(filterList);
        }


    }

    public void init(){


           pdfList= new ArrayList<>();

       // loadPdfFileinBackground();
        loadPdfFiles();



         adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), MainActivity.this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        // Set the text for each tab
                        if (position==0){
                            tab.setText("All Files");

                        }else if (position==1){
                            tab.setText("Recent");
                        }
                        else if(position==2){
                            tab.setText("Bookmarks");
                        }
                    }
                }
        ).attach();

        searchView=findViewById(R.id.searchView);


        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterlist(newText);
                return true;
            }
        });

        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
                .addTestDeviceHashedId("36AC515B03D289296D467572BED30BE8")
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .build();

        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setConsentDebugSettings(debugSettings)
                .setTagForUnderAgeOfConsent(false)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w(TAG, String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }

    }

    private void initializeMobileAdsSdk() {



    }


    public void getAllPdfFile(File file){

        File[] allfile=file.listFiles();

        for(File single: allfile){
            if(single.isDirectory() &&!single.isHidden()){

            }
        }


    }

  public void ViewPdffile(String filepath, String fileName, String date,String size){
        Intent intent=new Intent(MainActivity.this,PdfPreview.class);
        intent.putExtra("pdf",filepath);
        intent.putExtra("name",fileName);
        intent.putExtra("size",size);
        intent.putExtra("date",date);
        startActivity(intent);
    }

    private void loadPdfFiles() {
        Uri pdfUri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_MODIFIED};

        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "='application/pdf'";
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC"; // Order by date modified in descending order
        Cursor cursor = getContentResolver().query(pdfUri, projection, selection, null, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String pdfPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                String pdfName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
                long pdfSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                long pdfDateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED));

                String size = String.format("%.2f MB", pdfSize / (1024.0 * 1024.0));
                String date = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                        .format(new Date(pdfDateModified * 1000));

                PdfData pdfData = new PdfData(pdfName, size, date, pdfPath);
                pdfList.add(pdfData);
            }
            cursor.close();
        }



    }








    @Override
    protected void onResume() {
        super.onResume();


    }

    private void takePermission() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){

            try{
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent,101);
            }catch (Exception e){
                    e.printStackTrace();
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent,101);

            }


        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0){
            if(requestCode==101){
                 boolean readExtr=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                 if(!readExtr){
                     takePermission();
                 }else{
                     init();
                 }

            }
        }

    }


          public  void sharepdf(String path){
              File pdfFile = new File(path);
              Uri pdfFileUri = FileProvider.getUriForFile(this, "com.techtutor.pdfreadermaker.fileprovider", pdfFile);
              Intent shareIntent = new Intent(Intent.ACTION_SEND);
              shareIntent.setType("application/pdf");
              shareIntent.putExtra(Intent.EXTRA_STREAM, pdfFileUri);
              shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
              startActivity(Intent.createChooser(shareIntent, "Send PDF via"));

          }

          public  void deletefile(String path, String fileName,int position){


                            Dialog dialog=new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.setContentView(R.layout.file_delete_layout);

                            dialog.show();

              TextView filenametv=dialog.findViewById(R.id.filename);
              filenametv.setText(fileName);
              AppCompatButton delete,cancel;
              delete=dialog.findViewById(R.id.file_delete_confirm_button);
              cancel=dialog.findViewById(R.id.file_cancel_Button);

              cancel.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                       dialog.dismiss();
                  }
              });

              delete.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      File file =new File(path);
                      if(file.exists()){
                          boolean delete=file.delete();

                          if(delete){
                            //   Toast.makeText(MainActivity.this,"File Deleted Succesfully",Toast.LENGTH_SHORT).show();
                               pdfList.remove(position);
                              AllPdfFile.adpater.notifyItemRemoved(position);
                               dialog.dismiss();

                               RecentPdf recentPdf= db.databaseDao().getPdfByFilePath(path);
                               if(recentPdf!=null){

                                   db.databaseDao().deleteData(recentPdf);
                               }

                              BookMarkPdf bookMarkPdf=db.bookMarkDao().getPdfByFilePath(path);
                               if(bookMarkPdf!=null){
                                   db.bookMarkDao().deleteData(bookMarkPdf);
                               }






                          }else{
                              Toast.makeText(MainActivity.this,"Try Again",Toast.LENGTH_SHORT).show();
                          }
                      }else {
                          Toast.makeText(MainActivity.this,"No file Found",Toast.LENGTH_SHORT).show();
                      }
                  }
              });


          }

          public void renamefile(String path, String name, int position){


              Dialog dialog=new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
              dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
              dialog.setContentView(R.layout.rename_file);

              EditText editText=dialog.findViewById(R.id.rename);
              editText.setText(name);

              Button button=dialog.findViewById(R.id.renamebutton);
              dialog.show();


              button.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                       String name=editText.getText().toString();

                       if(name.length()<0){
                           editText.setError("Please enter file name");
                       }else{
                           if(name.endsWith(".pdf")){

                               File oldfile=new File(path);
                               File newFile=new File(oldfile.getParent(),name);

                               if(oldfile.renameTo(newFile)){
                                   pdfList.get(position).setName(name);
                                   pdfList.get(position).setPath(newFile.getAbsolutePath());
                                   AllPdfFile.adpater.notifyItemChanged(position);
                                   dialog.dismiss();
                               }


                           }else{
                               name=name.concat(".pdf");
                               File oldfile=new File(path);
                               File newFile=new File(oldfile.getParent(),name);

                               if(oldfile.renameTo(newFile)){
                                   pdfList.get(position).setName(name);
                                   pdfList.get(position).setPath(newFile.getAbsolutePath());
                                   AllPdfFile.adpater.notifyItemChanged(position);
                                   dialog.dismiss();
                               }

                           }
                       }
                  }
              });




          }

        public void   loadPdfFileinBackground(){

            ExecutorService service= Executors.newSingleThreadExecutor();
            Handler handler=new Handler(Looper.getMainLooper());
                  service.execute(new Runnable() {
                      @Override
                      public void run() {
                          loadPdfFiles();

                          handler.post(new Runnable() {
                              @Override
                              public void run() {
                                  AllPdfFile.adpater.notifyDataSetChanged();

                              }
                          });




                      }
                  });

          }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            init();
        }else{
            loadPermission();
            Toast.makeText(this,"We need permission",Toast.LENGTH_SHORT).show();
        }
    }


    public void insertData(RecentPdf recentPdf){
        db.databaseDao().insertData(recentPdf);
       

    }

   public void contactus(){

       Intent intent = new Intent(Intent.ACTION_SEND);
       String[] recipients = {"Rasel.cse.green@gmail.com"};
       intent.putExtra(Intent.EXTRA_EMAIL, recipients);
       intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
       intent.putExtra(Intent.EXTRA_TEXT, "");
       intent.putExtra(Intent.EXTRA_CC, "");
       intent.setType("text/html");
       intent.setPackage("com.google.android.gm");
       startActivity(Intent.createChooser(intent, "Send mail"));

   }

   public void shareap(){

       Intent sendIntent = new Intent();
       sendIntent.setAction(Intent.ACTION_SEND);
       sendIntent.putExtra(Intent.EXTRA_TEXT,
               "Download " + getResources().getString(R.string.app_name) + " : " + "https://play.google.com/store/apps/details?id=" + getPackageName());
       sendIntent.setType("text/plain");
       startActivity(sendIntent);

   }
   public void rateus(){

       final String AppPackageName=getPackageName();
       try {
           Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+ AppPackageName));
           startActivity(intent);

       }catch (Exception exception){

           Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ AppPackageName));
           startActivity(intent);
       }

   }

   public void policy(){
       startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://freshvpn22.blogspot.com/p/pdf-reader-privacy-policy.html")));
   }

   public void requestReviewInfo(){
        reviewManager= ReviewManagerFactory.create(MainActivity.this);
       Task<ReviewInfo> request=reviewManager.requestReviewFlow();
      request.addOnCompleteListener(task -> {
              if (task.isSuccessful()){
                  reviewInfo=task.getResult();
              }else{

              }


      });
   }

   public void showReviewFlow(){
        if (reviewInfo!=null){
            Task<Void> flow=reviewManager.launchReviewFlow(MainActivity.this,reviewInfo);
            flow.addOnCompleteListener(task -> {
                super.onBackPressed();
            });
        }
   }

    @Override
    public void onBackPressed() {
        showReviewFlow();


    }
}

















