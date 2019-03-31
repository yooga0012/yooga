package com.yooga.web;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;


@Component
@ServerEndpoint("/websocket/{uid}")
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();


    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收uid
    private String uid="";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session ,@PathParam("uid") String uid) throws IOException {
        this.session = session;
        this.uid=uid;
        webSocketSet.add(this);
        onlineCount++;
        System.out.println("有新窗口开始监听:"+uid+",当前在线人数为" + getOnlineCount());
        session.getBasicRemote().sendText("hello");
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        onlineCount--;
        System.out.println("close one "+onlineCount+" left");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message , Session session) throws IOException {
        System.out.println("receive from"+uid+" message :"+message);
        for (WebSocketServer webSocketServer : webSocketSet) {
            webSocketServer.getSession().getBasicRemote().sendText(message);
        }

    }


    //自定义
    public static void sendInfo(String message,@PathParam("uid") String uid) throws IOException {
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(uid==null) {
                    item.getSession().getBasicRemote().sendText(message);
                }else if(item.uid.equals(uid)){
                    item.getSession().getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }







        public static int getOnlineCount() {
        return onlineCount;
    }

    public static void setOnlineCount(int onlineCount) {
        WebSocketServer.onlineCount = onlineCount;
    }

    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }

    public static void setWebSocketSet(CopyOnWriteArraySet<WebSocketServer> webSocketSet) {
        WebSocketServer.webSocketSet = webSocketSet;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
