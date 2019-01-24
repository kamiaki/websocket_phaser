package com.aki.websocket;

public class SendThread implements Runnable{
    @Override
    public void run() {
        while (true){
                MyWebSocket.sendMessageAll("循环发送");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
