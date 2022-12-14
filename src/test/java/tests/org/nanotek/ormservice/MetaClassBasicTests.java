package tests.org.nanotek.ormservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.nanotek.ormservice.BaseConfiguration;
import org.nanotek.ormservice.BeanFactory;
import org.nanotek.ormservice.OrmServiceApplication;
import org.nanotek.ormservice.Provider;
import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.MetaClassType;
import org.nanotek.ormservice.api.meta.MetaDataAttribute;
import org.nanotek.ormservice.api.meta.MetaDataAttribute.AttributeType;
import org.nanotek.ormservice.api.meta.MetaIdentity;
import org.nanotek.ormservice.api.meta.MetaRelation;
import org.nanotek.ormservice.api.meta.RelationType;
import org.nanotek.ormservice.api.meta.builder.MetaClassDynamicTypeBuilder;
import org.nanotek.ormservice.api.meta.model.MetaModel;
import org.nanotek.ormservice.api.meta.service.DynamicTypeRelationService;
import org.nanotek.ormservice.api.meta.service.DynamicTypeService;
import org.nanotek.ormservice.api.meta.service.RelationTypeService;
import org.nanotek.ormservice.api.meta.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

@Slf4j
@SpringBootTest(classes = {BaseConfiguration.class , OrmServiceApplication.class})
public class MetaClassBasicTests {

	@Autowired
	InjectionClassLoader classLoader;

	@Autowired
	@BeanFactory
	DefaultListableBeanFactory beanFactory;

	@Autowired
	@TypeService
	DynamicTypeService typeService;

	@Autowired
	@RelationTypeService
	DynamicTypeRelationService dynamicTypeRelationService;

