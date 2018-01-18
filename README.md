# NEWSCREEN_WEB_SWITCH
* 뉴스크린 스위치를 웹에서 연동하기 위한 설명서
* 네이티브로 제작된 앱이라면 문제가 없겠지만 All WebView로 제작된 앱이라면 뉴스크린 스위치를 삽입하기 어려운점을 보완하기위한 코드
* 기존 뉴스크린SDK(https://github.com/HNSCommunication/NEWSCREEN-SDK-Publisher)를 프로젝트에 연동 한 후 진행 합니다.

## 1. 기본 파일

#### 2개 파일을 확인합니다.
- newscreen_switch.html : 웹 스위치 html코드
- WebviewActivity.java : 웹 스위치의 동작을 받아줄 예시 클래스

## 2. 웹 html
스위치를 노출할 영역에 `newscreen_switch.html` 코드를 삽입합니다.
html코드와 script코드 모두 삽입합니다.

## 3. 앱 class
뉴스크린 스위치를 호출하는 웹뷰가 있는 클래스에 `WebviewActivity.java`의 설정사항을 추가 합니다.
웹 스위치의 동작을 받아줄 함수와 설정코드가 있습니다 

```Java
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
  //웹뷰 설정~
  ...

  mWebView.getSettings().setJavaScriptEnabled(true);  //자바스크립트 enable
  mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");  //자바스크립트에서 WebAppInterface 클래스의 함수를 실행시키기 위한 설정
  
  ...
 }

 public class WebAppInterface {  //자바스크립트에서 함수를 실행시키는 영역입니다
     Context mContext;
     NewscreenAD newscreenAD = new NewscreenAD(getApplicationContext());  //뉴스크린 정의

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

```
