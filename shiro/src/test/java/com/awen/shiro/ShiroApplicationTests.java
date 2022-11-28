package com.awen.shiro;

import com.awen.shiro.entity.Role;
import com.awen.shiro.tool.MapperMenu;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShiroApplicationTests {

    @Autowired
    MapperMenu mapperMenu = new MapperMenu();

    @Test
    void contextLoads() {
        //去重条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, "admin");
        //查询重复角色
        Integer count = mapperMenu.getRoleMapper().selectCount(wrapper);
        System.out.println(count);
    }

}
