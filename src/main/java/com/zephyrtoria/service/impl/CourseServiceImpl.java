package com.zephyrtoria.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyrtoria.pojo.Course;
import com.zephyrtoria.service.CourseService;
import com.zephyrtoria.mapper.CourseMapper;
import com.zephyrtoria.utils.Result;
import com.zephyrtoria.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Lenovo
 * @description 针对表【course(课程)】的数据库操作Service实现
 * @createDate 2024-12-17 19:06:33
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
        implements CourseService {

    @Autowired
    private CourseMapper courseMapper;


    @Override
    public Result getAllCourses() {
        List<Course> courses = courseMapper.selectList(null);
        if (courses == null || courses.isEmpty()) {
            return Result.build(null, ResultCodeEnum.ERROR);
        }
        return Result.ok(courses);
    }
}




