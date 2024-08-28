import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 这个问题是在做财报解读的时候遇到的，我们要从恒生库拉取A股公司的公告url，在我们的oss上保存一份，并上传点金平台做embedding。
 * 大部分公告可以支持直接拉取，但是部分公告拉取失败，获取到的是一个html而不是pdf。盘了一下发现这种url的请求会在请求头里带上一
 * 个cookie，cookie的内容出现地莫名其妙，于是猜测是js生成，拿那个html文件抽丝剥茧后发现有一个js方法，经过处理后如下JS变量所示。
 * 请求还会返回一个随机字符串，看起来像是这个js方法的参数，在java里调用方法执行js获取到这个cookie的内容后塞入请求头再拉取则成功。
 * 需要添加几个依赖：
 * https://mvnrepository.com/artifact/org.openjdk.nashorn/nashorn-core
 * implementation 'org.openjdk.nashorn:nashorn-core:15.4'
 * https://mvnrepository.com/artifact/org.ow2.asm/asm
 * implementation 'org.ow2.asm:asm:9.5'
 * https://mvnrepository.com/artifact/org.ow2.asm/asm-util
 * implementation 'org.ow2.asm:asm-util:9.5'
 */
public class FinancialCrawl {
    private static String JS = "var _0x4818=function(name, arg1){\n" +
            "  var _0x3e9e=['c3BsaXQ=','c2xpY2U=','dG9TdHJpbmc=','c2V0VGltZQ==','Z2V0VGltZQ==','Y29va2ll','YWN3X3NjX192Mj0=','O2V4cGlyZXM9','dG9HTVRTdHJpbmc=','O21heC1hZ2U9MzYwMDtwYXRoPS8=','MzAwMDE3NjAwMDg1NjAwNjA2MTUwMTUzMzAwMzY5MDAyNzgwMDM3NQ==','bGVuZ3Ro','am9pbg==','MXw0fDN8MHwy'];(function(_0x2d8f05,_0x4b81bb){var _0x4d74cb=function(_0x32719f){while(--_0x32719f){_0x2d8f05['push'](_0x2d8f05['shift']());}};var _0x33748d=function(){var _0x3e4c21={'data':{'key':'cookie','value':'timeout'},'setCookie':function(_0x5c685e,_0x3e3156,_0x1e9e81,_0x292610){_0x292610=_0x292610||{};var _0x151bd2=_0x3e3156+'='+_0x1e9e81;var _0x558098=0x0;for(var _0x558098=0x0,_0x230f38=_0x5c685e['length'];_0x558098<_0x230f38;_0x558098++){var _0x948b6c=_0x5c685e[_0x558098];_0x151bd2+=';\\x20'+_0x948b6c;var _0x29929c=_0x5c685e[_0x948b6c];_0x5c685e['push'](_0x29929c);_0x230f38=_0x5c685e['length'];if(_0x29929c!==!![]){_0x151bd2+='='+_0x29929c;}}_0x292610['cookie']=_0x151bd2;},'removeCookie':function(){return'dev';},'getCookie':function(_0x5dd881,_0x550fbc){_0x5dd881=_0x5dd881||function(_0x18d5c9){return _0x18d5c9;};var _0x4ce2f1=_0x5dd881(new RegExp('(?:^|;\\x20)'+_0x550fbc['replace'](/([.$?*|{}()[]\\/+^])/g,'$1')+'=([^;]*)'));var _0x333808=function(_0x432180,_0x2ab90b){_0x432180(++_0x2ab90b);};_0x333808(_0x4d74cb,_0x4b81bb);return _0x4ce2f1?decodeURIComponent(_0x4ce2f1[0x1]):undefined;}};var _0x991246=function(){var _0x981158=new RegExp('\\x5cw+\\x20*\\x5c(\\x5c)\\x20*{\\x5cw+\\x20*[\\x27|\\x22].+[\\x27|\\x22];?\\x20*}');return _0x981158['test'](_0x3e4c21['removeCookie']['toString']());};_0x3e4c21['updateCookie']=_0x991246;var _0x57b080='';var _0x219af0=_0x3e4c21['updateCookie']();if(!_0x219af0){_0x3e4c21['setCookie'](['*'],'counter',0x1);}else if(_0x219af0){_0x57b080=_0x3e4c21['getCookie'](null,'counter');}else{_0x3e4c21['removeCookie']();}};_0x33748d();}(_0x3e9e,0x176));var _0x1e8e=function(_0x558645,_0x3571ed){_0x558645=_0x558645-0x0;var _0x23d32b=_0x3e9e[_0x558645];if(_0x1e8e['jweSQB']===undefined){(function(){var _0x2a4aae;try{var _0x1ac753=Function('return\\x20(function()\\x20'+'{}.constructor(\\x22return\\x20this\\x22)(\\x20)'+');');_0x2a4aae=_0x1ac753();}catch(_0x267ba9){_0x2a4aae=window;}var _0x22c6cf='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';_0x2a4aae['atob']||(_0x2a4aae['atob']=function(_0xb01b66){var _0x112e38=String(_0xb01b66)['replace'](/=+$/,'');for(var _0x315811=0x0,_0x196945,_0x8ee65b,_0x111e6b=0x0,_0x2a5e7f='';_0x8ee65b=_0x112e38['charAt'](_0x111e6b++);~_0x8ee65b&&(_0x196945=_0x315811%0x4?_0x196945*0x40+_0x8ee65b:_0x8ee65b,_0x315811++%0x4)?_0x2a5e7f+=String['fromCharCode'](0xff&_0x196945>>(-0x2*_0x315811&0x6)):0x0){_0x8ee65b=_0x22c6cf['indexOf'](_0x8ee65b);}return _0x2a5e7f;});}());_0x1e8e['VidPVs']=function(_0x539abf){var _0x126fa5=atob(_0x539abf);var _0x54d768=[];for(var _0x3d3645=0x0,_0x4289fc=_0x126fa5['length'];_0x3d3645<_0x4289fc;_0x3d3645++){_0x54d768+='%'+('00'+_0x126fa5['charCodeAt'](_0x3d3645)['toString'](0x10))['slice'](-0x2);}return decodeURIComponent(_0x54d768);};_0x1e8e['BXvRsu']={};_0x1e8e['jweSQB']=!![];}var _0x436197=_0x1e8e['BXvRsu'][_0x558645];if(_0x436197===undefined){var _0x4f4121=function(_0x5e2adc){this['nlcXFw']=_0x5e2adc;this['HAmvBE']=[0x1,0x0,0x0];this['YFWLey']=function(){return'newState';};this['YpNXEl']='\\x5cw+\\x20*\\x5c(\\x5c)\\x20*{\\x5cw+\\x20*';this['JsKhOp']='[\\x27|\\x22].+[\\x27|\\x22];?\\x20*}';};_0x4f4121['prototype']['pzRiIQ']=function(){var _0x3e581e=new RegExp(this['YpNXEl']+this['JsKhOp']);var _0x13a005=_0x3e581e['test'](this['YFWLey']['toString']())?--this['HAmvBE'][0x1]:--this['HAmvBE'][0x0];return this['gaiPha'](_0x13a005);};_0x4f4121['prototype']['gaiPha']=function(_0x1e6387){if(!Boolean(~_0x1e6387)){return _0x1e6387;}return this['hpKQFb'](this['nlcXFw']);};_0x4f4121['prototype']['hpKQFb']=function(_0x20dc19){for(var _0x19d402=0x0,_0x5a3818=this['HAmvBE']['length'];_0x19d402<_0x5a3818;_0x19d402++){this['HAmvBE']['push'](Math['round'](Math['random']()));_0x5a3818=this['HAmvBE']['length'];}return _0x20dc19(this['HAmvBE'][0x0]);};new _0x4f4121(_0x1e8e)['pzRiIQ']();_0x23d32b=_0x1e8e['VidPVs'](_0x23d32b);_0x1e8e['BXvRsu'][_0x558645]=_0x23d32b;}else{_0x23d32b=_0x436197;}return _0x23d32b;};var _0x52bd4a=function(){var _0x56121a=!![];return function(_0x215040,_0x309e1a){var _0x23d8c2=_0x56121a?function(){if(_0x309e1a){var _0x1d7a3f=_0x309e1a['apply'](_0x215040,arguments);_0x309e1a=null;return _0x1d7a3f;}}:function(){};_0x56121a=![];return _0x23d8c2;};}();var _0x1297ed=_0x52bd4a(this,function(){var _0x31f094=function(){return'\\x64\\x65\\x76';},_0x114f69=function(){return'\\x77\\x69\\x6e\\x64\\x6f\\x77';};var _0x21d55e=function(){var _0x4b4425=new RegExp('\\x5c\\x77\\x2b\\x20\\x2a\\x5c\\x28\\x5c\\x29\\x20\\x2a\\x7b\\x5c\\x77\\x2b\\x20\\x2a\\x5b\\x27\\x7c\\x22\\x5d\\x2e\\x2b\\x5b\\x27\\x7c\\x22\\x5d\\x3b\\x3f\\x20\\x2a\\x7d');return!_0x4b4425['\\x74\\x65\\x73\\x74'](_0x31f094['\\x74\\x6f\\x53\\x74\\x72\\x69\\x6e\\x67']());};var _0x2328d0=function(){var _0x56d0ca=new RegExp('\\x28\\x5c\\x5c\\x5b\\x78\\x7c\\x75\\x5d\\x28\\x5c\\x77\\x29\\x7b\\x32\\x2c\\x34\\x7d\\x29\\x2b');return _0x56d0ca['\\x74\\x65\\x73\\x74'](_0x114f69['\\x74\\x6f\\x53\\x74\\x72\\x69\\x6e\\x67']());};var _0x29c9ca=function(_0x523426){var _0x17ebab=~-0x1>>0x1+0xff%0x0;if(_0x523426['\\x69\\x6e\\x64\\x65\\x78\\x4f\\x66']('\\x69'===_0x17ebab)){_0x442ac7(_0x523426);}};var _0x442ac7=function(_0x10471a){var _0x4d91ed=~-0x4>>0x1+0xff%0x0;if(_0x10471a['\\x69\\x6e\\x64\\x65\\x78\\x4f\\x66']((!![]+'')[0x3])!==_0x4d91ed){_0x29c9ca(_0x10471a);}};if(!_0x21d55e()){if(!_0x2328d0()){_0x29c9ca('\\x69\\x6e\\x64\\u0435\\x78\\x4f\\x66');}else{_0x29c9ca('\\x69\\x6e\\x64\\x65\\x78\\x4f\\x66');}}else{_0x29c9ca('\\x69\\x6e\\x64\\u0435\\x78\\x4f\\x66');}});_0x1297ed();var posList=[0xf,0x23,0x1d,0x18,0x21,0x10,0x1,0x26,0xa,0x9,0x13,0x1f,0x28,0x1b,0x16,0x17,0x19,0xd,0x6,0xb,0x27,0x12,0x14,0x8,0xe,0x15,0x20,0x1a,0x2,0x1e,0x7,0x4,0x11,0x5,0x3,0x1c,0x22,0x25,0xc,0x24];var mask=_0x1e8e('0x0');var outPutList=[];var arg2='';var arg3='';for(var i=0x0;i<arg1[_0x1e8e('0x1')];i++){var this_i=arg1[i];for(var j=0x0;j<posList[_0x1e8e('0x1')];j++){if(posList[j]==i+0x1){outPutList[j]=this_i;}}}arg2=outPutList[_0x1e8e('0x2')]('');for(var i=0x0;i<arg2[_0x1e8e('0x1')]&&i<mask[_0x1e8e('0x1')];i+=0x2){var GxjQsM=_0x1e8e('0x3')[_0x1e8e('0x4')]('|'),QoWazb=0x0;while(!![]){switch(GxjQsM[QoWazb++]){case'0':if(xorChar[_0x1e8e('0x1')]==0x1){xorChar='0'+xorChar;}continue;case'1':var strChar=parseInt(arg2[_0x1e8e('0x5')](i,i+0x2),0x10);continue;case'2':arg3+=xorChar;continue;case'3':var xorChar=(strChar^maskChar)[_0x1e8e('0x6')](0x10);continue;case'4':var maskChar=parseInt(mask[_0x1e8e('0x5')](i,i+0x2),0x10);continue;}break;}}var expiredate=new Date();expiredate[_0x1e8e('0x7')](expiredate[_0x1e8e('0x8')]()+0xe10*0x3e8);var theHost='',theHostSplit=theHost.split(\".\"),theHostSplitLength=theHostSplit.length;!/^(\\d+\\.)*\\d+$/.test(theHost)&&theHostSplitLength>2&&(\"com.cn\"!=(theHost=theHostSplit[theHostSplitLength-2]+\".\"+theHostSplit[theHostSplitLength-1])&&\"gov.cn\"!=theHost&&\"org.cn\"!=theHost&&\"net.cn\"!=theHost&&\"com.my\"!=theHost||(theHost=theHostSplit[theHostSplitLength-3]+\".\"+theHost));return arg3;}";

