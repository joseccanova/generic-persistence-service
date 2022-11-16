package tests.org.nanotek.ormservice;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.nanotek.ormservice.BaseConfiguration;
import org.nanotek.ormservice.OrmServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BaseConfiguration.class , OrmServiceApplication.class})
public class MetaClassBasicTests {


	@Test
	public void basicTest() {
		assertTrue(false);
	}
	
}
