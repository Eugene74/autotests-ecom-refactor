package com.ecom.tests.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class RetryListener implements IAnnotationTransformer {
  @Override
  public void transform(
      ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    if (annotation.getRetryAnalyzerClass() == null) {
      annotation.setRetryAnalyzer(com.ecom.tests.support.RetryAnalyzer.class);
    }
  }
}