	public void basicMetaClassCreationTest() {
		MetaClass mt = createBasicMetaClassAndPopulateWithAttributes();
		createIdentity(mt);
		assertNotNull(beanFactory);
		assertNotNull(mt);
		Class<?> cls = typeService.build(mt).map(l ->l.getLoaded()).orElseThrow();
		assertNotNull(cls);
		assertEntityAnnotation(cls);
		assertTableAnnotation(cls);
		try {
			Object bean = beanFactory.createBean(cls);
			assertNotNull(bean);
			Object obj = cls.newInstance();
			assertNotNull(obj);
			assertTrue(obj.getClass().getDeclaredFields().length>1);
			verifyLongField(cls.getDeclaredFields());
			verifyStringField(cls.getDeclaredFields());
			verifyListField(cls.getDeclaredFields());
			verifyIdAnnotation(cls.getDeclaredFields());
			verifyClassCacheMetaClass();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void basicMetaClassRelationTest() {
		MetaClass mt1 = createBasicMetaClassAndPopulateWithAttributes();
		createIdentity(mt1);
		MetaClass mt2 = createBasicMetaClassAndPopulateWithAttributes();
		MetaClass mt3 = createBasicMetaClassAndPopulateWithAttributes();
		createIdentity(mt2);
		createIdentity(mt3);
		changeName(mt2 , "Test2");
		changeName(mt3 , "Test3");
		createManyRelation(mt1 , mt2 , mt3);
		assertTrue(mt1.getMetaRelations().size()==2);
		Loaded<?> loaded1 = typeService.build(mt1).get();
		Loaded <?> loaded2 = typeService.build(mt2).get();
		Loaded <?> loaded3 = typeService.build(mt3).get();
		dynamicTypeRelationService.processRelationClasses();
		var clsRelationName = mt1.defaultFullClassName()+"Relation";
		try {
			Class<?> cls = classLoader.loadClass(clsRelationName);
			assertTrue( Stream
				.of(cls.getDeclaredFields())
				.anyMatch(f -> f.getType().equals(loaded2.getLoaded())));
			assertTrue(Stream
			.of(cls.getDeclaredFields())
			.anyMatch(f -> f.getType().equals(List.class)));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	@Order(2)
	public void testMetaModel() {
		MetaModel<?> mm =  createBasicMetaClass()
		.map(mc-> MetaModel
				.intialize(mc, classLoader))
		.map(m->m.defineAttribute(createStringMetaAttribute()))
		.map(m->m.defineAttribute(createLongMetaAttribute()))
		.map(m -> m.defineAttribute(createDateMetaAttribute()))
		.orElseThrow();
		changeName(mm.getClazz(), "Test4Instance");
		createIdentity(mm.getClazz());
		try {
			mm.serializeInterfaces();
			Builder<?> db = MetaClassDynamicTypeBuilder.instance().build(mm.getClazz());
			Loaded<?> loaded = mm.buildFromModel(db).make().load(classLoader);
			Optional
			.of(loaded)
//			.map(l -> l.getLoaded())
			.map(l -> checkInstance(l))
			.ifPresentOrElse(i -> log.debug("the instance {}" , i), RuntimeException::new);
		} catch (Exception e) {
			log.debug("the problem {}" , e);
			assertTrue(false);
		}
	}
	
	private Object checkInstance(Loaded<?> loaded) {
		try {
			Class<?> clazz = loaded.getLoaded();
			loaded.saveIn(new File("c:/java"));
			Object instance =  clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
			return instance;
		} catch (Exception e) {
			log.debug("the problem {}" , e);
			assertTrue(false);
		}
		return null;
	}

	public void testSimpleReactiveApi()
	{   
		MySubscriber fs = new MySubscriber();
		sp.subscribe(fs);
		sp.submit("this is a string BIG STRING");
		sp.close();
		
		org.awaitility.Awaitility.
		 await().until(() -> fs.checkCompleted());;
	}

	private void createManyRelation(MetaClass mt1, MetaClass mt2 , MetaClass mt3) {
		MetaRelation mr = MetaRelation.builder().to(mt2).type(RelationType.ONE).build();
		MetaRelation mr1 = MetaRelation.builder().to(mt3).type(RelationType.MANY).build();
		mt1.setMetaRelations(List.<MetaRelation>of(mr , mr1));
	}

	private void changeName(MetaClass mt2, String string) {
		mt2.setClassName(string);
		mt2.setTableName(string);
	}

	@Autowired
	@Qualifier("classCache")
	Map<String,MetaClass> classCache;

	private void verifyClassCacheMetaClass() {
		assertTrue (classCache.entrySet().size() >0);
	}

	private void verifyIdAnnotation(Field[] declaredFields) {
		assertTrue (Stream.of(declaredFields)
				.anyMatch(f -> hasIdAnnotation(f)));
	}

	private boolean hasIdAnnotation(Field f) {
		return Stream.of(f.getAnnotations()).anyMatch(a -> a.annotationType().equals(Id.class));
	}

	private void verifyStringField(Field[] declaredFields) {
		assertTrue(Stream.of(declaredFields)
				.filter(f -> f.getType().equals(String.class)).count() > 0);		
	}

	private void verifyLongField(Field[] declaredFields) {
		assertTrue(Stream.of(declaredFields)
				.filter(f -> f.getType().equals(Long.class)).count() > 1);
	}

	private void verifyListField(Field[] declaredFields) {
		assertTrue(Stream.of(declaredFields)
				.filter(f -> f.getType().equals(List.class)).count() > 0);
	}

	public void classAndAttributesCreationTest() {
		createBasicMetaClassAndPopulateWithAttributes();
	}

	private void createIdentity(MetaClass mc) {
		MetaIdentity mt = createBasicMetaIdentity();
		mc.setIdentity(mt);
	}

	private MetaIdentity createBasicMetaIdentity() {
		return MetaIdentity.builder().attributes(createLongIdentityAttribute() ).name("testId").type(MetaIdentity.IdentityType.Identity).build();
	}

	private List< MetaDataAttribute > createLongIdentityAttribute() {
		var list = new ArrayList<MetaDataAttribute>();
		list.add(MetaDataAttribute.builder().attributeType(AttributeType.Single).fieldName("testId").clazz(Long.class).required(true).build());
		return list;
	}

	private MetaClass createBasicMetaClassAndPopulateWithAttributes() {
		return createBasicMetaClass()
			.map(m -> 
					populateWithAttributes(m))
			.orElseThrow();
	}

	private MetaClass populateWithAttributes(MetaClass mt) {
		assertTrue(mt.addMetaAttribute(createLongMetaAttribute()));
		assertTrue(mt.addMetaAttribute(createStringMetaAttribute()));
		return mt;
	}
	
	private MetaDataAttribute createDateMetaAttribute() {
		return MetaDataAttribute
				.builder()
				.attributeType(AttributeType.Single)
				.fieldName("datt1")
				.clazz(Date.class)
				.columnName("dattc1").build();
	}


	private MetaDataAttribute createLongMetaAttribute() {
		return MetaDataAttribute
				.builder()
				.attributeType(AttributeType.Single)
				.fieldName("att1")
				.clazz(Long.class)
				.columnName("attc1").build();
	}

	private MetaDataAttribute createStringMetaAttribute() {
		return MetaDataAttribute
				.builder()
				.attributeType(AttributeType.Single)
				.fieldName("att2")
				.clazz(String.class)
				.columnName("attc2")
				.length("255")
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

	private Optional<MetaClass>  createBasicMetaClass() {
		return Optional.ofNullable( MetaClass.builder()
					.tableName("test")
					.className("Test")
					.classType(MetaClassType.EntityClass).build()
		);
	}

	SubmissionPublisher<String> sp = new SubmissionPublisher<>();
	
	static class MySubscriber implements Flow.Subscriber<String> {

		private boolean completed = false;

		@Override
		public void onSubscribe(Subscription subscription) {
			subscription.request(1);
		}

		@Override
		public void onNext(String item) {
			log.debug("next {}" , item);
			completed = true;
		}

		@Override
		public void onError(Throwable throwable) {
			log.debug("error {} " , throwable);
		}

		@Override
		public void onComplete() {
			log.debug("complete");
		}
		
		public boolean checkCompleted() {
			return completed;
		}
	};

}
