package com.yhvictor.discuzclient.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {
  WebView discuzWebView;

  public static void startWebViewActivity(Context context, Uri uri) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setClassName(context.getPackageName(), WebViewActivity.class.getCanonicalName());
    intent.setDataAndType(uri, "text/html");
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    discuzWebView = new WebView(this);
    setContentView(discuzWebView);

    Uri uri = getIntent().getData();
    discuzWebView.loadUrl(uri.toString());
  }

  @Override
  public void onBackPressed() {
    if (discuzWebView.canGoBack()) {
      discuzWebView.goBack();
    } else {
      super.onBackPressed();
    }
  }
}
