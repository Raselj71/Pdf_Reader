package com.techtutor.pdfreadermaker.ViewpagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.techtutor.pdfreadermaker.Fragment.AllPdfFile;
import com.techtutor.pdfreadermaker.Fragment.BookMarkPdfFile;
import com.techtutor.pdfreadermaker.Fragment.RecentPdfFile;
import com.techtutor.pdfreadermaker.MainActivity;

public class ViewPagerAdapter extends FragmentStateAdapter {

           MainActivity mainActivity;
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, MainActivity mainActivity) {
        super(fragmentManager, lifecycle);
        this.mainActivity=mainActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
         switch (position){

             case 0: return  new AllPdfFile(mainActivity);
             case 1: return new RecentPdfFile(mainActivity);
             case 2 :return new BookMarkPdfFile(mainActivity);
             default:return new AllPdfFile(mainActivity);


         }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
