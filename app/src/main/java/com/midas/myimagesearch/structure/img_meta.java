package com.midas.myimagesearch.structure;

import com.google.gson.annotations.SerializedName;

public class img_meta
{
    @SerializedName("total_count")
    public Integer total_count;//검색어에 검색된 문서수
    @SerializedName("pageable_count")
    public Integer pageable_count;//total_count 중에 노출가능 문서수
    @SerializedName("is_end")
    public Boolean is_end;//현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음.
}
