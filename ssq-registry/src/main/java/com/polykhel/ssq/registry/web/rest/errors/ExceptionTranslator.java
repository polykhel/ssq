package com.polykhel.ssq.registry.web.rest.errors;

import com.polykhel.ssq.constants.ProfileConstants;
import com.polykhel.ssq.web.rest.errors.BadRequestAlertException;
import com.polykhel.ssq.web.rest.errors.ErrorConstants;
import com.polykhel.ssq.web.rest.errors.FieldErrorVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static com.polykhel.ssq.web.rest.utils.HeaderUtil.createFailureAlert;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    public static final String FIELD_ERRORS_KEY = "fieldErrors";
    public static final String MESSAGE_KEY = "message";
    public static final String PATH_KEY = "path";
    public static final String VIOLATIONS_KEY = "violations";

    @Value("${core.clientApp.name}")
    private String applicationName;

    private final Environment env;

    public ExceptionTranslator(Environment env) {
        this.env = env;
    }

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed.
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, @Nonnull NativeWebRequest request) {
        if (entity == null) {
            return null;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
            .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
            .withStatus(problem.getStatus())
            .withTitle(problem.getTitle())
            .with(PATH_KEY, Objects.requireNonNull(request.getNativeRequest(HttpServletRequest.class)).getRequestURI());

        if (problem instanceof ConstraintViolationProblem) {
            builder
                .with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations())
                .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        } else {
            builder
                .withCause(((DefaultProblem) problem).getCause())
                .withDetail(problem.getDetail())
                .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey(MESSAGE_KEY) && problem.getStatus() != null) {
                builder.with(MESSAGE_KEY, "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return ResponseEntity.status(entity.getStatusCode())
            .headers(entity.getHeaders())
            .body(builder.build());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(@Nonnull MethodArgumentNotValidException exception, @Nonnull NativeWebRequest request) {
        BindingResult result = exception.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors()
            .stream()
            .map(f -> new FieldErrorVM(f.getObjectName().replaceFirst("DTO$", ""), f.getField(), f.getCode()))
            .collect(Collectors.toList());

        Problem problem = Problem.builder()
            .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Method argument not valid")
            .withStatus(defaultConstraintViolationStatus())
            .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION)
            .with(FIELD_ERRORS_KEY, fieldErrors)
            .build();

        return create(exception, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, createFailureAlert(applicationName, false, ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    @Override
    public ProblemBuilder prepare(final Throwable throwable, final StatusType status, final URI type) {

        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

        if (activeProfiles.contains(ProfileConstants.SPRING_PROFILE_PRODUCTION)) {
            if (throwable instanceof HttpMessageConversionException) {
                return Problem.builder()
                        .withType(type)
                        .withTitle(status.getReasonPhrase())
                        .withStatus(status)
                        .withDetail("Unable to convert http message")
                        .withCause(Optional.ofNullable(throwable.getCause())
                                .filter(cause -> isCausalChainsEnabled())
                                .map(this::toProblem)
                                .orElse(null));
            }

            if (containsPackageName(throwable.getMessage())) {
                return Problem.builder()
                        .withType(type)
                        .withTitle(status.getReasonPhrase())
                        .withStatus(status)
                        .withDetail("Unexpected runtime exception")
                        .withCause(Optional.ofNullable(throwable.getCause())
                                .filter(cause -> isCausalChainsEnabled())
                                .map(this::toProblem)
                                .orElse(null));
            }
        }

        return Problem.builder()
                .withType(type)
                .withTitle(status.getReasonPhrase())
                .withStatus(status)
                .withDetail(throwable.getMessage())
                .withCause(Optional.ofNullable(throwable.getCause())
                        .filter(cause -> isCausalChainsEnabled())
                        .map(this::toProblem)
                        .orElse(null));
    }

    private boolean containsPackageName(String message) {
        // This list is for sure not complete
        return StringUtils.containsAny(message, "org.", "java.", "net.", "javax.", "com.", "io.", "de.", "com.polykhel.ssq.registry");
    }
}
