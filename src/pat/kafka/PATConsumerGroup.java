/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pat.kafka;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 *
 * @author M. Reza Irvanda (13512042) and Joshua Bezaleel Abednego (13512013)
 */
public class PATConsumerGroup implements Runnable {
    private ConsumerConnector consumerConnector = null;
    private String topic = null;
    
    public PATConsumerGroup(String _zooKeeper, String _groupId, String _topic){
        consumerConnector = Consumer.createJavaConsumerConnector(createConsumerConfig(_zooKeeper, _groupId));
        topic = _topic;
    }
    
    @Override
    public void run(){
        Map<String,Integer> topicMap  = new HashMap<>();
        topicMap.put(topic, new Integer(1));
        List<KafkaStream<byte[], byte[]>> streams = null;
        //while(streams == null){
            try{
                Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicMap);
                streams = consumerMap.get(topic);
            } catch (Exception e){
                System.out.println("Something's wrong : "+e.getMessage());
            }
        //}
        KafkaStream<byte[],byte[]> stream = streams.get(0);
        ConsumerIterator<byte[],byte[]>  it = stream.iterator();
        while(it.hasNext()){
           try {
             System.out.println(new String(it.next().message(),"UTF-8"));
           } catch (UnsupportedEncodingException ex) {
             Logger.getLogger(PATConsumerGroup.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
        
    }
    
    public void shutdown(){
        System.out.println("Anda berhenti dari " + topic);
        consumerConnector.shutdown();
    }
    
    // For Default Configuration
    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
 
        return new ConsumerConfig(props);
    }
}