    public static void downloadFile2(String downloadUrl, String path){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            String cookie = buildCookie(downloadUrl);
            System.out.println("cookie:"+cookie);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("host", "static.sse.com.cn");
            connection.setRequestProperty("upgrade-insecure-requests", "1");
            connection.setRequestProperty("cache-control", "max-age=0");
            int responseCode = connection.getResponseCode();
            inputStream = new BufferedInputStream(connection.getInputStream());
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 1024 * 5];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }


    private static String buildCookie(String url) {
        String cookie = "";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            // 获取响应的HTML内容
            String htmlContent = response.body().string();
            StringBuilder sb = new StringBuilder();
            boolean flag = false;
            for (char c : htmlContent.toCharArray()) {
                if (c == '\'') {
                    flag = !flag;
                }
                if (flag) {
                    sb.append(c);
                }
                if (c == ';') break;
            }
            List<String> headers = response.headers("Set-Cookie");
            for (String header : headers) {cookie += header+";";}
            var manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            String tempStr = sb.toString();
            String arg1 = tempStr.substring(1);
            String executeJs = String.format("_0x4818(\"acw_sc__v2\", '%s')", arg1);
            engine.eval(JS);
            Object result = engine.eval(executeJs);
            cookie += "acw_sc__v2=" + result.toString();
            return cookie;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
