package com.techtutor.pdfreadermaker.Adapter;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.techtutor.pdfreadermaker.Fragment.RecentPdfFile;
import com.techtutor.pdfreadermaker.MainActivity;
import com.techtutor.pdfreadermaker.MyApp;
import com.techtutor.pdfreadermaker.PdfPreview;
import com.techtutor.pdfreadermaker.R;
import com.techtutor.pdfreadermaker.RoomDatabase.DatabasePdf;

import java.io.File;
import java.util.List;

public class MyAdpater extends RecyclerView.Adapter<MyAdpater.myViewHolder> {
    private List<PdfData> pdfList;
    private Context context;
    private MainActivity mainActivity;

    public MyAdpater(List<PdfData> pdfList, Context context ,MainActivity mainActivity) {
        this.pdfList = pdfList;
        this.context = context;
        this.mainActivity=mainActivity;
    }



    public void setFilteredList(List<PdfData> filteredList){
        this.pdfList=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAdpater.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recylerview_item,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdpater.myViewHolder holder, int position) {
        PdfData pdf = pdfList.get(position);
        holder.pdfTile.setText(pdf.getName());
        holder.datetv.setText(pdf.getDate());
        holder.sizetv.setText(pdf.getSize());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mainActivity.ViewPdffile(pdf.getPath(),pdf.getName());




                DatabasePdf existingPdf = MyApp.db.databaseDao().getPdfByFilePath(pdf.getPath());

                if (existingPdf == null) {
                    // PDF doesn't exist, insert it
                    new Thread(() -> {

                        DatabasePdf databasePdf=new DatabasePdf(0,pdf.getName(),pdf.getSize(),pdf.getDate(),pdf.getPath(),true,false,System.currentTimeMillis());
                        mainActivity.insertData(databasePdf);
                        //RecentPdfFile.adapter.notifyDataSetChanged();
                    }).start();
                } else {
                  /*  // PDF already exists, update its timestamp
                    new Thread(() -> {
                        existingPdf.timestamp = System.currentTimeMillis();
                        mainActivity.db.databaseDao().insertPdf(existingPdf);
                    }).start();

                   */
                }





            }
        });

        holder.menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,holder.menubutton);
                popupMenu.inflate(R.menu.recylerview_item_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.share_item:{
                              mainActivity.sharepdf(pdf.getPath());
                              break;
                            }
                            case R.id.rename_item:{

                                mainActivity.renamefile(pdf.getPath(),pdf.getName(),position);
                                break;

                            }
                            case R.id.delete_item:{

                                mainActivity.deletefile(pdf.getPath(), pdf.getName(),position);
                                break;

                            }
                            case R.id.bookmark_all:{
                                DatabasePdf databasePdf=new DatabasePdf(0,pdf.getName(),pdf.getSize(),pdf.getDate(),pdf.getPath(),false,true,System.currentTimeMillis());
                                MyApp.db.databaseDao().updateData(databasePdf);

                                break;
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
