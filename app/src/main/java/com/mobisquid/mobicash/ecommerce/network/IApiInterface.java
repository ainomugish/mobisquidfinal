package com.mobisquid.mobicash.ecommerce.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Zeera on 7/3/2017 bt ${File}
 */

public interface IApiInterface {
    @GET(APIsEndPoints.SEARCH_PRODUCT)
    Call<ResponseBody> searchProducts(@Query("SearchTerm") String term, @Query("NoOfRows") int noOfRows
            , @Query("ApplicationId") String applicationId, @Query("LastProductId") Integer lastProductId);

    @GET(APIsEndPoints.GET_PARENT_CATEGORIES)
    Call<ResponseBody> getCategories(@Query("ApplicationId") String applicationId,
                                     @Query("NoOfRows") int numberOfRows,
                                     @Query("LastParentCategoryId") Integer lastItemId);

    @GET(APIsEndPoints.GET_SUB_CATEGORIES)
    Call<ResponseBody> getCategoriesWRTParent(@Query("ApplicationId") String applicationId,
                                              @Query("ParentId") int parentCategoryId,
                                              @Query("NoOfRows") int numberOfRows,
                                              @Query("LastSubCategoryId") Integer lastItemId);

    @GET(APIsEndPoints.GET_PRODUCTS)
    Call<ResponseBody> getProductByCategory(@Query("ApplicationId") String applicationId,
                                            @Query("CategoryId") int parentCategoryId,
                                            @Query("NoOfRows") int numberOfRows,
                                            @Query("LastProductId") Integer lastItemId);

    @POST(APIsEndPoints.CHECK_ACCOUNT)
    @FormUrlEncoded
    Call<ResponseBody> payAmount(@Field("client") String phoneNumber, @Field("pin") String pin,
                                 @Field("merchantAccount") String merchantAccount, @Field("amount") double amount,
                                 @Field("details") String details);

}
