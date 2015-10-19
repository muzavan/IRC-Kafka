/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pat.explorasi;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;




/**
 *
 * @author M. Reza Irvanda
 */
public class ProducerExample{
    
    public static void main(String args[]){
        Producer<String,String> producerExample  = new Producer<>(defaultProducerConfiguration());
        List<KeyedMessage<String,String>> allData = new ArrayList<>();
        //KeyedMessage<String, String> producerData = new KeyedMessage<>("contohPAT","Hallo Dapet Gak?");
        for(int i=0;i<10;i++){
            allData.add(new KeyedMessage<String,String>("contohCinta","HelloCinta-"+String.valueOf(i)));
        }
        
        producerExample.send(allData);
        allData.clear();
        
        for(int i=0;i<10;i++){
            allData.add(new KeyedMessage<String,String>("contohPAT","HelloPAT-"+String.valueOf(i)));
        }
        producerExample.send(allData);
    }
    
    private static ProducerConfig defaultProducerConfiguration(){
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //props.put("partitioner.class", "example.producer.SimplePartitioner");
        props.put("request.required.acks", "1");
        return new ProducerConfig(props);
    }
    
}
