package br.com.exemplo.demofileapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailConstraint {
    String message() default "Email must be a well-formed address";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
