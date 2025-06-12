package software.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.board.common.snowflake.Snowflake;

@Configuration
public class SnowflakeConfig {

	@Bean
	public Snowflake snowflake() {
		return new Snowflake();
	}
}
