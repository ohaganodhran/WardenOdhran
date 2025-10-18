package com.warden;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest
@PropertySource(value = "file:/var/WardenOdhran/local.properties")
class WardenOdhranApplicationTests {

	@Test
	void contextLoads() {
	}

}
