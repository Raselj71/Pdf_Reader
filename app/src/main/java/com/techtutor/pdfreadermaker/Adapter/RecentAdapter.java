package com.techtutor.pdfreadermaker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtutor.pdfreadermaker.Fragment.RecentPdfFile;
import com.techtutor.pdfreadermaker.MainActivity;
import com.techtutor.pdfreadermaker.MyApp;
import com.techtutor.pdfreadermaker.R;
import com.techtutor.pdfreadermaker.RoomDatabase.BookMarkPdf;
import com.techtutor.pdfreadermaker.RoomDatabase.RecentPdf;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.myViewHolder > {

    private List<RecentPdf> pdfList;
    private Context context;
    private MainActivity mainActivity;

    public RecentAdapter(List<RecentPdf> pdfList, Context context, MainActivity mainActivity) {
        this.pdfList = pdfList;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    public  void updateData(List<RecentPdf> allRecentFiles) {
        pdfList=allRecentFiles;
        notifyDataSetChanged();
    }


    public void setRecentPdfList(List<RecentPdf> list){
        pdfList=list;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recylerview_item,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        RecentPdf pdf = pdfList.get(position);
        holder.pdfTile.setText(pdf.getName());
        holder.datetv.setText(pdf.getDate());
        holder.sizetv.setText(pdf.getSize());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mainActivity.ViewPdffile(pdf.getPath(),pdf.getName(),pdf.getDate(),pdf.getSize());
               // DatabasePdf databasePdf=new DatabasePdf(0,pdf.getName(),pdf.getSize(),pdf.getDate(),pdf.getPath(),true,false);


               // mainActivity.insertData(databasePdf);




            }
        });

        holder.menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,holder.menubutton);
                popupMenu.getMenuInflater().inflate(R.menu.recent_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.share_recent:{
                                mainActivity.sharepdf(pdf.getPath());
                                break;
                            }
                            case R.id.remove_recent:{
                                MyApp.db.databaseDao().deleteData(pdf);
                                RecentPdfFile.updateUI();

                                break;

                            }
                            case R.id.bookmark_recent:{
                                BookMarkPdf existingPdf = MyApp.db.bookMarkDao().getPdfByFilePath(pdf.getPath());

                                if(existingPdf==null){

                                    BookMarkPdf bookMarkPdf=new BookMarkPdf(0,pdf.getName(),pdf.getSize(),pdf.getDate(),pdf.getPath(),System.currentTimeMillis());
                                    MyApp.db.bookMarkDao().insertData(bookMarkPdf);
                                    Toast.makeText(context,"Added in Bookmarks",Toast.LENGTH_SHORT).show();

                                    //mainActivity.adapter.notifyDataSetChanged();

                                }else{


                                    BookMarkPdf bookMarkPdf=new BookMarkPdf(0,pdf.getName(),pdf.getSize(),pdf.getDate(),pdf.getPath(),System.currentTimeMillis());
                                    MyApp.db.bookMarkDao().updateData(bookMarkPdf);
                                    Toast.makeText(context,"Already Added in Bookmarks",Toast.LENGTH_SHORT).show();


                                }
                            }

                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        public TextView pdfTile,sizetv,datetv;
        public ImageView menubutton;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfTile=itemView.findViewById(R.id.pdf_name);
            sizetv=itemView.findViewById(R.id.size);
            datetv=itemView.findViewById(R.id.date);
            menubutton=itemView.findViewById(R.id.moreButton);
        }
    }
}
