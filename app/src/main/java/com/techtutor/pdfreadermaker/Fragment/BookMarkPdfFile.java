package com.techtutor.pdfreadermaker.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techtutor.pdfreadermaker.Adapter.BookMarkAdapter;
import com.techtutor.pdfreadermaker.MainActivity;
import com.techtutor.pdfreadermaker.MyApp;
import com.techtutor.pdfreadermaker.R;
import com.techtutor.pdfreadermaker.RoomDatabase.BookMarkPdf;

import java.util.List;


public class BookMarkPdfFile extends Fragment {

    MainActivity mainActivity;

    public BookMarkPdfFile(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }
       private RecyclerView recyclerView;
    public static List<BookMarkPdf> allBoookMarkPdf;
    public static BookMarkAdapter bookMarkAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_book_mark_pdf_file, container, false);
        recyclerView=view.findViewById(R.id.bookmar_recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allBoookMarkPdf=getAllBookmark();
         bookMarkAdapter=new BookMarkAdapter(allBoookMarkPdf,getContext(),mainActivity);
        recyclerView.setAdapter(bookMarkAdapter);








        return view;
    }


    public List<BookMarkPdf> getAllBookmark(){
        return MyApp.db.bookMarkDao().getBookmarkFiles();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the UI when the fragment becomes visible again
        updateUI();
    }

    public static void updateUI() {
        // Fetch the recent files from the database
        allBoookMarkPdf = MyApp.db.bookMarkDao().getBookmarkFiles();

        // Update the RecyclerView's data with the new list of recent files
        bookMarkAdapter.updateData(allBoookMarkPdf);
    }


}