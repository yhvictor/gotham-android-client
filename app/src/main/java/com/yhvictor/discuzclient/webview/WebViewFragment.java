package com.yhvictor.discuzclient.webview;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.application.DiscuzClientApplication;
import com.yhvictor.discuzclient.debug.Logger;

public class WebViewFragment extends Fragment {
  private WebView webView;
  private ProgressBar progressBar;
  private String startUrl;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    DiscuzClientApplication.appComponent.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_webview, container, false);
    webView = view.findViewById(R.id.webview);
    configWebView(webView);

    progressBar = view.findViewById(R.id.progress_bar);
    progressBar.setVisibility(View.GONE);

    return view;
  }

  @Override
  public void onDestroyView() {
    webView.destroy();
    webView = null;
    progressBar = null;

    super.onDestroyView();
  }

  private void configWebView(WebView webView) {
    CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(new DiscuzWebViewClient());
    webView.setWebChromeClient(new DiscuzWebChromeClient());

    if (startUrl != null) {
      webView.loadUrl(startUrl);
    }
  }

  public void loadUrl(String url) {
    this.startUrl = url;
    if (webView != null) {
      webView.loadUrl(startUrl);
    }
  }

  private class DiscuzWebChromeClient extends WebChromeClient {

    @Override
    public void onProgressChanged(WebView view, int progress) {
      if (progressBar == null) {
        return;
      }

      progressBar.setProgress(progress);

      if (progress < 100) {
        progressBar.setVisibility(View.VISIBLE);
      } else {
        progressBar.setVisibility(View.GONE);
      }
    }
  }

  private class DiscuzWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      Logger.d("1: " + url);
      if ("forumdisplay".equals(Uri.parse(url).getQueryParameter("mod"))) {
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
      }
      return false;
    }
  }
}
