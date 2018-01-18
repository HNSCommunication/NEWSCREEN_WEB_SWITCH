

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.tnplanet.newscreen_sdk.NewscreenAD;

/**
 *      해당 클래스는 예시를 위해 작성한 클래스 입니다. 설정에 따른 에러가 존재합니다.
 */

public class WebviewActivity extends Activity {
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        mWebView.getSettings().setJavaScriptEnabled(true);  //자바스크립트 enable
        
        //자바스크립트에서 WebAppInterface 클래스의 함수를 실행시키기 위한 설정입니다.
        //"Android" 네임은 javascript에서 'Android.StartNewscreen()' 과 같이 함수를 실행시키기 위한 앞부분의 구분자 사용됩니다.
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");  

    }

    public class WebAppInterface {  //자바스크립트에서 함수를 실행시키는 영역입니다
        Context mContext;
        NewscreenAD newscreenAD = new NewscreenAD(getApplicationContext());     //뉴스크린 정의

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void StopNewscreen() {   //javascript Android.StopNewscreen()로 실행
            newscreenAD.stopAd();
        }

        @JavascriptInterface
        public void StartNewscreen() {  //javascript Android.StartNewscreen()로 실행
            newscreenAD.init("뉴스크린 담당자에게 발급받은 sdk_key");
        }

        @JavascriptInterface
        public boolean CheckNewscreen() {  //javascript Android.CheckNewscreen()로 실행
            return newscreenAD.isRunningNewscreen();
        }


    }
}
