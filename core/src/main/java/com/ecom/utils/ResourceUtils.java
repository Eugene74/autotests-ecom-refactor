package com.ecom.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/** Utility helpers for resolving resources regardless of module layout. */
public final class ResourceUtils {

  private static final String CLASSPATH_PREFIX = "classpath:";
  private static final String SRC_MAIN_RESOURCES_PREFIX = "src/main/resources/";

  private ResourceUtils() {}

  /**
   * Opens a resource as an {@link InputStream}. The path may be provided as a plain classpath
   * resource (e.g. {@code template/xml/paylink/PayRequest.xml}) or as a legacy
   * {@code src/main/resources/...} location.
   */
  public static InputStream stream(String resourcePath) {
    String normalized = normalize(resourcePath);
    InputStream classpathStream = ResourceUtils.class.getResourceAsStream(normalized);
    if (classpathStream != null) {
      return classpathStream;
    }

    Path fileSystemPath = Paths.get(resourcePath.replace('\\', '/'));
    if (Files.exists(fileSystemPath)) {
      try {
        return Files.newInputStream(fileSystemPath);
      } catch (IOException exception) {
        throw new IllegalStateException("Unable to open resource: " + resourcePath, exception);
      }
    }

    throw new IllegalStateException("Resource not found: " + resourcePath);
  }

  /** Reads a resource fully into a {@link String}. */
  public static String readAsString(String resourcePath) {
    try (InputStream inputStream = stream(resourcePath)) {
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to read resource: " + resourcePath, exception);
    }
  }

  /**
   * Resolves the resource to a {@link Path}. Classpath resources are copied to a temporary file so
   * code that requires {@link Path} access keeps functioning.
   */
  public static Path toTempFile(String resourcePath) {
    try (InputStream inputStream = stream(resourcePath)) {
      Path target = Files.createTempFile("resource-", "-" + fileName(resourcePath));
      Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
      target.toFile().deleteOnExit();
      return target;
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to copy resource: " + resourcePath, exception);
    }
  }

  private static String normalize(String resourcePath) {
    if (resourcePath == null || resourcePath.isBlank()) {
      throw new IllegalArgumentException("Resource path must not be blank");
    }

    String cleaned = resourcePath.trim().replace('\\', '/');
    if (cleaned.startsWith(CLASSPATH_PREFIX)) {
      cleaned = cleaned.substring(CLASSPATH_PREFIX.length());
    }
    if (cleaned.startsWith("./")) {
      cleaned = cleaned.substring(2);
    }
    if (cleaned.startsWith(SRC_MAIN_RESOURCES_PREFIX)) {
      cleaned = cleaned.substring(SRC_MAIN_RESOURCES_PREFIX.length());
    }
    if (!cleaned.startsWith("/")) {
      cleaned = '/' + cleaned;
    }
    return cleaned;
  }

  private static String fileName(String resourcePath) {
    String normalized = normalize(resourcePath);
    return Paths.get(normalized).getFileName().toString();
  }
}
