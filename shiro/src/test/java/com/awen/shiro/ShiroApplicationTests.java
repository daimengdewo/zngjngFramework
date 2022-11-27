package com.awen.shiro;

import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShiroApplicationTests {

    @Test
    void contextLoads() {
        String s = SecureUtil.md5("123456zngjng");
        System.out.println(s);
    }

}
