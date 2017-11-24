package me.ivmg;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;

import me.ivmg.dekavoice.TranscribeCallback;
import me.ivmg.dekavoice.converter.ConverterCallback;
import me.ivmg.dekavoice.converter.MediaConverter;
import me.ivmg.dekavoice.service.NetworkClient;
import me.ivmg.dekavoice.service.VoiceResult;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DekaVoice {

    private static DekaVoice instance = null;
    private static NetworkClient client = null;

    private DekaVoice() {
    }

    public static DekaVoice getInstance() {
        return instance;
    }

    public static void init(Context context, @NonNull  String apiKey) {
        instance = new DekaVoice();
        client = new NetworkClient(apiKey);

        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void transcribeVideo(Context context, File videoFile, final TranscribeCallback callback) {
        try {
            MediaConverter.convertVideoToAudio(context, videoFile, new ConverterCallback() {
                @Override
                public void onSuccess(File file) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("audio/wav"), file);
                    NetworkClient.getInstance().getTranscription(requestBody).enqueue(new Callback<VoiceResult>() {
                        @Override
                        public void onResponse(@NonNull Call<VoiceResult> call, @NonNull retrofit2.Response<VoiceResult> response) {
                            if (response.body() == null) {
                                callback.onError(response.message());
                            } else {
                                callback.onSuccess(response.body().getText());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<VoiceResult> call, @NonNull Throwable t) {
                            callback.onError(t.getMessage());
                            callback.onFinish();
                        }
                    });

                }

                @Override
                public void onError(String error) {
                    callback.onError(error);
                    callback.onFinish();
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            callback.onError(e.getMessage());
        }
    }
}
