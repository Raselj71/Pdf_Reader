package com.techtutor.pdfreadermaker.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techtutor.pdfreadermaker.Adapter.MyAdpater;
import com.techtutor.pdfreadermaker.Adapter.PdfData;
import com.techtutor.pdfreadermaker.MainActivity;
import com.techtutor.pdfreadermaker.R;

import java.util.List;


public class AllPdfFile extends Fragment {
            MainActivity mainActivity;

            public AllPdfFile(MainActivity mainActivity){
                this.mainActivity=mainActivity;
            }

          private RecyclerView recyclerView;
          TextView nofileTexview;
            public static MyAdpater adpater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_all_pdf_file, container, false);


           recyclerView=view.findViewById(R.id.allPdfRecylerView);
           nofileTexview=view.findViewById(R.id.nofile);
             if(MainActivity.pdfList.isEmpty()){
                 nofileTexview.setVisibility(View.VISIBLE);
                 recyclerView.setVisibility(View.GONE);
             }else{
                 recyclerView.setVisibility(View.VISIBLE);

                 adpater=new MyAdpater(MainActivity.pdfList,getContext(),mainActivity);
                 recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                 recyclerView.setAdapter(adpater);
             }















        return  view;
    }

    public static void setfilterdata(List<PdfData> filterlist){
        adpater.setFilteredList(filterlist);
    }

}