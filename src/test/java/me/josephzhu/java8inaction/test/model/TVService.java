package me.josephzhu.java8inaction.test.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by zhuye on 03/01/2017.
 */
public interface TVService
{
    @GET("tv/getCategory")
    Call<TVResult<List<TVCategory>>> getCategories(@Query("key") String key);

    @GET("tv/getChannel")
    Call<TVResult<List<TVChannel>>> getChannels(@Query("key") String key, @Query("pId") Integer pId);

    @GET("tv/getProgram")
    Call<TVResult<Map<String, String>>> getPrograms(@Query("key") String key, @Query("code") Integer code, @Query("date") String date);

}
