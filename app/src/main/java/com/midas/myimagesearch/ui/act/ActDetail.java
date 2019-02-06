package com.midas.myimagesearch.ui.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.midas.myimagesearch.MyApp;
import com.midas.myimagesearch.R;
import com.midas.myimagesearch.common.Constant;
import com.midas.myimagesearch.structure.img_documents;

import java.util.concurrent.TimeUnit;

public class ActDetail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    /********************* Define *********************/
    /********************* Member *********************/
    private MyApp m_App = null;
    private Context m_Context = null;
    private RequestManager m_RequestManager = null;
    private img_documents m_DataInfo = null;
    private Handler m_Handler = null;
    /********************* Controller *********************/
    private LinearLayout m_ly_ImageContainer = null;
    private LinearLayout m_ly_VideoContainer = null;

    private TextView m_tv_LinkUrl = null;
    private ImageView m_iv_Image = null;
    private ImageView m_iv_Video = null;
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

        m_DataInfo = (img_documents)pIntent.getSerializableExtra(Constant.INTENT_DATA_IMGDOCUMENTS);
    }
    //------------------------------------------------------------
    //
    public void initLayout()
    {
        m_Handler = new Handler();
        m_ly_ImageContainer = (LinearLayout)findViewById(R.id.ly_ImageContainer);
        m_ly_VideoContainer = (LinearLayout)findViewById(R.id.ly_VideoContainer);
        m_tv_LinkUrl = (TextView)findViewById(R.id.tv_LinkUrl);
        m_iv_Image = (ImageView)findViewById(R.id.iv_Image);
        m_iv_Video = (ImageView)findViewById(R.id.iv_Video);
        settingView();
    }
    //------------------------------------------------------------
    //
    public void settingView()
    {
        if(m_DataInfo == null)
            return;

        if(m_DataInfo.thumbnail_url != null)//image
        {
            m_ly_ImageContainer.setVisibility(View.VISIBLE);
            m_ly_VideoContainer.setVisibility(View.GONE);
            if(m_DataInfo.doc_url != null)
            {
                m_tv_LinkUrl.setText(m_DataInfo.doc_url);
                m_tv_LinkUrl.setVisibility(View.VISIBLE);
            }
            else
            {
                m_tv_LinkUrl.setVisibility(View.GONE);
            }


            if(m_DataInfo.thumbnail_url.length() > 0)
            {
                m_RequestManager.load(m_DataInfo.thumbnail_url).into(m_iv_Image);
            }
        }
        else if(m_DataInfo.thumbnail != null)//video
        {
            m_ly_ImageContainer.setVisibility(View.GONE);
            m_ly_VideoContainer.setVisibility(View.VISIBLE);

            if(m_DataInfo.thumbnail.length() > 0)
            {
                m_RequestManager.load(m_DataInfo.thumbnail).into(m_iv_Video);
            }

            m_ly_VideoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(m_DataInfo.url)));
                }
            });
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
