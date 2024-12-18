package com.zephyrtoria.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyrtoria.pojo.Course;
import com.zephyrtoria.service.CourseService;
import com.zephyrtoria.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【course(课程)】的数据库操作Service实现
* @createDate 2024-12-17 19:06:33
*/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
    implements CourseService{

    @Autowired
    private CourseMapper courseMapper;



}




