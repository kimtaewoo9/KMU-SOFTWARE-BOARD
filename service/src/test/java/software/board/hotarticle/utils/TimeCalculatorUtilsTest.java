package software.board.hotarticle.utils;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class TimeCalculatorUtilsTest {

	@Test
	void timeCalculatorTest() {
		Duration duration = TimeCalculatorUtils.calculateDurationToMidnight();

		System.out.println("duration.getSeconds() / 60 = " + duration.getSeconds() / 60);
	}
}
