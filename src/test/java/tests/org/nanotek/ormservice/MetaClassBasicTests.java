package tests.org.nanotek.ormservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.junit.jupiter.api.Test;
import org.nanotek.ormservice.BaseConfiguration;
import org.nanotek.ormservice.OrmServiceApplication;
import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.MetaClassType;
import org.nanotek.ormservice.api.meta.MetaDataAttribute;
import org.nanotek.ormservice.api.meta.MetaDataAttribute.AttributeType;
import org.nanotek.ormservice.api.meta.builder.MetaClassClassBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import net.bytebuddy.dynamic.DynamicType.Builder;

@SpringBootTest(classes = {BaseConfiguration.class , OrmServiceApplication.class})
public class MetaClassBasicTests {

	
	@Autowired
	DefaultListableBeanFactory beanFactory;
	
	@Autowired
	MetaClassClassBuilder classBuilder;
	
	@Test
	public void basicMetaClassCreationTest() {
		MetaClass mt = createBasicMetaClassAndPopulateWithAttributes();
		assertNotNull(beanFactory);
		assertNotNull(mt);
		Builder<?> bd = classBuilder.build(mt);
		Class<?> cls = bd.make().load(beanFactory.getBeanClassLoader()).getLoaded();
		assertNotNull(cls);
		assertEntityAnnotation(cls);
		assertTableAnnotation(cls);
		try {
			Object bean = beanFactory.createBean(cls);
			assertNotNull(bean);
			Object obj = cls.newInstance();
			assertNotNull(obj);
			assertTrue( obj.getClass().getDeclaredFields().length>1);
			verifyLongField(cls.getDeclaredFields());
			verifyStringField(cls.getDeclaredFields());
			verifyListField(cls.getDeclaredFields());
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	private void verifyStringField(Field[] declaredFields) {
		var vv = Stream.of(declaredFields)
				.filter(f -> f.getType().equals(String.class)).count() > 0;
				assertTrue(vv);		
	}

	private void verifyLongField(Field[] declaredFields) {
		var vv = Stream.of(declaredFields)
		.filter(f -> f.getType().equals(Long.class)).count() > 0;
		assertTrue(vv);
	}
	
	private void verifyListField(Field[] declaredFields) {
		var vv = Stream.of(declaredFields)
		.filter(f -> f.getType().equals(List.class)).count() > 0;
		assertTrue(vv);
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
			mt.addMetaAttribute(createLongMetaAttribute());
			mt.addMetaAttribute(createStringMetaAttribute());
			mt.addMetaAttribute(createListMetaAttribute());
	}

	private MetaDataAttribute createLongMetaAttribute() {
		return MetaDataAttribute
				.builder()
				.attributeType(AttributeType.Single)
				.fieldName("t1")
				.clazz(Long.class)
				.columnName("t1").build();
	}

	private MetaDataAttribute createStringMetaAttribute() {
		return MetaDataAttribute
				.builder()
				.attributeType(AttributeType.Single)
				.fieldName("t2")
				.clazz(String.class)
				.columnName("t2")
				.length("255")
				.build();
	}
	
	private MetaDataAttribute createListMetaAttribute() {
		return MetaDataAttribute
				.builder()
				.attributeType(AttributeType.List)
				.fieldName("t3")
				.clazz(Long.class)
				.columnName("t3")
				.build();
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
