package org.mvnsearch.spring.boot.dubbo;

import java.lang.annotation.*;

/**
 * eanble dubbo configuration
 *
 * @author alex
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EnableDubboConfiguration {
    /**
     * scan package for dubbo
     */
    String value() default "";
}
