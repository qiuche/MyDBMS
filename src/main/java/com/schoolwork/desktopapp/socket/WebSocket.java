package com.schoolwork.desktopapp.socket;

import com.schoolwork.desktopapp.helper.RSA;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;


/*
* 登录报文使用RSA加密，此处使用websocket进行后端推送，前端获取公钥加密报文
*
* */
@Component
@ServerEndpoint(value = "/websocket")
@EnableScheduling
public class WebSocket {
    //用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;




    /**
     *      * 连接建立成功调用的方法
     *     
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        boolean flag=false;
        for(WebSocket web:webSocketSet)
        {
            if(session.getId()==web.session.getId())
            {
                flag=true;
                break;
            }
        }
        if(!flag)
        {
            webSocketSet.add(this);//加入set中
        }
        System.out.println("有新连接加入！当前在线人数为" + webSocketSet.size());
//        this.session.getAsyncRemote().sendText("恭喜您成功连接上WebSocket-->当前在线人数为：" + webSocketSet.size());
    }

    /**
     *      * 连接关闭调用的方法
     *     
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this); //从set中删除
        System.out.println("有一连接关闭！当前在线人数为" + webSocketSet.size());
    }

    /**
     *      * 收到客户端消息后调用的方法
     *      *
     *      * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
        broadcast();
    }

    /**
     *      * 发生错误时调用
     *      *
     *     
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     *      * 群发自定义消息
     *      *
     */
    public void broadcast() {
        for (WebSocket item : webSocketSet) {
            //this.session.getBasicRemote().sendText(message);
            item.session.getAsyncRemote().sendText(RSA.RSAPublicKey);//异步发送消息.
            System.out.println(RSA.RSAPublicKey);
            System.out.println(RSA.RSAPrivateKey);
        }
    }

}
