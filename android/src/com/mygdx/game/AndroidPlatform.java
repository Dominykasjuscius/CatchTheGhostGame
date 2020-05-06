package com.mygdx.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


/**
 * Created by ddomi on 20-Oct-17.
 */

public class AndroidPlatform implements NativePlatform {
    Context context;

    public AndroidPlatform(Activity context){
        this.context = context;
    }

    @Override
    public void StartActivity() {
        context.startActivity(new Intent(context, Main.class));
    }
}
