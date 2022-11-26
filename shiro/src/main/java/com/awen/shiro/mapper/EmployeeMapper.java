package com.awen.shiro.mapper;

import com.awen.shiro.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    //查询用户对应角色
    @Select("SELECT `name` FROM role WHERE id IN (SELECT role_id FROM employee_role WHERE emp_id = (SELECT id FROM employee WHERE username = #{username}))")
    List<String> getUserRoleInfoMapper(@Param("username") String username);

    //查询角色关联的权限信息
    @Select({
            "<script>",
            "SELECT `info` FROM permission WHERE id IN ",
            "(SELECT permission_id FROM role_permission WHERE role_id IN (",
            "SELECT id FROM role WHERE `name` IN ",
            "<foreach collection='roles' item='name' open='(' separator=',' close=')' >",
            "#{name}",
            "</foreach>",
            "))",
            "</script>"
    })
    List<String> getUserPermissionInfoMapper(@Param("roles") List<String> roles);
}
