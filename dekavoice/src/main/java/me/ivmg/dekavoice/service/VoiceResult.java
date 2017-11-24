package me.ivmg.dekavoice.service;

import com.google.gson.annotations.SerializedName;

public class VoiceResult {
    @SerializedName("_text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
