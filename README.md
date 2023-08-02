# Learn Spring AOP

# Spring Aspect Oriented Programming

A layered approach is typically used to build applications:
Web Layer - View logic for web apps OR JSON conversion for REST API
Business Layer - Business Logic
Data Layer - Persistence Logic
Each layer has different responsibilities
HOWEVER, there are a few common aspects that apply to all layers
Security
Performance
Logging
These common aspects are called Cross Cutting Concerns
Aspect Oriented Programming can be used to implement Cross Cutting
Concerns

1: Implement the cross cutting concern as an aspect
2: Define point cuts to indicate where the aspect should be
applied
TWO Popular AOP Frameworks
1. **Spring AOP**
NOT a complete AOP solution BUT very popular
Only works with Spring Beans
Example: Intercept method calls to Spring Beans
2. **AspectJ**
Complete AOP solution BUT rarely used
Example: Intercept any method call on any Java class
Example: Intercept change of values in a field
We will be focusing on Spring AOP in this section

## Important Terminology

**Compile Time**
Advice - What code to execute?
Example: Logging, Authentication
Pointcut - Expression that identifies method calls to be intercepted
Example: execution( com.in28minutes.aop.data..*(..))
Aspect - A combination of
1: Advice - what to do AND
2: Pointcut - when to intercept a method call
Weaver - Weaver is the framework that implements AOP
AspectJ or Spring AOP

**Runtime**
Join Point - When pointcut condition is true, the advice is executed. A
specific execution instance of an advice is called a Join Point.

Services 

```java
package com.springboot.rest.aop.aopexample.business;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.rest.aop.aopexample.data.DataService1;

@Service
public class BusinessService1 {
	
	private DataService1 dataService1;
	
	public BusinessService1(DataService1 dataService1) {
		this.dataService1 = dataService1;
	}
	
	public int calculateMax() {
		int[] data = dataService1.retrieveData();
		return Arrays.stream(data).max().orElse(0);
	}
}

package com.springboot.rest.aop.aopexample.data;

import org.springframework.stereotype.Repository;

@Repository
public class DataService1 {
	public int[] retrieveData() {
		return new int[] {11, 22, 33, 44, 55};
	}
}
```

Spring boot app

```java
package com.springboot.rest.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springboot.rest.aop.aopexample.business.BusinessService1;

@SpringBootApplication
public class LearnSpringAopApplication implements CommandLineRunner{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private BusinessService1 businessService1;
	
	public LearnSpringAopApplication(BusinessService1 businessService1) {
		this.businessService1 = businessService1;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LearnSpringAopApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		logger.info("Value returned is {}", businessService1.calculateMax());

	}

}
```

LogginAspect

```java
package com.springboot.rest.aop.aopexample.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class LoggingAspect {
		
		private Logger logger = LoggerFactory.getLogger(getClass());
		
		//Pointcut - When?
		// execution(* PACKAGE.*.*(..))
		@Before("execution(* com.springboot.rest.aop.aopexample.*.*.*(..))")//WHEN
		public void logMethodCall(JoinPoint joinPoint) {
			logger.info("Before Aspect - {} is called with arguments: {}"
					,  joinPoint, joinPoint.getArgs());//WHAT
		}

	}
```

JoinPoint - what to do?? // PointCut

@Before - when to do??

@Before - Do something before a method is called
@After - Do something after a method is executed irrespective
of whether: BOTH
1: Method executes successfully OR
2: Method throws an exception
@AfterReturning - Do something ONLY when a method
executes successfully
@AfterThrowing - Do something ONLY when a method throws
an exception
@Around - Do something before and after a method execution
Do something AROUND a method execution

```java
package com.springboot.rest.aop.aopexample.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class LoggingAspect {
		
		private Logger logger = LoggerFactory.getLogger(getClass());
		
		//Pointcut - When?
		// execution(* PACKAGE.*.*(..))
		@Before("execution(* com.springboot.rest.aop.aopexample.*.*.*(..))")//WHEN
		public void logMethodCallBeforeExecution(JoinPoint joinPoint) {
			logger.info("Before Aspect - {} is called with arguments: {}"
					,  joinPoint, joinPoint.getArgs());//WHAT
		}

		@After("execution(* com.springboot.rest.aop.aopexample.*.*.*(..))")
		public void logMethodCallAfterExecution(JoinPoint joinPoint) {
			logger.info("After Aspect - {} has executed",  joinPoint);
		}

		@AfterThrowing(
		pointcut = "execution(* com.springboot.rest.aop.aopexample.*.*.*(..))",
		throwing = "exception"
		)
		public void logMethodCallAfterException(JoinPoint joinPoint, Exception exception) {
			logger.info("AfterThrowing Aspect - {} has thrown an exception {}"
					,  joinPoint, exception);
		}

		@AfterReturning(
		pointcut = "execution(* com.springboot.rest.aop.aopexample.*.*.*(..))",
		returning = "resultValue"
		)
		public void logMethodCallAfterSuccessfulExecution(JoinPoint joinPoint, 
				Object resultValue) {
			logger.info("AfterReturning Aspect - {} has returned {}"
					,  joinPoint, resultValue);
		}

	}
```

