package software.board.hotarticle.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeCalculatorUtils {

	public static Duration calculateDurationToMidnight() {
		// ttl 을 계산하기 위한 클래스.
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = now.plusDays(1).with(LocalTime.MIDNIGHT); // 하루 더하고 그날 자정.
		return Duration.between(now, midnight);
	}
}
