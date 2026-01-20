package com.asia.logistics.load.component;

import com.asia.logistics.load.dto.OptimizeLoadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;

@Component("optimizeLoadKeyGenerator")
@RequiredArgsConstructor
@Slf4j
public class OptimizedLoadKeyGenerator implements KeyGenerator {
    private final ObjectMapper objectMapper;
    private final static String SHA256 = "SHA-256";
    private final static String NAMESPACE_OPT = "opt";

    /**
     * Generates a deterministic cache key for load optimization requests.
     *
     * <p>Assumptions: The cached method has exactly one argument i.e. {@link OptimizeLoadRequest}
     *
     * <p>The request is serialized to JSON and hashed using SHA-256 to produce
     * a stable, compact Redis key independent of JVM object identity.</p>
     */
    @Override
    public Object generate(Object target, Method method, Object... params) {

        log.debug("Generating cache key for target={}, method={}, paramCount={}",
                target.getClass().getSimpleName(),
                method.getName(),
                params.length);

        if (params.length == 0 || !(params[0] instanceof OptimizeLoadRequest request)) {
            log.error("Invalid parameters for cache key generation. Expected OptimizeLoadRequest but got {}",
                    Arrays.toString(params));
            throw new IllegalArgumentException("OptimizeLoadKeyGenerator expects OptimizeLoadRequest as first argument");
        }

        try {
            // Serialize request into JSON to ensure deterministic hashing
            String json = objectMapper.writeValueAsString(request);

            // Hash the request
            MessageDigest digest = MessageDigest.getInstance(SHA256);
            byte[] hash = digest.digest(json.getBytes(StandardCharsets.UTF_8));

            // Calculate the cache key
            String cacheKey = String.format("%s:%s", NAMESPACE_OPT, HexFormat.of().formatHex(hash));

            log.debug("Generated cache key for truckId={}, orderCount={}: {}",
                    request.getTruck().getId(),
                    request.getOrders().size(),
                    cacheKey);

            return cacheKey;

        } catch (Exception ex) {
            log.error("Failed to generate cache key for load optimization request", ex);
            throw new IllegalStateException("Failed to generate cache key", ex);
        }
    }
}
