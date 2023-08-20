package com.techtutor.pdfreadermaker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtutor.pdfreadermaker.MainActivity;
import com.techtutor.pdfreadermaker.R;
import com.techtutor.pdfreadermaker.RoomDatabase.DatabasePdf;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.myViewHolder > {

    private List<DatabasePdf> pdfList;
    private Context context;
    private MainActivity mainActivity;

    public RecentAdapter(List<DatabasePdf> pdfList, Context context, MainActivity mainActivity) {
        this.pdfList = pdfList;
        this.context = context;
        this.mainActivity = mainActivity;
    }



    public void setRecentPdfList(List<DatabasePdf> list){
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
        DatabasePdf pdf = pdfList.get(position);
        holder.pdfTile.setText(pdf.getName());
        holder.datetv.setText(pdf.getDate());
        holder.sizetv.setText(pdf.getSize());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mainActivity.ViewPdffile(pdf.getPath(),pdf.getName());
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

                                mainActivity.renamefile(pdf.getPath(),pdf.getName(),position);
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
