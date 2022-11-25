package com.lt.modules.sys.mapper;

import com.lt.modules.sys.model.entity.QuestionBank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.modules.sys.model.vo.bank.QuestionBankVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author teng
 * @description 针对表【question_bank(题库表)】的数据库操作Mapper
 * @createDate 2022-11-22 09:24:12
 * @Entity com.lt.modules.sys.model.entity.QuestionBank
 */
@Repository
public interface QuestionBankMapper extends BaseMapper<QuestionBank> {

    /**
     * 根据机构名称查询题库信息
     */
    List<QuestionBankVO> getQuestionBank(String bankName);
}




