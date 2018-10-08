package com.midas.myimagesearch.core;

import com.midas.myimagesearch.common.Constant;
import com.midas.myimagesearch.structure.function.img_list.res_img_list;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface
{
    @Headers(Constant.WEB_HEADER_KAKAO_AUTH)
    @GET("/v2/search/image")
    Call<res_img_list> getImageListProc(@Query("query") String query,
                                        @Query("sort") String sort,
                                        @Query("page") Integer page,
                                        @Query("size") Integer size);
}
