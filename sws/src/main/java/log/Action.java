package log;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {
    String description() default "no description";
}
