# orm-data-service
A simple experiment on persistence/repository/controller focusing on the minimal restcontroller configuration. 
### Database Platform under usage - Postgres 
##Configuration
Check application.yml to verify database connection
##Project Objective 
Evaluate if a simple meta-object definition can be sufficient to configure a complete ORM - Mapping Configuration (DDL - JPA - Repository - Controller) at counter part to reverse engineer a Schema Definition (as used on project crawler data service).
##First Task 
Generate a metaobject abstraction. 
1 - Generate a class model that will provide the following
a. Capable to represent a Type structure as defined on java.lang.reflect (partially)
Reflection is not indicated for the case. 
In matter of fact as verifying on [java api documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Type.html) reflection seems more appropriate for "an already existing model".  
**avoid to consider that is a compiler like component**
**It's not a compiler like component, will used byte buddy to generate class/package definition instead of a template generation**
>***Objective is simple, evaluate how far we can use a parametrized definition as a background for a set of classes type definition, avoiding the complexity of UML metamodel.*** 

>**This is an assumption to verify, in fact an XMI could be used problem is, how to fit XMI in an Angular front end**

This component [Generator XMI](https://github.com/donvadicastro/generator-xmi) , XMI class model on typescript could be suficient.

This component [Inception](https://inception-project.github.io/), as usual the "to much complex" principle that i follow. Amazing tool but far beyond on where i want o reach now.

Found something to help as a guide for development.[CrossCore](https://github.com/crossecore)

>Let's remember the first assumption, XML will not be used because there is already solutions on such field. The think a model based on a "entity type model". No cyclic neither other problems on graph representations. Model is a relational representation of a structure that can be used as a runtime-class-model, no code.

>Attached in a beanfactory a ClassLoader with the entire model definition and this model definition will have its own adapters for class generation. Try to exploit the [Liskov substitution principle](https://en.wikipedia.org/wiki/Liskov_substitution_principle).

>Object behavior will the same, since such objects act as a gateway objects on a API definition, complex behaviors can be specialized on future using "method subtyping"

>Will fix the model to stay aligned to MOF but avoid MOF complexity (for now).

>MOF used generic supertype that on first view will generate certain cyclic behavior as an exemple Classifier , TypedElement. At first create MetaModel without complete abstract generic type (that characterizes the model is designed for  compiler specific usage).MOF is designed for compiler purposes. So, MOF is too much.

>At first and at glance, behavior over properties can be "generified" (in portuguese we do this) since global behavior is real generic. 

>As "we did" on brainz model [spring-core-base](https://github.com/joseccanova/repository-spring-core) and on java validation API , simple behavior can be annotated. Bridge behaior and routing can use annotation facilities too. imagining that the object can be sent to a stream pipeline.

>MOF , JPA MetaModel API (this a constraint) are guidelines for the model.

>Since JPA MetaModel is the ORM-Relationaal operator a sufficient transformation M-M is a "must have".

>Why not generate code? it's done [staruml](https://staruml.io/extensions).

>The metamodel is not just designed for JPA but is a requirement for the project.