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
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    List<String> queryAllPerms(long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);
}




