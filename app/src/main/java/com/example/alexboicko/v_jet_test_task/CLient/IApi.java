package com.example.alexboicko.v_jet_test_task.CLient;

import com.example.alexboicko.v_jet_test_task.Model.Pojo.NewsAPI.Everything;

import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApi {




    @GET("everything")
    Call<Everything> getSearchResultSortedBy(@Query("q") String query,
                                             @Query("sortBy") String sortBy,
                                             @Query("apiKey") String apiKey);

    @GET("everything")
    Call<Everything> getSearchResultFilteredByDateRange(@Query("q") String query,
                                                        @Query("from") String from,
                                                        @Query("to") String to,
                                                        @Query("apiKey") String apiKey);

    @GET("everything")
    Call<Everything> getSearchResultFilteredBySource(@Query("q") String query,
                                                     @Query("source") String source,
                                                     @Query("apiKey") String apiKey);

    @GET("everything")
    Call<Everything> getSearchResults (@Query("q") String query,
                                       @Query("language") String language,
                                       @Query("apiKey") String apiKey);

    @Deprecated
    @GET("everything?from={from}&to={to}")
    Call<Everything> getSearchResultFilteredByDateRangeDeprecate(@Path("from") String fromDate,
                                                                 @Path("to") String toDate,
                                                                 @Query("q") String query,
                                                                 @Query("apiKey") String apiKey);
    @Deprecated
    @GET("everything?source={source}")
    Call<Everything> getSearchResultFilteredBySourceDeprecate(@Path("source") String source,
                                                              @Query("q") String query,
                                                              @Query("apiKey") String apiKey);
    @Deprecated
    @GET("everything?sortBy=popularity")
    Call<Everything> getSearchResultSortedByPopularity(@Query("q") String query,
                                                       @Query("apiKey") String apiKey);
    @Deprecated
    @GET("everything?sortBy=publishedAt")
    Call<Everything> getSearchResultSortedByPublishedAt(@Query ("q") String query,
                                                        @Query("apiKey") String apiKey);



}
