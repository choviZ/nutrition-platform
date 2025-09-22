package com.zcw.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcw.springbootinit.model.entity.UserHealthProfile;
import com.zcw.springbootinit.service.UserHealthProfileService;
import com.zcw.springbootinit.mapper.UserHealthProfileMapper;
import org.springframework.stereotype.Service;

/**
* @author zcw
* @description 针对表【user_health_profile(用户健康档案表)】的数据库操作Service实现
* @createDate 2025-09-22 09:14:28
*/
@Service
public class UserHealthProfileServiceImpl extends ServiceImpl<UserHealthProfileMapper, UserHealthProfile>
    implements UserHealthProfileService{

}




