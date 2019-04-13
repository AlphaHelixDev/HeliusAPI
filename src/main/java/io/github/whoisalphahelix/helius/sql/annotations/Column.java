/*
 * Decompiled with CFR 0_132.
 */
package io.github.whoisalphahelix.helius.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface Column {
	String name();
	
	String additionalQuery() default "";
}

