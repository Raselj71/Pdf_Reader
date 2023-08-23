package com.techtutor.pdfreadermaker.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techtutor.pdfreadermaker.Adapter.RecentAdapter;
import com.techtutor.pdfreadermaker.MainActivity;
import com.techtutor.pdfreadermaker.MyApp;
import com.techtutor.pdfreadermaker.R;
import com.techtutor.pdfreadermaker.RoomDatabase.RecentPdf;


import java.util.List;


public class RecentPdfFile extends Fragment {

    MainActivity mainActivity;
    private RecyclerView recyclerView;
    private TextView textView;
    public static RecentAdapter recentAdapter;


    public RecentPdfFile(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }

    public static List<RecentPdf> allRecentFiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_recent_pdf_file, container, false);

       recyclerView=view.findViewById(R.id.recent_RecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        textView=view.findViewById(R.id.recentTv);

        allRecentFiles=recentFile();

            recentAdapter=new RecentAdapter(allRecentFiles,getContext(),mainActivity);

            recyclerView.setAdapter(recentAdapter);



       return  view;
    }

    public List<RecentPdf> recentFile(){
        return MyApp.db.databaseDao().getRecentPdfFiles();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Update the UI when the fragment becomes visible again
        updateUI();
    }

    public static void updateUI() {
        // Fetch the recent files from the database
        allRecentFiles = MyApp.db.databaseDao().getRecentPdfFiles();

        // Update the RecyclerView's data with the new list of recent files
        recentAdapter.updateData(allRecentFiles);
    }

}