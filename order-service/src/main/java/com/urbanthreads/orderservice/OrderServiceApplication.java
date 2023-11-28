package com.urbanthreads.orderservice;


import com.urbanthreads.orderservice.DTO.ShippingOrderDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Configuration
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	public ConsumerFactory<String, ShippingOrderDTO> consumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-lzvrd.us-west4.gcp.confluent.cloud:9092");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "US-Shipping");
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put("security.protocol", "SASL_SSL");
		configProps.put("sasl.mechanism", "PLAIN");
		configProps.put("sasl.jaas.config",
				"org.apache.kafka.common.security.plain.PlainLoginModule required username='SJROTDOKJXRO2SIC' password='hmJ1tzfD2vsimMZ47fvOoGS1V/zfrSR3Qqw6AL1AxHqOppSVpWq9rySgH0m/hr09';");

		JsonDeserializer<ShippingOrderDTO> deserializer = new JsonDeserializer<>(ShippingOrderDTO.class);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(false); // Ignore __TypeId__ in the message key
		deserializer.setUseTypeHeaders(false); // Ignore __TypeId__ in the message headers


		return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
}
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ShippingOrderDTO> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ShippingOrderDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}


}

