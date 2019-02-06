package com.midas.myimagesearch.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.midas.myimagesearch.MyApp;
import com.midas.myimagesearch.R;
import com.midas.myimagesearch.common.Constant;
import com.midas.myimagesearch.structure.img_documents;
import com.midas.myimagesearch.ui.act.ActDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    /*********************** define ***********************/
    /*********************** member ***********************/
    private MyApp m_App = null;
    private Context m_Context = null;
    private ArrayList<img_documents> m_Items;    // 아이템 리스트
    private RequestManager m_RequestManager = null;//glide request manager
    private IfCallback m_IfCallback = null;

    /*********************** controler ***********************/


    /*********************** constructor ***********************/
    //------------------------------------------------------------
    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(MyApp myApp, Context pContext, ArrayList<img_documents> pArray, IfCallback pCallback)
    {
        m_App = myApp;
        m_Context = pContext;
        m_Items = pArray;
        m_RequestManager = Glide.with(m_Context);
        m_IfCallback = pCallback;
    }
    /*********************** system function ***********************/
    //------------------------------------------------------------
    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View pView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        ImageView pImageView= (ImageView)pView.findViewById(R.id.iv_Item);
        LinearLayout pLinearLayout = (LinearLayout)pView.findViewById(R.id.ly_Save);
        ViewHolder vh = new ViewHolder(pView, pImageView, pLinearLayout);
        return vh;
    }
    //------------------------------------------------------------
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final img_documents pInfo = m_Items.get(position);
        if(pInfo == null)
            return;

        holder.ly_Save.setVisibility(View.GONE);
        if(pInfo.thumbnail_url != null)//이미지검색결과..
        {
            if(pInfo.thumbnail_url.length() > 0)
            {
                m_RequestManager.load(pInfo.thumbnail_url).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.ly_Save.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(holder.iv_Item);
            }
        }
        else if(pInfo.thumbnail != null)//동영상검색결과
        {
            if(pInfo.thumbnail.length() > 0)
            {
                m_RequestManager.load(pInfo.thumbnail).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.ly_Save.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(holder.iv_Item);
            }
        }

        holder.v_Row.setTag(pInfo);
        holder.v_Row.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //go Detail..
                img_documents pInfo = (img_documents) view.getTag();
                if(pInfo == null)
                    return;

                Intent pIntent = new Intent(m_Context, ActDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.INTENT_DATA_IMGDOCUMENTS, pInfo);
                pIntent.putExtras(bundle);
                m_Context.startActivity(pIntent);
            }
        });

        //
        holder.ly_Save.setTag(pInfo);
        holder.ly_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_documents pInfo = (img_documents) view.getTag();
                if(pInfo == null)
                    return;

                //get Current Data..
                String strCurrentData = m_App.m_SpCtrl.getImageUrlJsonData();
                try {
                    JSONArray jsonArray = null;
                    if(strCurrentData.length() > 0)
                        jsonArray = new JSONArray(strCurrentData);
                    else
                        jsonArray = new JSONArray();

                    JSONObject jsonObj = new JSONObject();
                    if(pInfo.thumbnail_url != null)
                        jsonObj.put("url", pInfo.thumbnail_url);
                    else if(pInfo.thumbnail != null)
                        jsonObj.put("url", pInfo.thumbnail);

                    jsonArray.put(jsonObj);

                    m_App.m_SpCtrl.setImageUrlJsonData(jsonArray.toString());
                    Toast.makeText(m_Context, m_Context.getResources().getString(R.string.str_msg_2), Toast.LENGTH_SHORT).show();

                    if(m_IfCallback != null)
                        m_IfCallback.notifyAddData(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //------------------------------------------------------------
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return m_Items.size();
    }

    /*********************** user function ***********************/
    //------------------------------------------------------------
    //
    public void addData(ArrayList<img_documents> pArray)
    {
        if(pArray != null)
        {
            m_Items.addAll(pArray);
            notifyDataSetChanged();
        }
    }
    //------------------------------------------------------------
    //
    public void removeAllData()
    {
        if(m_Items != null)
        {
            m_Items.clear();
            notifyDataSetChanged();
        }
    }

    /*********************** inner class ***********************/
    //-------------------------------------------------------------
    //
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public View v_Row;
        public ImageView iv_Item;
        public LinearLayout ly_Save;
        public ViewHolder(View pView, ImageView pImageView, LinearLayout pLinearLayout)
        {
            super(pView);
            v_Row = pView;
            iv_Item = pImageView;
            ly_Save = pLinearLayout;
        }
    }

    /*********************** interface ***********************/
    //-------------------------------------------------------------
    //
    public interface IfCallback
    {
        void notifyAddData(JSONObject pObj);
    }
}