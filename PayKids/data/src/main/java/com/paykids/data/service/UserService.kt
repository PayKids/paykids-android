package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.UserInfoResponseDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserService {

    @GET("/user/info")
    suspend fun getUserInfo(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<UserInfoResponseDTO>>

    @Multipart
    @POST("/user/profile-image/change")
    suspend fun updateProfileImage(
        @Header("Authorization") accessToken: String,
        @Part file: MultipartBody.Part
    ): Response<BaseResponse<String>>

    @POST("/user/nickname/save")
    suspend fun saveNickname(
        @Header("Authorization") accessToken: String,
        @Query("nickname") nickname: String
    ): Response<BaseResponse<String>>

    @POST("/user/nickname/change")
    suspend fun updateNickname(
        @Header("Authorization") accessToken: String,
        @Query("newNickname") newNickname: String
    ): Response<BaseResponse<String>>

    @DELETE("/user/delete")
    suspend fun deleteUser(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<String>>

}