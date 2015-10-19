/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pat.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author M. Reza Irvanda
 */
public final class PATChatClient {
    private PATProducer producer;
    private Map<String, PATConsumerGroup> channelConsumer;
    private String nickName = null;
    private static final int MAX_USER = 3000;
    
    public String getName(){
        return nickName;
    }
    
    public boolean hasName(){
        return (nickName!=null);
    }
    public PATChatClient(){
        nick("");
    }
    
    public PATChatClient(String _nickName){
        nick(_nickName);
    }
    
    public void nick(String _nickName){
        if(_nickName.isEmpty()){
            _nickName = "user_"+(new Random().nextInt(MAX_USER));
        }
        producer = new PATProducer(_nickName);
        nickName = _nickName;
        channelConsumer = new HashMap<>();
    }
    
    public void join(String channel){
        if(channelConsumer.containsKey(channel)){
            System.out.println("Anda sudah bergabung di '"+channel+"'");
            return;
        }
        PATConsumerGroup newGroup = new PATConsumerGroup("localhost:2181", "_"+nickName, channel);
        channelConsumer.put(channel,newGroup);
        new Thread(newGroup).start(); // start actively accept new message
        System.out.println("Anda berhasil bergabung di '"+channel+"'");
    }
    
    public void leave(String channel){
        if(!channelConsumer.containsKey(channel)){
            System.out.println("Anda tidak bergabung di '"+channel+"'");
            return;
        }
        channelConsumer.get(channel).shutdown();
        channelConsumer.remove(channel);
        System.out.println("Anda berhasil meninggalkan '"+channel+"'");
    }
    
    public void exit(){
        producer.close();
        //shutdown all active connection
        for(PATConsumerGroup consumer : channelConsumer.values()){
            consumer.shutdown();
        }
    }
    
    
    public void send(String channel, String message){
        if(channel.isEmpty()){
            for(String _channel : channelConsumer.keySet()){
                producer.send(_channel,message);
            }
            return;
        }
        if(channelConsumer.containsKey(channel)){
            producer.send(channel, message);
        }
        else{
            System.out.println("Anda belum bergabung dengan channel '"+channel+"'");
        }
    }
    
    public void receive(){
        for(String channel : channelConsumer.keySet()){
            channelConsumer.get(channel).run();
        }
    }
    
    
    public static void main(String[] args) {
        boolean exit=false;
        Scanner cli = new Scanner(System.in);
        PATChatClient client = null;
        String userInput,userAction,userMessage;
        while(client == null){
            System.out.println("Mulai dengan mengetikkan /NICK <username>");
            userInput = cli.nextLine();
            if(userInput.indexOf(' ') != -1){
                userAction = userInput.substring(0,userInput.indexOf(' '));
                userMessage = userInput.substring(userInput.indexOf(' ')+1);
                //System.out.println("Action : "+userAction+"\n"+"Message : "+userMessage);
                if(userAction.equals("/NICK")){
                    if(userMessage.equals(userInput)){
                        //berarti tidak ada input <username>
                        client = new PATChatClient();
                        System.out.println("Anda login sebagai : "+ client.getName());
                    }
                    else{
                        // ada input
                        client = new PATChatClient(userMessage);
                        System.out.println("Anda login sebagai : "+ client.getName());
                    }
                }
            }
        }
        while(!exit && !(client == null)){
            userInput = cli.nextLine();
            if(userInput.indexOf(' ')!=-1 || userInput.indexOf("/EXIT")!=-1){
                userAction = userInput.substring(0,userInput.indexOf(' '));
                userMessage = userInput.substring(userInput.indexOf(' ')+1);
                //System.out.println("Action : "+userAction+"\n"+"Message : "+userMessage);

                    if(userAction.equals("/JOIN") && !userMessage.equals("")){
                        client.join(userMessage);
                    }
                    else if(userAction.equals("/LEAVE") && !userMessage.equals("")){
                        client.leave(userMessage);
                    }
                    else if(userAction.equals("/EXIT")){
                        client.exit();
                        exit = true;
                    }
                    else if(userAction.charAt(0) == '@'){
                        client.send(userAction.substring(1),userMessage);
                    }
                    else{ //send broadcast message
                        client.send("",userInput);
                    }
            }
            else{
                //dideteksi sebagai pesan broadcast
                client.send("",userInput);
            }
        }
    }
    
}
