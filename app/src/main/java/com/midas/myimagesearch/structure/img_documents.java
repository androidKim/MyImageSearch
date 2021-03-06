package com.midas.myimagesearch.structure;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class img_documents implements Serializable
{
    public img_documents()
    {

    }
    //img list
    @SerializedName("collection")
    public String collection;//컬렉션
    @SerializedName("thumbnail_url")
    public String thumbnail_url;//이미지 썸네일 URL
    @SerializedName("image_url")
    public String image_url;//이미지 URL
    @SerializedName("width")
    public Integer width;//이미지의 가로 크기
    @SerializedName("height")
    public Integer height;//이미지의 세로 크기
    @SerializedName("display_sitename")
    public String display_sitename;//출처명
    @SerializedName("doc_url")
    public String doc_url;//문서 URL
    @SerializedName("datetime")
    public String datetime;//문서 작성시간. ISO 8601. [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]


    //vod list
    @SerializedName("title")
    public String title;//동영상 제목
    @SerializedName("url")
    public String url;//동영상 링크
    @SerializedName("play_time")
    public String play_time;//동영상 재생시간
    @SerializedName("thumbnail")
    public String thumbnail;//동영상 썸네일 url
    @SerializedName("author")
    public String author;//동영상 업로더


    public long getDateTime()
    {
        if(datetime == null)
            return 0;

        String date = datetime.split("\\+")[0];//ex 2016-09-23T14:32:02.000+09:00
        date = date.replace("T", " ");
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(date);
            long timestamp = parsedDate.getTime();
            return timestamp;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }
}
