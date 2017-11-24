package me.ivmg.dekavoice.converter;

import java.io.File;

public interface ConverterCallback {
    void onSuccess(File file);
    void onError(String error);
    void onFinish();
;}
