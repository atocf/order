package br.com.atocf.order.processor;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProcessorApplicationTests {

	@Mock
	private RedissonClient redissonClient;

	@Test
	void contextLoads() {
	}

}
