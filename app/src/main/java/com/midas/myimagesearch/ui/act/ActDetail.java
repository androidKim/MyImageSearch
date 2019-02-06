package com.midas.myimagesearch.ui.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.midas.myimagesearch.MyApp;
import com.midas.myimagesearch.R;
import com.midas.myimagesearch.common.Constant;
import com.midas.myimagesearch.structure.img_documents;
public class ActDetail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    /********************* Define *********************/
    /********************* Member *********************/
    private MyApp m_App = null;
    private Context m_Context = null;
    private RequestManager m_RequestManager = null;
    private img_documents m_ImageInfo = null;
    /********************* Controller *********************/
    private TextView m_tv_LinkUrl = null;
    private ImageView m_iv_Item = null;
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
        m_RequestManager = Glide.with(m_Context);

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
        m_iv_Item = (ImageView)findViewById(R.id.iv_Item);
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

        if(m_ImageInfo.thumbnail_url != null)//이미지 검색 결과
        {
            if(m_ImageInfo.thumbnail_url.length() > 0)
            {
                RequestBuilder requestBuilder = m_RequestManager.load(m_ImageInfo.thumbnail_url);
                requestBuilder.into(m_iv_Item);
            }
            m_iv_Item.setVisibility(View.VISIBLE);
        }
        else if(m_ImageInfo.thumbnail != null)//동영상검색결과
        {
            if(m_ImageInfo.thumbnail.length() > 0)
            {
                RequestBuilder requestBuilder = m_RequestManager.load(m_ImageInfo.thumbnail);
                requestBuilder.into(m_iv_Item);
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
