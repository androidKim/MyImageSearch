package com.midas.myimagesearch.core;

import com.midas.myimagesearch.common.Constant;
import com.midas.myimagesearch.structure.function.img_list.res_img_list;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface
{
    @Headers(Constant.WEB_HEADER_KAKAO_AUTH)
    @GET("/v2/search/image")
    Observable<res_img_list> getImageListProc(@Query("query") String query,
                                              @Query("sort") String sort,
                                              @Query("page") Integer page,
                                              @Query("size") Integer size);

    @Headers(Constant.WEB_HEADER_KAKAO_AUTH)
    @GET("/v2/search/vclip")
    Observable<res_img_list> getVodListProc(@Query("query") String query,
                                      @Query("sort") String sort,
                                      @Query("page") Integer page,
                                      @Query("size") Integer size);

}
