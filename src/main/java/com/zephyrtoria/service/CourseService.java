package com.zephyrtoria.service;

import com.zephyrtoria.pojo.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyrtoria.utils.Result;

/**
 * @author Lenovo
 * @description 针对表【course(课程)】的数据库操作Service
 * @createDate 2024-12-17 19:06:33
 */
public interface CourseService extends IService<Course> {

    /**
     * 获取所有的课程信息
     *
     * @return
     */
    Result getAllCourses();

    /**
     * 获取课程规划
     *
     * @param credit
     * @return
     */
    Result getCoursePlan(double credit);
}