@Around

PerformanceTrackingAspect.java

```java
package com.springboot.rest.aop.aopexample.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class PerformanceTrackingAspect {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Around("execution(* com.springboot.rest.aop.aopexample.*.*.*(..))")
	public Object findExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		//Start a timer
		long startTimeMillis = System.currentTimeMillis();
		
		//Execute the method
		Object returnValue = proceedingJoinPoint.proceed();
		
		//Stop the timer
		long stopTimeMillis = System.currentTimeMillis();
		
		long executionDuration = stopTimeMillis - startTimeMillis;
		
		logger.info("Around Aspect - {} Method executed in {} ms"
				,proceedingJoinPoint, executionDuration);
		
		return returnValue;
	}
}
```

### Creating a separate Common Pointcut Config file

```java
package com.springboot.rest.aop.aopexample.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcutConfig {
	//  com/springboot/rest/aop/aopexample/aspects/CommonPointcutConfig.java
	@Pointcut("execution(* com.springboot.rest.aop.aopexample.*.*.*(..))")
	public void businessAndDataPackageConfig() {}

	@Pointcut("execution(* com.springboot.rest.aop.aopexample.business.*.*(..))")
	public void businessPackageConfig() {}

	@Pointcut("execution(* com.springboot.rest.aop.aopexample.data.*.*(..))")
	public void dataPackageConfig() {}
	
	@Pointcut("bean(*Service*)")
	public void allPackageConfigUsingBean() {}

}
```

Now we add the Pointcut in the @Before @After etc annotations to refer that

```java
package com.springboot.rest.aop.aopexample.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class LoggingAspect {
		
		private Logger logger = LoggerFactory.getLogger(getClass());
		
	//  com/springboot/rest/aop/aopexample/aspects/CommonPointcutConfig.java
		//Pointcut - When?
		// execution(* PACKAGE.*.*(..))
		@Before("com.springboot.rest.aop.aopexample.aspects.CommonPointcutConfig.allPackageConfigUsingBean()")//WHEN
		public void logMethodCallBeforeExecution(JoinPoint joinPoint) {
			logger.info("Before Aspect - {} is called with arguments: {}"
					,  joinPoint, joinPoint.getArgs());//WHAT
		}

		@After("com.springboot.rest.aop.aopexample.aspects.CommonPointcutConfig.businessPackageConfig()")
		public void logMethodCallAfterExecution(JoinPoint joinPoint) {
			logger.info("After Aspect - {} has executed",  joinPoint);
		}

		@AfterThrowing(
		pointcut = "com.springboot.rest.aop.aopexample.aspects.CommonPointcutConfig.businessAndDataPackageConfig()",
		throwing = "exception"
		)
		public void logMethodCallAfterException(JoinPoint joinPoint, Exception exception) {
			logger.info("AfterThrowing Aspect - {} has thrown an exception {}"
					,  joinPoint, exception);
		}

		@AfterReturning(
		pointcut = "com.springboot.rest.aop.aopexample.aspects.CommonPointcutConfig.dataPackageConfig()",
		returning = "resultValue"
		)
		public void logMethodCallAfterSuccessfulExecution(JoinPoint joinPoint, 
				Object resultValue) {
			logger.info("AfterReturning Aspect - {} has returned {}"
					,  joinPoint, resultValue);
		}

	}
```

Please make note we remove “execution” from the string

### Creating Custom Annotation @TrackTime

```java
package com.springboot.rest.aop.aopexample.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackTime {

}
```

Adding that in custompointcutconfig

```java
@Pointcut("@annotation(com.springboot.rest.aop.aopexample.annotations.TrackTime)")
	public void trackTimeAnnotation() {}
```

Now we can just add the annotation and refer the @ to TrackTime config to run TrackTime Aspect