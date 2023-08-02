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
	
	@Pointcut("@annotation(com.springboot.rest.aop.aopexample.annotations.TrackTime)")
	public void trackTimeAnnotation() {}
}
