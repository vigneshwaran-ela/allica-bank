package com.allica.allica_bank;

import org.junit.jupiter.api.Test;

/**
 * Unit test for the {@link AllicaBankApplication} main class.
 */
class AllicaBankApplicationTests {

    /**
     * Tests that the main method executes without throwing any exceptions.
     * 
     * This ensures that the Spring Boot application context can be loaded
     * successfully when the application is launched.
     */
    @Test
    void main_whenInvoked_shouldRunWithoutException() {
        AllicaBankApplication.main(new String[] {});
    }
}