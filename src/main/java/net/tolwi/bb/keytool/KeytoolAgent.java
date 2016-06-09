package net.tolwi.bb.keytool;

import static net.bytebuddy.matcher.ElementMatchers.nameContainsIgnoreCase;
import static net.bytebuddy.matcher.ElementMatchers.named;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Callable;

public class KeytoolAgent {
	
	public static class LogInterceptor {

		  public static Date fixDate(@AllArguments Object[] allArguments, @Origin Method method, @SuperCall Callable<?> superCall) {
			  try {
			    	return (Date) superCall.call();
			    } catch (Exception e) {
					System.out.println(">>> catched " + e.getMessage()+" .. returning current date");
					return new Date();
				}
		    }
		  
		}
	
	  public static void premain(String args, Instrumentation inst) {
		  System.setSecurityManager(null);
		  
		  
		  new AgentBuilder.Default()
		  	.enableBootstrapInjection(new File(System.getProperty("java.io.tmpdir")), inst)
//		  	.with(new AgentBuilder.Listener.StreamWriting(System.out))
			.with(RedefinitionStrategy.RETRANSFORMATION)
	        .type(nameContainsIgnoreCase("DerInputBuffer"))
	        
	        .transform((DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) -> {

	        	ImplementationDefinition<?> method = builder.method(named("getTime"));
				ReceiverTypeDefinition<?> intercept = method.intercept(MethodDelegation.to(LogInterceptor.class));

	        	return intercept;
	        })
      .installOn(inst);
	  }
	}
