package com.example.CommonTest;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

public class DingTalkTest {


    public static void main(String[] args) throws Exception{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?"+sign("f46256c26dcb27a268bc44cb9d5ec2af658c7b8065d58298ea4f9dbfb98f3211"));
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent("测试文本消息");
        request.setText(text);

        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
//        at.setAtMobiles(Arrays.asList("13738049559"));
// isAtAll类型如果不为Boolean，请升级至最新SDK
        at.setIsAtAll(false);
        request.setAt(at);

//        request.setMsgtype("link");
//        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
//        link.setMessageUrl("https://www.dingtalk.com/");
//        link.setPicUrl("");
//        link.setTitle("时代的火车向前开");
//        link.setText("这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林");
//        request.setLink(link);
//
//        request.setMsgtype("markdown");
//        OapiRobotSendRequest.Text text1 = new OapiRobotSendRequest.Text();
//        text1.setContent("1234");
//        request.setText(text1);
        OapiRobotSendResponse response = client.execute(request);
        System.out.println(JSONObject.toJSONString(response));


    }


    private static String sign(String accessToken) throws Exception{
        Long timestamp = System.currentTimeMillis();
        String secret = "SEC3ed8b405bcfdc50d009f51398a6e1e9107b571f059c60d94bbd86c1db133bc63";
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        String result = "access_token="+accessToken+"&timestamp="+timestamp+"&sign="+sign;
        System.out.println(result);
        return result;
    }
}
