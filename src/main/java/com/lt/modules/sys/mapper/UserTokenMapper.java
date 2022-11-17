package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.UserToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【user_token(系统用户Token)】的数据库操作Mapper
 * @createDate 2022-11-17 11:26:32
 * @Entity com.lt.model.entity.UserToken
 */
@Repository
public interface UserTokenMapper extends BaseMapper<UserToken> {

}




