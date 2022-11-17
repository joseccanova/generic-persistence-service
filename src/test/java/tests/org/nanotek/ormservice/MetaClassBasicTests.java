package tests.org.nanotek.ormservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
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
		assertTrue(mt !=null);
		Builder<?> bd = classBuilder.build(mt, beanFactory.getBeanClassLoader());
		Class<?> cls = bd.make().load(beanFactory.getBeanClassLoader()).getLoaded();
		assertNotNull(cls);
		assertEntityAnnotation(cls);
		assertTableAnnotation(cls);
	}

	private void assertEntityAnnotation(Class<?> cls) {
		Annotation[] anottations = cls.getAnnotations();
		boolean b = Stream
			.of(anottations)
			.filter(a -> a.annotationType().equals(Entity.class)).count()>0;
		assertTrue(b);
	}

	private void assertTableAnnotation(Class<?> cls) {
		Annotation[] anottations = cls.getAnnotations();
		boolean b = Stream
			.of(anottations)
			.anyMatch(a -> a.annotationType().equals(Table.class));
		assertTrue(b);
	}
	
	//TODO: Promote methods for a specialized adapter / strategy classes.
	private MetaClass createBasicMetaClass() {
		MetaClass cm = new  MetaClass();
						cm.setTableName("test");
						cm.setClassName("Test");
						cm.setClassType(MetaClassType.EntityClass);
		return cm;
	}
	
	private Annotation processTableType(MetaClass cm11) {
		return new TableAnnotation(cm11.getTableName());
	}

	private Annotation processEntityType (MetaClass cm)
	{
		switch(cm.getClassType()) {
		case EntityClass:
			return new EntityAnnotation(cm.getClassName());
		case MappedSuperClass:
			return new MappedSuperClassAnnotation();
		default: 
			return null;
		}
	}
	
	private Class<?> getIdClass(MetaClass cm11) {
		return Long.class;
	}
	
}
