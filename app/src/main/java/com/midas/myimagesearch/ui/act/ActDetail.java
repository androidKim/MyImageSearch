package com.midas.myimagesearch.ui.act;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.midas.myimagesearch.MyApp;
import com.midas.myimagesearch.R;
import com.midas.myimagesearch.common.Constant;
import com.midas.myimagesearch.structure.img_documents;

import me.relex.photodraweeview.PhotoDraweeView;

public class ActDetail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    /********************* Define *********************/
    /********************* Member *********************/
    public MyApp m_App = null;
    public Context m_Context = null;
    public img_documents m_ImageInfo = null;
    /********************* Controller *********************/
    public TextView m_tv_LinkUrl = null;
    public PhotoDraweeView m_iv_Item = null;
    /********************* System Function *********************/
    //------------------------------------------------------------
    //
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail);
        m_App = new MyApp(this);
        m_Context = this;
        Fresco.initialize(this);//img library

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

        m_ImageInfo = (img_documents)pIntent.getSerializableExtra(Constant.INTENT_DATA_IMGDOCUMENTS);
    }
    //------------------------------------------------------------
    //
    public void initLayout()
    {
        m_tv_LinkUrl = (TextView)findViewById(R.id.tv_LinkUrl);
        m_iv_Item = (PhotoDraweeView)findViewById(R.id.iv_Item);
        
        settingView();
    }
    //------------------------------------------------------------
    //
    public void settingView()
    {
        if(m_ImageInfo == null)
            return;

        if(m_ImageInfo.doc_url != null)
        {
            m_tv_LinkUrl.setText(m_ImageInfo.doc_url);
            m_tv_LinkUrl.setVisibility(View.VISIBLE);
        }
        else
        {
            m_tv_LinkUrl.setVisibility(View.GONE);
        }

        if(m_ImageInfo.image_url != null)
        {
            if(m_ImageInfo.image_url.length() > 0)
            {
                Uri uri = Uri.parse(m_ImageInfo.image_url);
                m_iv_Item.setPhotoUri(uri);
            }
            m_iv_Item.setVisibility(View.VISIBLE);
        }
        else
        {
            m_iv_Item.setVisibility(View.GONE);
        }
    }
    /*********************** listener ***********************/

    /*********************** callback ***********************/
    //------------------------------------------------------------
    //
    @Override
    public void onRefresh()
    {

    }
}
