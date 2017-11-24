package me.ivmg.dekavoice.converter;

import android.content.Context;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.io.File;
import java.util.UUID;

public class MediaConverter {

    public static void convertVideoToAudio(Context context,
                                           File sourceFile,
                                           final ConverterCallback callback)
            throws FFmpegCommandAlreadyRunningException {

        FFmpeg ffmpeg = FFmpeg.getInstance(context);

        String fileName = UUID.randomUUID().toString() + ".wav";

        final String fullFilePath = context.getFilesDir().getAbsolutePath() + "/" + fileName;

        String textCommand = String.format("-i %s -f wav -ab 16000 -vn %s", sourceFile, fullFilePath);
        String[] command = textCommand.split(" ");

        ffmpeg.execute(command, new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                callback.onSuccess(new File(fullFilePath));
                callback.onFinish();
            }

            @Override
            public void onProgress(String message) {
            }

            @Override
            public void onFailure(String message) {
                callback.onError(message);
                callback.onFinish();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
