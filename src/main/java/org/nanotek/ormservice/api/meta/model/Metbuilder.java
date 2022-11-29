package org.nanotek.ormservice.api.meta.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodDescription.InDefinedShape;
import net.bytebuddy.description.modifier.ModifierContributor.ForField;
import net.bytebuddy.description.modifier.ModifierContributor.ForMethod;
import net.bytebuddy.description.modifier.ModifierContributor.ForType;
import net.bytebuddy.description.type.RecordComponentDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeDescription.Generic;
import net.bytebuddy.description.type.TypeVariableToken;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition.Optional;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.attribute.TypeAttributeAppender;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.LatentMatcher;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.visitor.ContextClassVisitor;

public class Metbuilder<T> implements Builder<T> {

	Builder<T> builder;

	public Builder<T> visit(AsmVisitorWrapper asmVisitorWrapper) {
		return builder.visit(asmVisitorWrapper);
	}

	public Builder<T> name(String name) {
		return builder.name(name);
	}

	public Builder<T> suffix(String suffix) {
		return builder.suffix(suffix);
	}

	public Builder<T> modifiers(ForType... modifierContributor) {
		return builder.modifiers(modifierContributor);
	}

	public Builder<T> modifiers(Collection<? extends ForType> modifierContributors) {
		return builder.modifiers(modifierContributors);
	}

	public Builder<T> modifiers(int modifiers) {
		return builder.modifiers(modifiers);
	}

	public Builder<T> merge(ForType... modifierContributor) {
		return builder.merge(modifierContributor);
	}

	public Builder<T> merge(Collection<? extends ForType> modifierContributors) {
		return builder.merge(modifierContributors);
	}

