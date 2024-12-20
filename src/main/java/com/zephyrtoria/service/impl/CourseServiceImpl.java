package com.zephyrtoria.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyrtoria.algorithm.CourseGraph;
import com.zephyrtoria.mapper.CoursePeriodMapper;
import com.zephyrtoria.mapper.PrereqMapper;
import com.zephyrtoria.mapper.SucceedMapper;
import com.zephyrtoria.pojo.Course;
import com.zephyrtoria.pojo.CoursePeriod;
import com.zephyrtoria.pojo.Prereq;
import com.zephyrtoria.pojo.Succeed;
import com.zephyrtoria.service.CourseService;
import com.zephyrtoria.mapper.CourseMapper;
import com.zephyrtoria.utils.Result;
import com.zephyrtoria.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private CoursePeriodMapper coursePeriodMapper;
    @Autowired
    private PrereqMapper prereqMapper;
    @Autowired
    private SucceedMapper succeedMapper;


    @Override
    public Result getAllCourses() {
        List<Course> courses = courseMapper.selectList(null);
        if (courses == null || courses.isEmpty()) {
            return Result.build(null, ResultCodeEnum.ERROR);
        }
        return Result.ok(courses);
    }

    /**
     * 规划课表
     *
     * @param credit
     * @return
     */
    @Override
    public Result getCoursePlan(double credit) {
        List<Course> courses = courseMapper.selectList(null);
        List<CoursePeriod> coursePeriods = coursePeriodMapper.selectList(null);
        List<Prereq> prereqs = prereqMapper.selectList(null);
        List<Succeed> succeeds = succeedMapper.selectList(null);
        CourseGraph graph = new CourseGraph(courses, coursePeriods, prereqs, succeeds);
        graph.showData();

        return null;
    }
}




