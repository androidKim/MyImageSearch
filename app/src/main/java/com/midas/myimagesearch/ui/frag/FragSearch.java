package com.midas.myimagesearch.ui.frag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.midas.myimagesearch.MyApp;
import com.midas.myimagesearch.R;
import com.midas.myimagesearch.core.APIClient;
import com.midas.myimagesearch.core.APIInterface;
import com.midas.myimagesearch.structure.ReqBase;
import com.midas.myimagesearch.structure.function.img_list.res_img_list;
import com.midas.myimagesearch.structure.img_documents;
import com.midas.myimagesearch.ui.adapter.RecyclerViewAdapter;
import com.midas.myimagesearch.util.NetworkCtrl;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
Searhc Fragment
 */
public class FragSearch extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener, RecyclerViewAdapter.IfCallback
{
    /************************** Member *************************/
    private static FragSearch instance = null;
    private MyApp m_App = null;
    private Context m_Context = null;
    private Activity m_Activity = null;
    private Handler m_Handler = null;
    private RecyclerViewAdapter m_Adapter = null;
    private IfCallbackSearch m_IfCabllbackSearch = null;
    private res_img_list m_ResImageList = null;
    private ArrayList<img_documents> m_arrItems = null;
    private ArrayList<img_documents> m_arrSumRecvData = null;
    private String m_strSearchText = null;//image searchText
    private String m_strSearchType = ReqBase.STR_SEARCH_TYPE_RECENCY;//deafult 최근순
    private int m_nPageNum = 1;//pageIndex
    private int m_nReqCount = 0;//
    private boolean m_bRunning = false;
    private boolean m_bImageEnd = false;
    private boolean m_bVodEnd = false;
    /************************** Controller *************************/
    private Spinner m_Spinner = null;
    private SwipeRefreshLayout m_SwipeRefresh = null;
    private RecyclerView m_RecyclerView = null;
    private EditText m_edit_Search = null;
    private ProgressBar m_ProgressBar = null;

    //----------------------------------------------------
    //
    public FragSearch()
    {

    }
    //----------------------------------------------------
    //
    public static FragSearch newInstance()
    {
        if(instance == null)
            instance = new FragSearch();

        return instance;
    }

