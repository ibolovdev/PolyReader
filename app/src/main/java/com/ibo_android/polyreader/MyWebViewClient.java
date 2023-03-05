package com.ibo_android.polyreader;

import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MyWebViewClient extends WebViewClient
{


    private WebView mWebView;

    @Override
    public boolean onRenderProcessGone(WebView view,
                                       RenderProcessGoneDetail detail) {



        if (view !=null) {
          //  view.removeallviews();
            view.destroy ();
            view=null;
           // ll_webview.removeallviews ();
           // ll_webview=null;
        }



      /*  if (!detail.didCrash()) {
            // Renderer was killed because the system ran out of memory.
            // The app can recover gracefully by creating a new WebView instance
            // in the foreground.
            Log.e("MY_APP_TAG", "System killed the WebView rendering process " +
                    "to reclaim memory. Recreating...");

            if (mWebView != null) {
                ViewGroup webViewContainer =
                        (ViewGroup) findViewById(R.id.my_web_view_container);
                webViewContainer.removeView(mWebView);
                mWebView.destroy();
                mWebView = null;
            }

            // By this point, the instance variable "mWebView" is guaranteed
            // to be null, so it's safe to reinitialize it.

            return true; // The app continues executing.
        }*/

        // Renderer crashed because of an internal error, such as a memory
        // access violation.
     //   Log.e("MY_APP_TAG", "The WebView rendering process crashed!");

        // In this example, the app itself crashes after detecting that the
        // renderer crashed. If you choose to handle the crash more gracefully
        // and allow your app to continue executing, you should 1) destroy the
        // current WebView instance, 2) specify logic for how the app can
        // continue executing, and 3) return "true" instead.
        return false;
    }


    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
        Toast.makeText(view.getContext(), description, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);
        return true;
    }





}
