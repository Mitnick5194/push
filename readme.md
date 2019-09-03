生产者PushProductor可实现单例，在发送消息时从消息里传入所属主题topic或队列queue
消费者PushListener如果需要监听多个主题，则需要在activemq的消费者主题设置多个
Topic topic = session.createTopic("test-topic,test-topic2");
使用,分隔

所有的监听器都要实现AbstractPushListener，在在方法comsume处理自己的逻辑