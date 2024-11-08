package com.lagab.blank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("context")
@SpringBootTest
@DisplayName("Context test for MainApplication")
class BlankApplicationTests {

    @Test
    @DisplayName("Context loads successfully")
    void contextLoads() {
        BlankApplication.main(new String[0]);
    }

}
