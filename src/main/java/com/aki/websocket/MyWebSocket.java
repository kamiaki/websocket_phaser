package com.aki.websocket;

import com.aki.websocket.po.Player;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/linkWS")
@Component
public class MyWebSocket {
    //静态 参数&方法
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();  //客户端集合
    private static List<Player> players = Collections.synchronizedList(new ArrayList<Player>() );  //玩家集合
    private static int playerID = 0; //用户id递增添加
    private static final Gson gson = new Gson();
    //本对象属性
    private Session session;    //本客户端 session
    private int playerid;       //本客户端 playerid

    //连接
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入集合

        //创建一个player对象
        playerID++;
        int x = (int) (Math.random()*350);
        int y = (int) (Math.random()*350);
        Player player = new Player(playerID,"",x,y);
        players.add(player);
        //本客户端id
        this.playerid = playerID;

        //给自己发送id号
        Map<String,Object> playerIDMap = new HashMap<String,Object>();
        playerIDMap.put("msgType", "playerID");
        playerIDMap.put("playerID", playerID);
        this.sendMessage(gson.toJson(playerIDMap));

        //发送所有人的消息
        Map<String,Object> playerMap = new HashMap<String,Object>();
        playerMap.put("msgType", "playersInfo");
        playerMap.put("playersInfo", players);
        this.sendMessageAll(gson.toJson(playerMap));
    }
    //接受
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            Map map = gson.fromJson(message,Map.class);
        }catch (Exception e){}
        this.sendMessageAll(message);//群发消息
    }







    //发送消息
    public void sendMessage(String message){
        try {
            this.session.getBasicRemote().sendText(message);      //同步发送
        } catch (IOException e) {}
    }
    //群发消息
    public static void sendMessageAll(String message){
        for (MyWebSocket myWebSocket : webSocketSet) {
            myWebSocket.sendMessage(message);
        }
    }
    //断开
    @OnClose
    public void onClose() {
        int id = this.playerid;
        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()){
            Player player = iterator.next();
            if(id == player.getPlayerID()){
                iterator.remove();
            }
        }
        webSocketSet.remove(this);  //集合中删除
    }
    //发生错误
    @OnError
    public void onError(Session session, Throwable error) {
        try {
            session.getBasicRemote().sendText("你报错了!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        error.printStackTrace();
    }
}