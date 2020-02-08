package com.app.mdc.mapper.mdc;

import com.app.mdc.model.mdc.Contract;
import com.app.mdc.model.mdc.UserContract;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface UserContractMapper extends BaseMapper<UserContract> {

    @Select("select sc.id,amount,income_rate as incomeRate from mdc_user_contract muc join sys_contract sc on sc.id = muc.contract_id where muc.user_id = #{userId} and sc.type = #{type}")
    Contract selectContractByUserId(Integer userId, Integer type);
}
