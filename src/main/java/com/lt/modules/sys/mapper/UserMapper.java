package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author teng
 * @description 针对表【user(用户表)】的数据库操作Mapper
 * @createDate 2022-11-16 19:41:16
 * @Entity com.lt.model.entity.User
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询对应角色信息
     *
     * @param principal 用户身份---用户名
     * @return 用户角色信息
     */
    List<String> getUserRoleInfo(@Param("principal") String principal);

    /**
     * 根据角色名查询对应权限信息
     *
     * @param roles 角色名集合
     * @return 该角色对应的权限集合
     */
    List<String> getUserPermissionInfo(@Param("roles") List<String> roles);

    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    List<String> queryAllPerms(long userId);
}




