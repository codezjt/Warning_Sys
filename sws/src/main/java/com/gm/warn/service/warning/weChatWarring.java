package com.gm.warn.service.warning;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.stereotype.Service;

@Service
public class weChatWarring {
    public static  final String CORPID="XXX";
    public static  final Integer AGENTID=000;
    public static  final String CORPSECRET="XXXX";
    public static final String GET_TOKEN_URL="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET";

    public  void push(String user,String content) throws WxErrorException {

//        System.out.println("=====================================push()=============================");
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId(CORPID);      // 设置微信企业号的appid
        config.setCorpSecret(CORPSECRET);  // 设置微信企业号的app corpSecret
        config.setAgentId(AGENTID);     // 设置微信企业号应用ID
        // config.setToken(token);       // 设置微信企业号应用的token


        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);

        System.out.println(user+"==="+content);
        WxCpMessage message = WxCpMessage.TEXT().agentId(AGENTID).toUser(user).content(content).build();
        wxCpService.messageSend(message);
        WxCpUser usercp = wxCpService.getUserService().getById(user);
//        System.out.println("accessToken==="+wxCpService.getAccessToken());
//        System.out.println("accessToken==="+usercp.getName()+"==="+usercp.getMobile());

//        *****************weChatWarring.push(num,username); 调用发送消息

    }

}
