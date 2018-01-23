# NEWSCREEN_WEB_SWITCH
* 뉴스크린 스위치를 웹에서 연동하기 위한 설명서
* 네이티브로 제작된 앱이라면 문제가 없겠지만 All WebView로 제작된 앱이라면 뉴스크린 스위치를 삽입하기 어려운점을 보완하기위한 코드
* 기존 뉴스크린SDK( https://github.com/HNSCommunication/NEWSCREEN-SDK-Publisher )를 프로젝트에 연동 한 후 진행 합니다.

## 1. 기본 파일

#### 2개 파일을 확인합니다.
- newscreen_switch.html : 웹 스위치 html코드
- WebviewActivity.java : 웹 스위치의 동작을 받아줄 예시 클래스

## 2. 웹 html
스위치를 노출할 영역에 `newscreen_switch.html` 코드를 삽입합니다.
html코드와 script코드 모두 삽입합니다.
```html
<!--
        start 뉴스크린 스위치 
-->
<!--meta 태그의 charset 및 viewport 설정은 안드로이드 웹뷰에서 페이지가 모바일 사이즈로 한글 꺠짐없이 표현되기 위한 처리 입니다. 이미 추가되어있다면 생략하셔도 됩니다-->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<!--jquery로 처리 되었습니다. 이미 추가되어있다면 생략하셔도 됩니다-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!--뉴스크린 웹 스위치 영역 입니다. 그대로 사용하셔도 되고, 커스텀 디자인을 입히셔도 됩니다.-->
<div style="background-color: #ffffff; border: 1px solid #ececec; height:45px; padding: 5px 20px 0 20px;overflow: hidden;" >
    <div style="float:left; left:10%; height:45px; text-align: left;">
        <span style="color: #818181; font-weight: bold; font-size: 17px;">뉴스크린 </span><br>
        <span style="color: #909090; font-size: 12px;">뉴스크린 뉴스를 실시간으로 제공합니다.</span>
    </div>
    <div style="float:left; height:45px;">
        <img id="newscreen_onoff_switch_back_img" style="position:absolute; right:15px; width:60px;" src="http://ad.news-screen.com/v7/images/onoff_switch_back_gray.png" onclick="newscreen_service_on_off();" >
        <img id="newscreen_onoff_switch_toggle" style="position:absolute; right:39px; width:41px;" src="http://ad.news-screen.com/v7/images/onoff_switch_toggle.png" onclick="newscreen_service_on_off();">
    </div>
    <input type="hidden" id="newscreen_onoff_switch_state" value="off" />
</div>

<script>
$(document).ready(function() {
    newscreen_service_check();
});
        
//뉴스크린 실행 여부 확인 
function newscreen_service_check(){
    if(navigator.userAgent.toLowerCase().match(/android/)){
        var check_newscreen = Android.CheckNewscreen();     //안드로이드 뉴스크린 동작여부 확인 함수 호출
        if(check_newscreen == true){
            $("#newscreen_onoff_switch_state").val("on");
            $('#newscreen_onoff_switch_toggle').css("right","10px");
            $("#newscreen_onoff_switch_back_img").attr("src","http://ad.news-screen.com/v7/images/onoff_switch_back_blue.png");
        }
    }
}
//뉴스크린 실행 및 종료 함수
function newscreen_service_on_off(){
    if(navigator.userAgent.toLowerCase().match(/android/)){
        var onoff = $("#newscreen_onoff_switch_state").val();
        if(onoff == "on"){
            Android.StopNewscreen();           //안드로이드 뉴스크린 stop 함수호출
            $("#newscreen_onoff_switch_state").val("off");
            $('#newscreen_onoff_switch_toggle').animate({ 
                right: '39px', 
             }, 300, function() {
                  $("#newscreen_onoff_switch_back_img").attr("src","http://ad.news-screen.com/v7/images/onoff_switch_back_gray.png");
             });
        }else{
            Android.StartNewscreen();          //안드로이드 뉴스크린 start 함수호출
            $("#newscreen_onoff_switch_state").val("on");
            $('#newscreen_onoff_switch_toggle').animate({ 
                right: '10px', 
             }, 300, function() {
                  $("#newscreen_onoff_switch_back_img").attr("src","http://ad.news-screen.com/v7/images/onoff_switch_back_blue.png");
             });
        }
    }
}
</script>
<!--
        end 뉴스크린 스위치 
-->
```

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
