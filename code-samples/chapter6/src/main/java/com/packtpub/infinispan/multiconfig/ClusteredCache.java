package com.packtpub.infinispan.multiconfig;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@javax.inject.Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.
METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClusteredCache {

}
