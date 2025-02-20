package br.com.atocf.order.processor;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProcessorApplicationTests {

	@Autowired
	private RedissonClient redissonClient;

	@Test
	void contextLoads() {
	}
}