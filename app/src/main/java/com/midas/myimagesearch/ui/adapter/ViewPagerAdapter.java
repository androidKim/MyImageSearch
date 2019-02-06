package com.midas.myimagesearch.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.midas.myimagesearch.R;
import com.midas.myimagesearch.ui.frag.FragSearch;
import com.midas.myimagesearch.ui.frag.FragStorage;

import org.json.JSONObject;

public class ViewPagerAdapter extends FragmentStatePagerAdapter
{
    public static final int PAGE_COUNT = 2;
    public static final int FRAG_SEARCH = 0;
    public static final int FRAG_STORAGE = 1;

    private Context m_Context = null;
    private FragSearch.IfCallbackSearch m_IfCallbackSearch = null;
    private int[] m_TabTitles = {R.string.frag_search, R.string.frag_storage};

    public ViewPagerAdapter(FragmentManager fm, Context pContext, FragSearch.IfCallbackSearch pCallback)
    {
        super(fm);
        this.m_Context = pContext;
        this.m_IfCallbackSearch = pCallback;
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment pFragment = null;
        switch (position)
        {
            case FRAG_SEARCH:
                pFragment = FragSearch.newInstance();
                if(m_IfCallbackSearch != null)
                    ((FragSearch) pFragment).setInterface(m_IfCallbackSearch);

                return pFragment;
            case FRAG_STORAGE:
                pFragment = FragStorage.newInstance();
                return pFragment;
        }

        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return m_Context.getString(m_TabTitles[position]);
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }
}
