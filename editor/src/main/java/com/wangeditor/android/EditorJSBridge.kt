package com.wangeditor.android;

import android.webkit.JavascriptInterface;
import java.lang.ref.WeakReference;


public class EditorJSBridge extends Object {
    private WeakReference<IEditorWeb> iWebWeakReference;

    public EditorJSBridge(IEditorWeb web) {
        this.iWebWeakReference = new WeakReference<>(web);
    }

    @JavascriptInterface
    public void onStyleChange(String content) {
        IEditorWeb ihwWeb = iWebWeakReference.get();
        if (ihwWeb == null) return;
        ihwWeb.onStyleChange(content);
    }

    @JavascriptInterface
    public void onContentChange(String content) {
        IEditorWeb ihwWeb = iWebWeakReference.get();
        if (ihwWeb == null) return;
        ihwWeb.onContentChange(content);
    }


}

