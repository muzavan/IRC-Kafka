/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pat.kafka;

import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 *
 * @author M. Reza Irvanda (13512042) and Joshua Bezaleel Abednego (13512013)
 */
public class PATProducer {
    private final Producer<String,String> producer; //interface with Kafka
    private final String nickName;
    
    public PATProducer(String _nickName){
        producer = new Producer<>(defaultProducerConfiguration());
        this.nickName = _nickName;
    }
    
    //For Main Constructor, Using Default Configuration
    private static ProducerConfig defaultProducerConfiguration(){
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("producer.type","async");
        //props.put("partitioner.class", "example.producer.SimplePartitioner");
        props.put("request.required.acks", "1");
        return new ProducerConfig(props);
    }
    
    public void send(String channel, String chatMessage){
        String _message = String.format("%s (@%s) : %s", nickName,channel,chatMessage);
        System.out.println("SEND : " + _message);
        KeyedMessage<String,String> message = new KeyedMessage<>(channel, _message);
        producer.send(message);
    }
    
    public void close(){
        producer.close();
    }
    
}
