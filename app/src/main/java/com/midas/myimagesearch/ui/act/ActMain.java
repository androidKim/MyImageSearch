package com.midas.myimagesearch.ui.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.midas.myimagesearch.MyApp;
import com.midas.myimagesearch.R;
import com.midas.myimagesearch.structure.ReqBase;
import com.midas.myimagesearch.structure.function.img_list.res_img_list;
import com.midas.myimagesearch.structure.img_documents;
import com.midas.myimagesearch.ui.adapter.RecyclerViewAdapter;
import com.midas.myimagesearch.util.NetworkCtrl;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    /********************* Define *********************/
    /********************* Member *********************/
    public MyApp m_App = null;
    public Context m_Context = null;
    public Handler m_Handler = null;
    public RecyclerViewAdapter m_Adapter = null;
    public res_img_list m_ResImageList = null;
    public ArrayList<img_documents> m_arrItems = null;
    public String m_strSearchText = null;//image searchText
    public int m_nPageNum = 1;//pageIndex
    public boolean m_bRunning = false;
    public boolean m_bEnd = false;
    /********************* Controller *********************/
    public SwipeRefreshLayout m_SwipeRefresh = null;
    public RecyclerView m_RecyclerView = null;
    public EditText m_edit_Search = null;
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
        Fresco.initialize(this);//img library
        m_Handler = new Handler();
        initValue();
        recvIntentData();
        initLayout();
    }

    /********************* User Function *********************/
    //------------------------------------------------------------
    //
    public void initValue()
    {
        m_ResImageList = null;
        if(m_strSearchText == null)
            m_strSearchText = "";

        m_arrItems = new ArrayList<>();
        m_nPageNum = 1;
        m_Adapter = null;
        m_bEnd = false;

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(m_RecyclerView != null)
                    m_RecyclerView.removeAllViews();
            }
        });
    }
    //------------------------------------------------------------
    //
    public void recvIntentData()
    {
        Intent pIntent = getIntent();
        if(pIntent == null)
            return;
    }
    //------------------------------------------------------------
    //
    public void initLayout()
    {
        m_SwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.ly_SwipeRefresh);
        m_RecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        m_edit_Search = (EditText)findViewById(R.id.edit_Search);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this);
        m_RecyclerView.setLayoutManager(verticalLayoutManager);

        //listener..
        m_SwipeRefresh.setOnRefreshListener(this);
        m_edit_Search.addTextChangedListener(textWatcher);
    }
    //------------------------------------------------------------
    //
    public void runHandler(final String strValue)
    {
        if(m_strSearchText == null || strValue == null)
            return;

        m_Handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //1초동안 대기하고있던 검색어가 동일하면..
                if(m_strSearchText.equals(strValue))
                {
                    if(!m_bRunning)//검색수행..
                    {
                        initValue();
                        getImageListProc();
                    }
                }
            }
        }, 1000);
    }

    //------------------------------------------------------------
    //
    public void getImageListProc()
    {
        if(m_App.m_NetworkCtrl.getStatus() == NetworkCtrl.STAT_NOT_CONNECTED)//Network체크
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(m_Context, m_Context.getResources().getString(R.string.network_msg_5), Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
        else
        {
            if(!m_bRunning)
            {
                m_bRunning = true;
                Call<res_img_list> call = m_App.m_APIInterface.getImageListProc(m_strSearchText, ReqBase.STR_SEARCH_TYPE_RECENCY, m_nPageNum, ReqBase.ITEM_COUNT);
                call.enqueue(new Callback<res_img_list>() {
                    @Override
                    public void onResponse(Call<res_img_list> call, Response<res_img_list> response)
                    {
                        m_bRunning = false;
                        Log.d("status code",response.code()+"");
                        res_img_list pRes = response.body();

                        if(pRes!=null)
                        {
                            if(pRes.meta  != null)
                            {
                                m_bEnd = pRes.meta.is_end;

                                if(pRes.meta.total_count <= 0)
                                {
                                    String msg = String.format("[%s]\n%s", m_strSearchText, m_Context.getResources().getString(R.string.network_msg_2));
                                    m_App.showMessageDlg(m_Context, m_Context.getResources().getString(R.string.network_msg_1),
                                            msg);
                                }
                                else if(pRes.meta.is_end)
                                {
                                    String msg = String.format("[%s]\n%s", m_strSearchText, m_Context.getResources().getString(R.string.network_msg_3));
                                    m_App.showMessageDlg(m_Context, m_Context.getResources().getString(R.string.network_msg_1),
                                            msg);
                                }

                            }

                            m_nPageNum++;
                            settingView(pRes);
                        }
                        else
                        {
                            String msg = String.format("[%s]\n%s", m_strSearchText, m_Context.getResources().getString(R.string.network_msg_2));
                            m_App.showMessageDlg(m_Context, m_Context.getResources().getString(R.string.network_msg_1),
                                    msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<res_img_list> call, Throwable t)
                    {
                        m_bRunning = false;
                        call.cancel();

                        m_App.showMessageDlg(m_Context, m_Context.getResources().getString(R.string.network_msg_1),
                                m_Context.getResources().getString(R.string.network_msg_4));
                    }
                });
            }
            return;
        }
    }
    //------------------------------------------------------------
    //
    public void settingView(res_img_list pRes)
    {
        if(pRes == null)
            return;

        if(pRes.documents == null)
            return;

        if(m_Adapter == null)//initView
        {
            m_Adapter = new RecyclerViewAdapter(m_Context, pRes.documents);
            m_RecyclerView.setAdapter(m_Adapter);
            //setLietener..
            m_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState)
                {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1))
                    {
                        if(!m_bEnd)//more view
                        {
                            getImageListProc();
                        }
                    }
                }
            });
        }
        else//addView
        {
            m_Adapter.addData(pRes.documents);
        }
    }
    //------------------------------------------------------------
    //
    public void setRefresh()
    {
        initValue();
        getImageListProc();

        m_SwipeRefresh.setRefreshing(false);
    }
    /*********************** listener ***********************/
    //------------------------------------------------------------
    //
    TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            String strValue = m_edit_Search.getText().toString().trim();
            if (strValue.length() > 0)
            {
                if(m_strSearchText.equals(""))//최초입력..
                {
                    m_strSearchText = strValue;
                    runHandler(strValue);
                }
                else
                {
                    if(!strValue.equals(m_strSearchText))//검색어 변경..
                    {
                        initValue();
                        m_strSearchText = strValue;
                        runHandler(strValue);
                    }
                    else
                    {
                        m_strSearchText = strValue;
                    }
                }
            }
        }
    };
    /*********************** callback ***********************/
    //------------------------------------------------------------
    //
    @Override
    public void onRefresh()
    {
        setRefresh();
    }
}
