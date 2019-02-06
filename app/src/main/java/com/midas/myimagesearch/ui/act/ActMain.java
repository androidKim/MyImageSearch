package com.midas.myimagesearch.ui.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.midas.myimagesearch.MyApp;
import com.midas.myimagesearch.R;
import com.midas.myimagesearch.ui.adapter.ViewPagerAdapter;
import com.midas.myimagesearch.ui.frag.FragSearch;
import com.midas.myimagesearch.ui.frag.FragStorage;

import org.json.JSONObject;


public class ActMain extends AppCompatActivity implements FragSearch.IfCallbackSearch
{
    /********************* Define *********************/
    /********************* Member *********************/
    private MyApp m_App = null;
    private Context m_Context = null;
    private Activity m_Activity = null;
    /********************* Controller *********************/
    private TabLayout m_TabLaytout = null;
    private ViewPager m_ViewPager = null;
    private ViewPagerAdapter m_PagerAdater = null;

    /********************* System Function *********************/
    //------------------------------------------------------------
    //
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        m_App = new MyApp(this);
        m_Context = this;
        m_Activity = this;

        initValue();
        recvIntentData();
        initLayout();
    }

    /********************* User Function *********************/
    //------------------------------------------------------------
    //
    public void initValue()
    {

    }
    //------------------------------------------------------------
    //
    public void recvIntentData()
    {
        Intent pIntent = getIntent();
        if(pIntent == null)
            return;
    }
    public void initLayout()
    {
        m_TabLaytout = (TabLayout) findViewById(R.id.tabLayout);
        m_ViewPager = (ViewPager)findViewById(R.id.viewPager);

        m_TabLaytout.setupWithViewPager(m_ViewPager);
        m_PagerAdater = new ViewPagerAdapter(getSupportFragmentManager(), this, this);
        m_ViewPager.setAdapter(m_PagerAdater);
        m_ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(m_TabLaytout));
        m_TabLaytout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                m_ViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
    }
    /********************* callback *********************/
    //------------------------------------------------------------
    //
    @Override
    public void sendData(JSONObject jsonObj)
    {
        FragStorage fragment  = (FragStorage)m_PagerAdater.getItem(ViewPagerAdapter.FRAG_STORAGE);
        fragment.addData(jsonObj);
    }

}
