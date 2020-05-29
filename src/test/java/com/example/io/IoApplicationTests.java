package com.example.io;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IoApplication.class)
public class IoApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println("test");
    }

}
