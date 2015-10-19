package pat.explorasi;

import java.util.ArrayList;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import kafka.consumer.ConsumerIterator;
import kafka.message.MessageAndMetadata;
 
public class ConsumerGroupExample {
    private final ConsumerConnector consumer;
    private final String[] topic;
    private  ExecutorService executor;
 
    public ConsumerGroupExample(String a_zookeeper, String a_groupId, String[] a_topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
    }
 
    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (Exception e) {
            System.out.println("Interrupted during shutdown, exiting uncleanly");
        }
   }
 
    public void run(int a_numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        System.out.println("Sini-1");
//        for(String tp : topic){
//            System.out.println(tp);
//            topicCountMap.put(tp, new Integer(a_numThreads));
//        }
//        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
//        
//        List<KafkaStream<byte[], byte[]>> streams = new ArrayList<>();
//        for(String tp : topic){
//            streams.addAll((consumerMap.get(tp)));
//        }
        topicCountMap.put("contohCinta", 1);
        topicCountMap.put("contohPAT", 2);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get("contohCinta");
        Set<String> keySet = consumerMap.keySet();
        System.out.println(streams.get(0).toString());
        for(String key : keySet){
            System.out.println(key);
        }
//        for(KafkaStream<byte[],byte[]> m_stream : streams){
//            ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
//            while (it.hasNext() && it.hasDefiniteSize()){
//                MessageAndMetadata<byte[], byte[]> next = it.next();
//                System.out.println("Thread " + " "+ next.topic() + ": " + new String(next.message()));
//            }
//            System.out.println("Lewat");
//        }
//        streams.clear();
//        streams = consumerMap.get("contohPAT");
//        for(KafkaStream<byte[],byte[]> m_stream : streams){
//            ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
//            while (it.hasNext()){
//                MessageAndMetadata<byte[], byte[]> next = it.next();
//                System.out.println("Thread " + " "+ next.topic() + ": " + new String(next.message()));
//            }
//        }
        
//        // now launch all the threads
//        //
//        executor = Executors.newFixedThreadPool(a_numThreads);
// 
//        // now create an object to consume the messages
//        //
//        int threadNumber = 0;
//        for (final KafkaStream stream : streams) {
//            executor.submit(new ConsumerTest(stream, threadNumber));
//            threadNumber++;
//        }
;
    }
 
    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
 
        return new ConsumerConfig(props);
    }
 
    public static void main(String[] args) {
        String zooKeeper = "localhost:2181";
        String groupId = "13512042";
        String[] topic = {"contohPAT","contohCinta"};
        int threads = Integer.parseInt("4");
 
        ConsumerGroupExample example = new ConsumerGroupExample(zooKeeper, groupId, topic);
        example.run(1);
        System.out.println("Sini");
        example.shutdown();
    }
}