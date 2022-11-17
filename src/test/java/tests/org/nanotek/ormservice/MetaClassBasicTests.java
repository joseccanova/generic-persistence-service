package tests.org.nanotek.ormservice;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.junit.jupiter.api.Test;
import org.nanotek.ormservice.Base;
import org.nanotek.ormservice.BaseConfiguration;
import org.nanotek.ormservice.OrmServiceApplication;
import org.nanotek.ormservice.api.meta.MetaClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.FixedValue;

@Slf4j
@SpringBootTest(classes = {BaseConfiguration.class , OrmServiceApplication.class})
public class MetaClassBasicTests {

	private final String PACKAGE = "org.nanotek.";
	
	@Autowired
	DefaultListableBeanFactory beanFactory;
	
	@Test
	public void basicMetaClassCreationTest() {
		MetaClass mt = createBasicMetaClass();
		assertNotNull(beanFactory);
		assertTrue(mt !=null);
		Builder<?> bd = processClassMetaData(mt, beanFactory.getBeanClassLoader());
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
						cm.setClassType(MetaClass.MetaClassType.EntityClass);
		return cm;
	}
	
	private Builder processClassMetaData(MetaClass cm11, ClassLoader classLoader) {
//		processors.stream().forEach(p -> p.process(cm11));
		String tableName =  cm11.getTableName();
		String myClassName = cm11.getClassName();
		log.debug("class name " + myClassName);
		System.err.println("class name " + myClassName);
		AnnotationDescription rootAnnotation =  AnnotationDescription.Builder.ofType(JsonRootName.class)
				.define("value", myClassName)
				.build();
		
//		Class<?> idClass = getIdClass(cm11);
		//TODO:generate another strategy to produce an ID for the EntityClass.
		Class<?> idClass = getIdClass(cm11);
		TypeDefinition td = TypeDescription.Generic.Builder.parameterizedType(Base.class  , idClass).build();
		Builder bd = new ByteBuddy(ClassFileVersion.JAVA_V8)
						.subclass(td)
						.name(PACKAGE+myClassName)
						.annotateType(rootAnnotation)
						.annotateType(new EntityImpl(myClassName))
						.annotateType(new TableImpl(tableName))
						.withHashCodeEquals()
						.withToString()
						.method(named("getMetaClass"))
						.intercept(FixedValue.value(MetaClass.class.cast(cm11)));
			return bd;
	}

	private Class<?> getIdClass(MetaClass cm11) {
		return Long.class;
	}
	
	class TableImpl implements Table{

		private String name;
		
		public TableImpl(String name2) {
			this.name = name2;
		}
		
		@Override
		public Class<? extends Annotation> annotationType() {
			return Table.class;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public String catalog() {
			return "";
		}

		@Override
		public String schema() {
			return "";
		}

		@Override
		public UniqueConstraint[] uniqueConstraints() {
			return new UniqueConstraint[0];
		}

		@Override
		public Index[] indexes() {
			return new Index[0];
		}
		
	}
	
	class EntityImpl implements Entity{

		private String name;
		
		public EntityImpl(String name2) {
			this.name = name2;
		}
		
		@Override
		public Class<? extends Annotation> annotationType() {
			// TODO Auto-generated method stub
			return Entity.class;
		}

		@Override
		public String name() {
			return name;
		}
		
	}
	
}
