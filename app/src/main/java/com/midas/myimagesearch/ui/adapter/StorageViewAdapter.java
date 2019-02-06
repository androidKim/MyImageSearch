package com.midas.myimagesearch.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.midas.myimagesearch.MyApp;
import com.midas.myimagesearch.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StorageViewAdapter extends RecyclerView.Adapter<StorageViewAdapter.ViewHolder>
{
    /*********************** define ***********************/
    /*********************** member ***********************/
    private MyApp m_App = null;
    private Context m_Context = null;
    private JSONArray m_Items;    // 아이템 리스트
    private RequestManager m_RequestManager = null;//glide request manager

    /*********************** controler ***********************/


    /*********************** constructor ***********************/
    //------------------------------------------------------------
    // Provide a suitable constructor (depends on the kind of dataset)
    public StorageViewAdapter(MyApp myApp, Context pContext, JSONArray pArray)
    {
        m_App = myApp;
        m_Context = pContext;
        m_Items = pArray;
        m_RequestManager = Glide.with(m_Context);
    }
    /*********************** system function ***********************/
    //------------------------------------------------------------
    // Create new views (invoked by the layout manager)
    @Override
    public StorageViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
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
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj = m_Items.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonObj == null)
            return;

        if(jsonObj != null)//이미지검색결과..
        {
            String url = "";
            try {
                url = jsonObj.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(url.length() > 0)
            {
                m_RequestManager.load(url).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                }).into(holder.iv_Item);
            }
        }
    }
    //------------------------------------------------------------
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return m_Items.length();
    }

    /*********************** user function ***********************/
    //------------------------------------------------------------
    //
    public void addData(JSONObject jsonObj)
    {
        if(jsonObj != null)
        {
            m_Items.put(jsonObj);
            notifyDataSetChanged();
        }
    }
    //------------------------------------------------------------
    //
    public void removeAllData()
    {
        if(m_Items != null)
        {
            m_Items = new JSONArray();
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
        public ViewHolder(View pView, ImageView pImageView, LinearLayout pLinearLayout)
        {
            super(pView);
            v_Row = pView;
            iv_Item = pImageView;
        }
    }
}