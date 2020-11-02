package com.polykhel.ssq.utils;

import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = getLogger(HeaderUtil.class);

    private HeaderUtil() {
    }

    /**
     * Create Alert
     *
     * @param applicationName a {@link String} object.
     * @param message         a {@link String} object.
     * @param param           a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createAlert(String applicationName, String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", message);
        try {
            headers.add("X-" + applicationName + "-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            // StandardCharsets are supported by every Java implementation so this exception will never happen
        }
        return headers;
    }

    /**
     * Create Entity Creation Alert
     *
     * @param applicationName   a {@link String} object.
     * @param enableTranslation a boolean.
     * @param entityName        a {@link String} object.
     * @param param             a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityCreationAlert(String applicationName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".created"
            : "A new " + entityName + " is created with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    /**
     * Create Entity Update Alert
     *
     * @param applicationName   a {@link String} object.
     * @param enableTranslation a boolean.
     * @param entityName        a {@link String} object.
     * @param param             a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityUpdateAlert(String applicationName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".updated"
            : "A " + entityName + " is updated with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    /**
     * Create Entity Deletion Alert
     *
     * @param applicationName   a {@link String} object.
     * @param enableTranslation a boolean.
     * @param entityName        a {@link String} object.
     * @param param             a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityDeletionAlert(String applicationName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".deleted"
            : "A " + entityName + " is deleted with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    /**
     * Create Failure Alert
     *
     * @param applicationName   a {@link String} object.
     * @param enableTranslation a boolean.
     * @param entityName        a {@link String} object.
     * @param errorKey          a {@link String} object.
     * @param defaultMessage    a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createFailureAlert(String applicationName, boolean enableTranslation, String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);

        String message = enableTranslation ? "error." + errorKey : defaultMessage;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-error", message);
        headers.add("X-" + applicationName + "-params", entityName);
        return headers;
    }
}
