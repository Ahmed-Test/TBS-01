package sa.tamkeentech.tbs.event;

import sa.tamkeentech.tbs.config.Constants;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/**
 * Custom Event publisher.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface TBSEventPub {

    // principal must be part of the request
    String principal() default "principalId";

    // principal must be part of the response
    String referenceId() default "referenceId";

    Constants.EventType eventName();

}
