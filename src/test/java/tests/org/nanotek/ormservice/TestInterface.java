package tests.org.nanotek.ormservice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.jar.asm.Opcodes;

class TestInterface {

	@Test
	void test() {
		Loaded<?> l= new ByteBuddy()
				.makeInterface()
				.defineMethod("myLongProperty", Long.class , Opcodes.ACC_PUBLIC)
				.withoutCode().make().load(getClass().getClassLoader());
		assertNotNull(l);
	}

}
