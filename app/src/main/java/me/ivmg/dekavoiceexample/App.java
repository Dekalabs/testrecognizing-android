package me.ivmg.dekavoiceexample;

import android.app.Application;

import me.ivmg.DekaVoice;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DekaVoice.init(this, "C7L5MV2RZGHU54ARAMLYS7GVPXOXJ75Z");
    }

}
