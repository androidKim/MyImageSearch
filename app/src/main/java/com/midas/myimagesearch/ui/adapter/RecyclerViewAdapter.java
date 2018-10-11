package com.midas.myimagesearch.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.midas.myimagesearch.R;
import com.midas.myimagesearch.common.Constant;
import com.midas.myimagesearch.structure.img_documents;
import com.midas.myimagesearch.ui.act.ActDetail;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    /*********************** define ***********************/
    /*********************** member ***********************/
    private Context m_Context = null;
    private ArrayList<img_documents> m_Items;    // 아이템 리스트

    /*********************** controler ***********************/


    /*********************** constructor ***********************/
    //------------------------------------------------------------
    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(Context pContext, ArrayList<img_documents> pArray)
    {
        m_Context = pContext;
        m_Items = pArray;
    }
    /*********************** system function ***********************/
    //------------------------------------------------------------
    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View pView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        SimpleDraweeView pSimpleDraweeView= (SimpleDraweeView) pView.findViewById(R.id.iv_Item);
        ViewHolder vh = new ViewHolder(pView, pSimpleDraweeView);
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

        if(pInfo.image_url != null)
        {
            if(pInfo.image_url.length() > 0)
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(pInfo.getBitmap() != null)
                        {
                            Bitmap bitmap = pInfo.getBitmap();
                            float width = bitmap.getWidth();
                            float height = bitmap.getHeight();
                            final float ratio = width / height;
                            //UIThread..
                            ((Activity)m_Context).runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    holder.iv_Item.setImageURI(Uri.parse(pInfo.image_url));
                                    holder.iv_Item.setAspectRatio(ratio);
                                    holder.iv_Item.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        else
                        {
                            try
                            {
                                URL url = new URL(pInfo.image_url);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = false;
                                options.inSampleSize = 4;
                                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null ,options);
                                if(bitmap != null)
                                {
                                    pInfo.setBitmap(bitmap);

                                    float width = bitmap.getWidth();
                                    float height = bitmap.getHeight();
                                    final float ratio = width / height;
                                    //UIThread..
                                    ((Activity)m_Context).runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            holder.iv_Item.setImageURI(Uri.parse(pInfo.image_url));
                                            holder.iv_Item.setAspectRatio(ratio);
                                            holder.iv_Item.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                            catch(IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
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
        public SimpleDraweeView iv_Item;
        public ViewHolder(View pView, SimpleDraweeView pSimpleDraweeView)
        {
            super(pView);
            v_Row = pView;
            iv_Item = pSimpleDraweeView;
        }
    }
}