    /*********************** System Function ***********************/
    //----------------------------------------------------
    //
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_search, container, false);
        initValue();
        initLayout(view);
        return view;

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        m_Context = context;
        m_Activity = getActivity();
        m_App = new MyApp(m_Context);
    }

    /*********************** User Function ***********************/
    public void initValue(){
        m_ResImageList = null;
        if(m_strSearchText == null)
            m_strSearchText = "";

        m_arrItems = new ArrayList<>();
        m_nPageNum = 1;

        if(m_Adapter!= null)
        {
            m_Adapter.removeAllData();
            m_Adapter = null;
        }

        m_bImageEnd = false;
        m_bVodEnd = false;

        ((Activity)m_Context).runOnUiThread(new Runnable()
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
    public void initLayout(View pView)
    {
        m_Handler = new Handler();
        m_Spinner = (Spinner)pView.findViewById(R.id.spinner);
        m_SwipeRefresh = (SwipeRefreshLayout)pView.findViewById(R.id.ly_SwipeRefresh);
        m_RecyclerView = (RecyclerView)pView.findViewById(R.id.recyclerView);
        m_edit_Search = (EditText)pView.findViewById(R.id.edit_Search);
        m_ProgressBar = (ProgressBar)pView.findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        m_RecyclerView.setLayoutManager(layoutManager);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(m_Context, R.array.search_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        m_Spinner.setAdapter(adapter);

        //listener..
        m_Spinner.setOnItemSelectedListener(this);
        m_SwipeRefresh.setOnRefreshListener(this);
        m_edit_Search.addTextChangedListener(textWatcher);
    }
    //------------------------------------------------------------
    //
    public void runHandler(final String strValue)
    {
        if(m_strSearchText == null || strValue == null)
            return;

        if(m_strSearchText.equals(""))
        {
            initValue();
            return;
        }

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

                        if(m_App != null)
                            m_App.hideKeyboard(m_Activity);
                    }
                }
            }
        }, 2000);
    }

    //------------------------------------------------------------
    //
    public void getImageListProc()
    {
        m_arrSumRecvData = new ArrayList<>();
        if(m_App.m_NetworkCtrl.getStatus() == NetworkCtrl.STAT_NOT_CONNECTED)//Network체크
        {
            ((Activity)m_Context).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    m_bRunning = false;
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
                m_ProgressBar.setVisibility(View.VISIBLE);
                /*
                multiple api request
                 */
                Observable.just(APIClient.getClient().create(APIInterface.class)).subscribeOn(Schedulers.computation())
                        .flatMap(s -> {
                            Observable<res_img_list> imgResObsaverble
                                    = s.getImageListProc(m_strSearchText, m_strSearchType, m_nPageNum, ReqBase.ITEM_COUNT).subscribeOn(Schedulers.io());

                            Observable<res_img_list> vodResObsaverble
                                    = s.getVodListProc(m_strSearchText, m_strSearchType, m_nPageNum, ReqBase.ITEM_COUNT).subscribeOn(Schedulers.io());

                            if(m_bImageEnd && !m_bVodEnd)
                                return Observable.concatArray(vodResObsaverble);
                            else if(!m_bImageEnd && m_bVodEnd)
                                return Observable.concatArray(imgResObsaverble);
                            else if(!m_bImageEnd && !m_bVodEnd)
                                return Observable.concatArray(imgResObsaverble, vodResObsaverble);
                            else
                                return Observable.concatArray(null);

                        }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError );
            }
            return;
        }
    }
    private void handleResults(res_img_list pRes){
        m_nReqCount++;


        if(pRes.meta  != null)
        {
            if(!m_bImageEnd && !m_bVodEnd)
            {
                if(m_nReqCount == 1)
                    m_bImageEnd = pRes.meta.is_end;
                else if(m_nReqCount == 2)
                    m_bVodEnd = pRes.meta.is_end;
            }
            else
            {
                if(m_bImageEnd)
                    m_bVodEnd = pRes.meta.is_end;

                if(m_bVodEnd)
                    m_bImageEnd = pRes.meta.is_end;
            }


            if(m_bImageEnd && m_bVodEnd)
            {
                String msg = String.format("[%s]\n%s", m_strSearchText, m_Context.getResources().getString(R.string.network_msg_3));
                m_App.showMessageDlg(m_Context, m_Context.getResources().getString(R.string.network_msg_1),
                        msg);
            }


        }

        if(pRes != null)//sum data..
        {
            if(pRes.documents != null)
            {
                if(pRes.documents.size() > 0)
                {
                    m_arrSumRecvData.addAll(pRes.documents);
                }
            }
        }

        m_bRunning = false;
        m_ProgressBar.setVisibility(View.GONE);
        if(m_nReqCount >= 2)//갱신할 데이터가 있으면..
        {
            m_nPageNum ++;
            m_nReqCount = 0;
            sortArray();
            settingView(m_arrSumRecvData);
        }
        else if(m_bImageEnd || m_bVodEnd)
        {
            m_nPageNum ++;
            m_nReqCount = 0;
            if(m_arrSumRecvData.size() > 0)
            {
                sortArray();
                settingView(m_arrSumRecvData);
            }
        }

    }
    private void handleError(Throwable t){
        m_nReqCount++;
        if(m_nReqCount >= 2) {
            m_nReqCount = 0;
        }
        m_bRunning = false;

        m_App.showMessageDlg(m_Context, m_Context.getResources().getString(R.string.network_msg_1),
                m_Context.getResources().getString(R.string.network_msg_4));

        m_ProgressBar.setVisibility(View.GONE);
    }
    //------------------------------------------------------------
    //
    public void sortArray(){
        Collections.sort(m_arrSumRecvData, new Comparator<img_documents>() {
            @Override
            public int compare(img_documents item1, img_documents item2) {
                long timestamp1 = item1.getDateTime();
                long timestamp2 = item2.getDateTime();
                if (timestamp1 > timestamp2)
                    return 1;
                else if (timestamp1 < timestamp2)
                    return -1;
                else
                    return 0;
            }
        });
    }
    //------------------------------------------------------------
    //
    public void settingView(ArrayList<img_documents> pArray)
    {
        if(pArray == null)
            return;

        if(pArray.size() == 0)
            return;

        //sort Array..


        if(m_Adapter == null)//initView
        {
            m_Adapter = new RecyclerViewAdapter(m_App, m_Context, pArray, this);
            m_RecyclerView.setAdapter(m_Adapter);
            //setLietener..
            m_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState)
                {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1))//최하단
                    {
                        if(!m_bImageEnd || !m_bVodEnd)//more view
                        {
                            if(!m_bRunning)
                                getImageListProc();
                        }
                    }
                }
            });
        }
        else//addView
        {
            m_Adapter.addData(pArray);
        }
    }
    //------------------------------------------------------------
    //
    public void setRefresh()
    {
        m_SwipeRefresh.setRefreshing(false);
        initValue();
        if(m_strSearchText == null)
            return;

        if(m_strSearchText.equals(""))
            return;

        getImageListProc();
    }


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
                }
                else
                {
                    if(!strValue.equals(m_strSearchText))//검색어 변경..
                    {
                        m_strSearchText = strValue;
                    }
                    else
                    {
                        m_strSearchText = strValue;
                    }
                }
                runHandler(strValue);
            }
            else
            {
                m_strSearchText = "";
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
    //------------------------------------------------------------
    //spinner callback
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        // An item was selected. You can retrieve the selected item using
        //String str = (String)adapterView.getItemAtPosition(i);
        switch (i)
        {
            case 0:
                m_strSearchType = ReqBase.STR_SEARCH_TYPE_RECENCY;
                break;
            case 1:
                m_strSearchType = ReqBase.STR_SEARCH_TYPE_ACCURACY;
                break;
            default:
                m_strSearchType = ReqBase.STR_SEARCH_TYPE_RECENCY;
                break;
        }

        if(m_strSearchText != null)
        {
            if(m_strSearchText.length() > 0)
                runHandler(m_strSearchText);
        }
    }
    //------------------------------------------------------------
    //spinner callback
    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        // Another interface callback
        Log.d("","");
    }

    /********************* interface *********************/
    //------------------------------------------------------------
    //
    public interface IfCallbackSearch
    {
        void sendData(JSONObject jsonObj);
    }
    //------------------------------------------------------------
    //
    public void setInterface(IfCallbackSearch pCallback){
        this.m_IfCabllbackSearch = pCallback;
    }

    /********************* callback *********************/
    //------------------------------------------------------------
    //
    @Override
    public void notifyAddData(JSONObject jsonObj)
    {
        if(m_IfCabllbackSearch != null)
            m_IfCabllbackSearch.sendData(jsonObj);
    }
}
