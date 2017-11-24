package me.ivmg.dekavoice;

public interface TranscribeCallback {
    void onSuccess(String result);
    void onError(String error);
    void onFinish();
}
