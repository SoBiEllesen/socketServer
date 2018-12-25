package com.ixtens.server.service.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WorkerLoader {

    private static Log logger = LogFactory.getLog(WorkerLoader.class);
    private static final String resourceName = "service.properties";

    @Nullable
    private static Object loadWorker(String className) {
        try {
            Constructor<?> ctor = Class.forName(WorkerLoader.class.getPackageName() + "." + className).getConstructor();
            return ctor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Cant load class " + e.getMessage());
        }
        return null;
    }

    public static Map<String, Object> loadWorkers() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            assert resourceStream != null;
            props.load(resourceStream);
        }
        Map<String, Object> result = new HashMap<>();
        props.forEach((key, value) -> {
            Object workerService = loadWorker(value.toString());
            if (workerService != null) {
                result.put(key.toString(), workerService);
            }
        });
        return result;
    }
}
