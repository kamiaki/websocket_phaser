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


    @OnOpen//连接
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入集合

        playerID++;
        //本客户端id
        this.playerid = playerID;
        //给自己发送id号
        Map<String,Object> playerIDMap = new HashMap<String,Object>();
        playerIDMap.put("msgType", "playerID");
        playerIDMap.put("playerID", playerID);
        this.sendMessage(gson.toJson(playerIDMap));
    }

    @OnMessage//接受
    public void onMessage(String message, Session session) {
        try {
            Map map = gson.fromJson(message,Map.class);
            if(map != null){
                String msgType = (String) map.get("msgType");
                if("create".equals(msgType)){
                    //创建一个player对象
                    Player player = gson.fromJson(message,Player.class);
                    players.add(player);


                }else if("start".equals(msgType)){
                    //同步所有人的信息给新来的人
                    Map<String,Object> playerMap = new HashMap<String,Object>();
                    playerMap.put("msgType", "playersInfo");
                    playerMap.put("playersInfo", players);
                    this.sendMessageAll(gson.toJson(playerMap));


                }else if("playerMove".equals(msgType)){
                    Player playerMove = gson.fromJson(message,Player.class);
                    int playerMoveID = playerMove.getPlayerID();
                    int playerMoveX = playerMove.getX();
                    int playerMoveY = playerMove.getY();
                    Iterator<Player> iterator = players.iterator();
                    while (iterator.hasNext()){
                        Player player = iterator.next();
                        if(playerMoveID == player.getPlayerID()){
                            player.setX(playerMoveX);
                            player.setY(playerMoveY);
                        }
                    }

                    this.sendMessageAll(message);//群发消息
                }else{
                    this.sendMessageAll(message);//群发消息
                }
            }
        }catch (Exception e){
            this.sendMessageAll(message);//群发消息
        }
    }

    @OnClose//断开
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
    @OnError//错误
    public void onError(Session session, Throwable error) {
        try {
            session.getBasicRemote().sendText("你报错了!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        error.printStackTrace();
    }
}