	public Builder<T> topLevelType() {
		return builder.topLevelType();
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.InnerTypeDefinition.ForType<T> innerTypeOf(Class<?> type) {
		return builder.innerTypeOf(type);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.InnerTypeDefinition.ForType<T> innerTypeOf(TypeDescription type) {
		return builder.innerTypeOf(type);
	}

	public InnerTypeDefinition<T> innerTypeOf(Method method) {
		return builder.innerTypeOf(method);
	}

	public InnerTypeDefinition<T> innerTypeOf(Constructor<?> constructor) {
		return builder.innerTypeOf(constructor);
	}

	public InnerTypeDefinition<T> innerTypeOf(InDefinedShape methodDescription) {
		return builder.innerTypeOf(methodDescription);
	}

	public Builder<T> declaredTypes(Class<?>... type) {
		return builder.declaredTypes(type);
	}

	public Builder<T> declaredTypes(TypeDescription... type) {
		return builder.declaredTypes(type);
	}

	public Builder<T> declaredTypes(List<? extends Class<?>> types) {
		return builder.declaredTypes(types);
	}

	public Builder<T> declaredTypes(Collection<? extends TypeDescription> types) {
		return builder.declaredTypes(types);
	}

	public Builder<T> noNestMate() {
		return builder.noNestMate();
	}

	public Builder<T> nestHost(Class<?> type) {
		return builder.nestHost(type);
	}

	public Builder<T> nestHost(TypeDescription type) {
		return builder.nestHost(type);
	}

	public Builder<T> nestMembers(Class<?>... type) {
		return builder.nestMembers(type);
	}

	public Builder<T> nestMembers(TypeDescription... type) {
		return builder.nestMembers(type);
	}

	public Builder<T> nestMembers(List<? extends Class<?>> types) {
		return builder.nestMembers(types);
	}

	public Builder<T> nestMembers(Collection<? extends TypeDescription> types) {
		return builder.nestMembers(types);
	}

	public Builder<T> permittedSubclass(Class<?>... type) {
		return builder.permittedSubclass(type);
	}

	public Builder<T> permittedSubclass(TypeDescription... type) {
		return builder.permittedSubclass(type);
	}

	public Builder<T> permittedSubclass(List<? extends Class<?>> types) {
		return builder.permittedSubclass(types);
	}

	public Builder<T> permittedSubclass(Collection<? extends TypeDescription> types) {
		return builder.permittedSubclass(types);
	}

	public Builder<T> unsealed() {
		return builder.unsealed();
	}

	public Builder<T> attribute(TypeAttributeAppender typeAttributeAppender) {
		return builder.attribute(typeAttributeAppender);
	}

	public Builder<T> annotateType(Annotation... annotation) {
		return builder.annotateType(annotation);
	}

	public Builder<T> annotateType(List<? extends Annotation> annotations) {
		return builder.annotateType(annotations);
	}

	public Builder<T> annotateType(AnnotationDescription... annotation) {
		return builder.annotateType(annotation);
	}

	public Builder<T> annotateType(Collection<? extends AnnotationDescription> annotations) {
		return builder.annotateType(annotations);
	}

	public Optional<T> implement(Type... interfaceType) {
		return builder.implement(interfaceType);
	}

	public Optional<T> implement(List<? extends Type> interfaceTypes) {
		return builder.implement(interfaceTypes);
	}

	public Optional<T> implement(TypeDefinition... interfaceType) {
		return builder.implement(interfaceType);
	}

	public Optional<T> implement(Collection<? extends TypeDefinition> interfaceTypes) {
		return builder.implement(interfaceTypes);
	}

	public Builder<T> initializer(ByteCodeAppender byteCodeAppender) {
		return builder.initializer(byteCodeAppender);
	}

	public Builder<T> initializer(LoadedTypeInitializer loadedTypeInitializer) {
		return builder.initializer(loadedTypeInitializer);
	}

	public Builder<T> require(TypeDescription type, byte[] binaryRepresentation) {
		return builder.require(type, binaryRepresentation);
	}

	public Builder<T> require(TypeDescription type, byte[] binaryRepresentation,
			LoadedTypeInitializer typeInitializer) {
		return builder.require(type, binaryRepresentation, typeInitializer);
	}

	public Builder<T> require(DynamicType... auxiliaryType) {
		return builder.require(auxiliaryType);
	}

	public Builder<T> require(Collection<DynamicType> auxiliaryTypes) {
		return builder.require(auxiliaryTypes);
	}

	public TypeVariableDefinition<T> typeVariable(String symbol) {
		return builder.typeVariable(symbol);
	}

	public TypeVariableDefinition<T> typeVariable(String symbol, Type... bound) {
		return builder.typeVariable(symbol, bound);
	}

	public TypeVariableDefinition<T> typeVariable(String symbol, List<? extends Type> bounds) {
		return builder.typeVariable(symbol, bounds);
	}

	public TypeVariableDefinition<T> typeVariable(String symbol, TypeDefinition... bound) {
		return builder.typeVariable(symbol, bound);
	}

	public TypeVariableDefinition<T> typeVariable(String symbol, Collection<? extends TypeDefinition> bounds) {
		return builder.typeVariable(symbol, bounds);
	}

	public Builder<T> transform(ElementMatcher<? super Generic> matcher, Transformer<TypeVariableToken> transformer) {
		return builder.transform(matcher, transformer);
	}

	public Valuable<T> defineField(String name, Type type, ForField... modifierContributor) {
		return builder.defineField(name, type, modifierContributor);
	}

	public Valuable<T> defineField(String name, Type type, Collection<? extends ForField> modifierContributors) {
		return builder.defineField(name, type, modifierContributors);
	}

	public Valuable<T> defineField(String name, Type type, int modifiers) {
		return builder.defineField(name, type, modifiers);
	}

	public Valuable<T> defineField(String name, TypeDefinition type, ForField... modifierContributor) {
		return builder.defineField(name, type, modifierContributor);
	}

	public Valuable<T> defineField(String name, TypeDefinition type,
			Collection<? extends ForField> modifierContributors) {
		return builder.defineField(name, type, modifierContributors);
	}

	public Valuable<T> defineField(String name, TypeDefinition type, int modifiers) {
		return builder.defineField(name, type, modifiers);
	}

	public Valuable<T> define(Field field) {
		return builder.define(field);
	}

	public Valuable<T> define(FieldDescription field) {
		return builder.define(field);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional<T> serialVersionUid(
			long serialVersionUid) {
		return builder.serialVersionUid(serialVersionUid);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Valuable<T> field(
			ElementMatcher<? super FieldDescription> matcher) {
		return builder.field(matcher);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Valuable<T> field(
			LatentMatcher<? super FieldDescription> matcher) {
		return builder.field(matcher);
	}

	public Builder<T> ignoreAlso(ElementMatcher<? super MethodDescription> ignoredMethods) {
		return builder.ignoreAlso(ignoredMethods);
	}

	public Builder<T> ignoreAlso(LatentMatcher<? super MethodDescription> ignoredMethods) {
		return builder.ignoreAlso(ignoredMethods);
	}

	public Initial<T> defineMethod(String name, Type returnType, ForMethod... modifierContributor) {
		return builder.defineMethod(name, returnType, modifierContributor);
	}

	public Initial<T> defineMethod(String name, Type returnType, Collection<? extends ForMethod> modifierContributors) {
		return builder.defineMethod(name, returnType, modifierContributors);
	}

	public Initial<T> defineMethod(String name, Type returnType, int modifiers) {
		return builder.defineMethod(name, returnType, modifiers);
	}

	public Initial<T> defineMethod(String name, TypeDefinition returnType, ForMethod... modifierContributor) {
		return builder.defineMethod(name, returnType, modifierContributor);
	}

	public Initial<T> defineMethod(String name, TypeDefinition returnType,
			Collection<? extends ForMethod> modifierContributors) {
		return builder.defineMethod(name, returnType, modifierContributors);
	}

	public Initial<T> defineMethod(String name, TypeDefinition returnType, int modifiers) {
		return builder.defineMethod(name, returnType, modifiers);
	}

	public Initial<T> defineConstructor(ForMethod... modifierContributor) {
		return builder.defineConstructor(modifierContributor);
	}

	public Initial<T> defineConstructor(Collection<? extends ForMethod> modifierContributors) {
		return builder.defineConstructor(modifierContributors);
	}

	public Initial<T> defineConstructor(int modifiers) {
		return builder.defineConstructor(modifiers);
	}

	public ImplementationDefinition<T> define(Method method) {
		return builder.define(method);
	}

	public ImplementationDefinition<T> define(Constructor<?> constructor) {
		return builder.define(constructor);
	}

	public ImplementationDefinition<T> define(MethodDescription methodDescription) {
		return builder.define(methodDescription);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional<T> defineProperty(String name,
			Type type) {
		return builder.defineProperty(name, type);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional<T> defineProperty(String name, Type type,
			boolean readOnly) {
		return builder.defineProperty(name, type, readOnly);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional<T> defineProperty(String name,
			TypeDefinition type) {
		return builder.defineProperty(name, type);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional<T> defineProperty(String name,
			TypeDefinition type, boolean readOnly) {
		return builder.defineProperty(name, type, readOnly);
	}

	public ImplementationDefinition<T> method(ElementMatcher<? super MethodDescription> matcher) {
		return builder.method(matcher);
	}

	public ImplementationDefinition<T> constructor(ElementMatcher<? super MethodDescription> matcher) {
		return builder.constructor(matcher);
	}

	public ImplementationDefinition<T> invokable(ElementMatcher<? super MethodDescription> matcher) {
		return builder.invokable(matcher);
	}

	public ImplementationDefinition<T> invokable(LatentMatcher<? super MethodDescription> matcher) {
		return builder.invokable(matcher);
	}

	public Builder<T> withHashCodeEquals() {
		return builder.withHashCodeEquals();
	}

	public Builder<T> withToString() {
		return builder.withToString();
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition.Optional<T> defineRecordComponent(
			String name, Type type) {
		return builder.defineRecordComponent(name, type);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition.Optional<T> defineRecordComponent(
			String name, TypeDefinition type) {
		return builder.defineRecordComponent(name, type);
	}

	public net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition.Optional<T> define(
			RecordComponentDescription recordComponentDescription) {
		return builder.define(recordComponentDescription);
	}

	public RecordComponentDefinition<T> recordComponent(ElementMatcher<? super RecordComponentDescription> matcher) {
		return builder.recordComponent(matcher);
	}

	public RecordComponentDefinition<T> recordComponent(LatentMatcher<? super RecordComponentDescription> matcher) {
		return builder.recordComponent(matcher);
	}

	public ContextClassVisitor wrap(ClassVisitor classVisitor) {
		return builder.wrap(classVisitor);
	}

	public ContextClassVisitor wrap(ClassVisitor classVisitor, int writerFlags, int readerFlags) {
		return builder.wrap(classVisitor, writerFlags, readerFlags);
	}

	public ContextClassVisitor wrap(ClassVisitor classVisitor, TypePool typePool) {
		return builder.wrap(classVisitor, typePool);
	}

	public ContextClassVisitor wrap(ClassVisitor classVisitor, TypePool typePool, int writerFlags, int readerFlags) {
		return builder.wrap(classVisitor, typePool, writerFlags, readerFlags);
	}

	public Unloaded<T> make() {
		return builder.make();
	}

	public Unloaded<T> make(TypeResolutionStrategy typeResolutionStrategy) {
		return builder.make(typeResolutionStrategy);
	}

	public Unloaded<T> make(TypePool typePool) {
		return builder.make(typePool);
	}

	public Unloaded<T> make(TypeResolutionStrategy typeResolutionStrategy, TypePool typePool) {
		return builder.make(typeResolutionStrategy, typePool);
	}

	public TypeDescription toTypeDescription() {
		return builder.toTypeDescription();
	}

}
