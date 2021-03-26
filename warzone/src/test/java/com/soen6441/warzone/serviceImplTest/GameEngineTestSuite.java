package com.soen6441.warzone.serviceImplTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({GameEngineServiceTest.class})
public class GameEngineTestSuite {

    /**
     * This method is used to load Springboot Application Context
     */
    @Test
    public void contextLoads() {

    }
}
