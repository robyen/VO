package com.systex.b300.vbooking.sys;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/broadcast")
public class BroadcasterServer {
    static Map<String, RemoteEndpoint.Basic> allRemote = new HashMap<String, RemoteEndpoint.Basic>();
    
    @OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection");
//        try {
            RemoteEndpoint.Basic basicRemote = session.getBasicRemote();
            allRemote.put(session.getId(), basicRemote);
//            basicRemote.sendText("Connection Established");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println(session.getId() + " send Message :"+message);
    }
    
    public static void broadcasterMessage(String message){
        try {
            for (String id : allRemote.keySet()) {
                RemoteEndpoint.Basic basic = allRemote.get(id);
                basic.sendText(message);
            }
            System.out.println("allRemote..." + allRemote.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * 當WebSocket連線關閉時觸發
     */
    @OnClose
    public void onClose(Session session) {
        allRemote.remove(session.getId());
        System.out.println("Session " + session.getId() + " has ended");
    }

}
