package com.example.notwebview;

import android.content.Context;
import android.view.View;

/**
 * Created by rahul on 12/10/15.
 */
public class NotWebView extends View {
    String html;
    public NotWebView(Context context,String html) {
        super(context);
        this.html=html;
    }

}
