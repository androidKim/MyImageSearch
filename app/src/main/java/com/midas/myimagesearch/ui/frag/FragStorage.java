package com.midas.myimagesearch.ui.frag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midas.myimagesearch.MyApp;
import com.midas.myimagesearch.R;
import com.midas.myimagesearch.ui.adapter.StorageViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
Storage Fragment
 */
public class FragStorage extends Fragment
{
    /*********************** Define ***********************/
    /*********************** Member ***********************/
    private static FragStorage instance = null;
    private Context m_Context = null;
    private Activity m_Activity = null;
    private MyApp m_App = null;
    private StorageViewAdapter m_Adapter = null;
    /*********************** Controller ***********************/
    private RecyclerView m_RecyclerView = null;
    //----------------------------------------------------
    //
    public FragStorage()
    {

    }

    public static FragStorage newInstance()
    {
        if(instance == null)
            instance = new FragStorage();

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
        View view = inflater.inflate(R.layout.frag_storage, container, false);
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
    //------------------------------------------------------------
    //
    public void initValue(){

    }
    //------------------------------------------------------------
    //
    public void initLayout(View pView)
    {
        m_RecyclerView = (RecyclerView)pView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        m_RecyclerView.setLayoutManager(layoutManager);

        JSONArray pArray = null;
        String strSavedData = m_App.m_SpCtrl.getImageUrlJsonData();
        if(strSavedData.length() > 0)
        {
            try {
                pArray = new JSONArray(strSavedData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            m_Adapter = new StorageViewAdapter(m_App, m_Context, pArray);
            m_RecyclerView.setAdapter(m_Adapter);
        }
    }

    //------------------------------------------------------------
    //
    public void addData(JSONObject jsonObj)
    {
        if(jsonObj != null)
        {
            m_Adapter.addData(jsonObj);
        }
    }

}
