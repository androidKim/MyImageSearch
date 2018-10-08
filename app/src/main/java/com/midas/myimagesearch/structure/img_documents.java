package com.midas.myimagesearch.structure;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class img_documents implements Serializable
{
    public img_documents()
    {

    }

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
}
