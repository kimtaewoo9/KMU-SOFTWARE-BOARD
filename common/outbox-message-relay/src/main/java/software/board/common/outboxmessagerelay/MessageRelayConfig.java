package software.board.common.outboxmessagerelay;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync // 트랜잭션 끝나면 message relay 가 전달 받은 이벤트를 비동기로 카프카에게 전송
@Configuration
@ComponentScan("software.board.common.outboxmessagerelay")
@EnableScheduling // 스프링 스케줄링. 기능
public class MessageRelayConfig {

	@Value("${spring.kafka.bootstrap-server}")
	private String bootstrapServers;

	// 카프카로 메시지를 보내기 위한 KafkaTemplate .. 다른 컴포넌트에서 이 빈을 주입 받아서 사용 .
	// 카프 클러스터의 서버 주소 목록 지정,
	@Bean
	public KafkaTemplate<String, String> messageRelayKafkaTemplate() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // 리더, 팔로워 모두 메시지가 복제 됐을 때 성공으로 간주.
		return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
	}

	// 전달 받은 이벤트를 카프카로 전송 ..
	@Bean
	public Executor messageRelayPublishEventExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20); // 핵심 작업 팀 .
		executor.setMaxPoolSize(50); // 50개 더 늘릴 수 있음 .
		executor.setQueueCapacity(100); // 큐는 100개
		executor.setThreadNamePrefix("mr-pub-event-");
		return executor;
	}

	@Bean
	public Executor messageRelayPublishPendingEventExecutor() {
		return Executors.newSingleThreadScheduledExecutor();
	}
}
