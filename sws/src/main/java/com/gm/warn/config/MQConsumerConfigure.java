package com.gm.warn.config;

import com.gm.warn.controller.RocketMqConsumer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "rocketmq.consumer")
//@PropertySource({"classpath:rocketmq.yml"})
public class MQConsumerConfigure {

//    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MQConsumerConfigure.class);

    private String groupName;
    private String namesrvAddr;
    private String topics;
    // 消费者线程数据量
    private Integer consumeThreadMin;
    private Integer consumeThreadMax;
    private Integer consumeMessageBatchMaxSize;

    @Autowired
    private RocketMqConsumer rocketMqConsumer;

    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.consumer", value = "isOnOff", havingValue = "on")
    public DefaultMQPushConsumer defaultConsumer(){
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(groupName);
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setConsumeThreadMin(consumeThreadMin);
        defaultMQPushConsumer.setConsumeThreadMax(consumeThreadMax);
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        defaultMQPushConsumer.registerMessageListener(rocketMqConsumer);

        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        try {
            String[] topicArr = topics.split(";");
            for(String topic : topicArr){
                String[] tagArr = topic.split("~");
                defaultMQPushConsumer.subscribe(tagArr[0], tagArr[1]);
            }
            defaultMQPushConsumer.start();
            System.out.println("consumer 创建成功 groupName={}, topics={}, namesrvAddr={}"+ groupName + topics + namesrvAddr);
        } catch (MQClientException e) {
            System.out.println("consumer 创建失败!");
        }

        return defaultMQPushConsumer;

    }

}
