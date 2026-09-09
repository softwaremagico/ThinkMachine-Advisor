package com.softwaremagico.tm.advisor.core;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FileUtilsTest {

    @Test
    public void readFileReturnsEmptyStringForNullPath() {
        assertEquals("", FileUtils.readFile((String) null));
    }

    @Test
    public void readFileReturnsEmptyStringForMissingFile() {
        assertEquals("", FileUtils.readFile("/path/that/does/not/exist"));
    }

    @Test
    public void readFileReadsAndDeletesFileWhenRequested() throws Exception {
        final Path tempFile = Files.createTempFile("think-machine", ".txt");
        Files.write(tempFile, "hello".getBytes(StandardCharsets.UTF_8));

        assertEquals("hello", FileUtils.readFile(tempFile.toString(), true));
        assertFalse(Files.exists(tempFile));
    }

    @Test
    public void sanitizeFileNameReplacesReservedCharacters() {
        assertEquals("name_with_invalid_chars", FileUtils.sanitizeFileName("name/with:invalid*chars"));
    }
}
