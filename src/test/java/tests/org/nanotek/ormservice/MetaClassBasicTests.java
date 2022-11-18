package tests.org.nanotek.ormservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.junit.jupiter.api.Test;
import org.nanotek.ormservice.BaseConfiguration;
import org.nanotek.ormservice.OrmServiceApplication;
import org.nanotek.ormservice.api.meta.EntityAnnotation;
import org.nanotek.ormservice.api.meta.MappedSuperClassAnnotation;
import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.MetaClassType;
import org.nanotek.ormservice.api.meta.TableAnnotation;
import org.nanotek.ormservice.api.meta.builder.MetaClassClassBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import net.bytebuddy.dynamic.DynamicType.Builder;

@SpringBootTest(classes = {BaseConfiguration.class , OrmServiceApplication.class})
public class MetaClassBasicTests {

	private final String PACKAGE = "org.nanotek.";
	
	@Autowired
	DefaultListableBeanFactory beanFactory;
	
	@Autowired
	MetaClassClassBuilder classBuilder;
	
	@Test
	public void basicMetaClassCreationTest() {
		MetaClass mt = createBasicMetaClass();
		assertNotNull(beanFactory);
		assertNotNull(mt);
		Builder<?> bd = classBuilder.build(mt, beanFactory.getBeanClassLoader());
		Class<?> cls = bd.make().load(beanFactory.getBeanClassLoader()).getLoaded();
		assertNotNull(cls);
		assertEntityAnnotation(cls);
		assertTableAnnotation(cls);
		try {
			Object obj = cls.newInstance();
			assertNotNull(obj);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void classAndAttributesCreationTest() {
		MetaClass mt = createBasicMetaClassAndPopulateWithAttributes();
	}

	private MetaClass createBasicMetaClassAndPopulateWithAttributes() {
		MetaClass mt = createBasicMetaClass();
		populateWithAttributes(mt);
		return mt;
	}

	private void populateWithAttributes(MetaClass mt) {
		// TODO Auto-generated method stub
		
	}

	private void assertEntityAnnotation(Class<?> cls) {
		Annotation[] anottations = cls.getAnnotations();
		boolean b = Stream
			.of(anottations)
			.filter(a -> a.annotationType().equals(Entity.class)).count()==1;
		assertTrue(b);
	}

	private void assertTableAnnotation(Class<?> cls) {
		Annotation[] anottations = cls.getAnnotations();
		boolean b = Stream
			.of(anottations)
			.filter(a -> a.annotationType().equals(Table.class)).count()==1;
		assertTrue(b);
	}
	
	private MetaClass createBasicMetaClass() {
		MetaClass cm = new  MetaClass();
						cm.setTableName("test");
						cm.setClassName("Test");
						cm.setClassType(MetaClassType.EntityClass);
		return cm;
	}
	
}
