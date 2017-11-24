package me.ivmg.dekavoice.service;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
    @Headers("Content-Type: audio/wav")
    @POST("speech?v=20171123")
    Call<VoiceResult> getTranscription(@Body RequestBody wavFile);
}
