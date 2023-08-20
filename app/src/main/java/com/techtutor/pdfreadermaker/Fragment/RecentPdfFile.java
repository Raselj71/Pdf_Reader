package com.techtutor.pdfreadermaker.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.techtutor.pdfreadermaker.Adapter.RecentAdapter;
import com.techtutor.pdfreadermaker.MainActivity;
import com.techtutor.pdfreadermaker.MyApp;
import com.techtutor.pdfreadermaker.R;
import com.techtutor.pdfreadermaker.RoomDatabase.DatabaseDao;
import com.techtutor.pdfreadermaker.RoomDatabase.DatabasePdf;

import java.util.List;


public class RecentPdfFile extends Fragment {

    MainActivity mainActivity;
    private RecyclerView recyclerView;
    private TextView textView;
    public static    RecentAdapter recentAdapter;
    public RecentPdfFile(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_recent_pdf_file, container, false);

       recyclerView=view.findViewById(R.id.recent_RecylerView);


       RecentAdapter recentAdapter=new RecentAdapter(recentFile(),getContext(),mainActivity);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       recyclerView.setAdapter(recentAdapter);
















       return  view;
    }

    public List<DatabasePdf> recentFile(){
        return MyApp.db.databaseDao().getRecentPdfFiles();
    }
}