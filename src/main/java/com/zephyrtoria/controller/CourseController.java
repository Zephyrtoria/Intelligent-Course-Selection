/**
 * @Author: Zephyrtoria
 * @CreateTime: 2024-12-17
 * @Description:
 * @Version: 1.0
 */

package com.zephyrtoria.controller;

import com.zephyrtoria.pojo.VO.CreditsLimitVo;
import com.zephyrtoria.service.CourseService;
import com.zephyrtoria.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("course")
@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;

    /**
     * 获取所有课程信息
     *
     * @return
     */
    @GetMapping("getAllCourses")
    public Result<Object> getAllCourse() {
        Result<Object> result = courseService.getAllCourses();
        System.out.println("result = " + result);
        return result;
    }


    /**
     * 普通规划课程
     *
     * @param department 根据院系划分
     * @return
     */
    @GetMapping("getCoursePlan")
    public Result<Object> getCoursePlan(String department) {
        Result<Object> result = courseService.getCoursePlan(department);
        System.out.println("result = " + result);
        return result;
    }

    @PostMapping("getFastestPlan")
    public Result<Object> getFastestPlan(@RequestBody CreditsLimitVo creditsLimit) {
        Result<Object> result = courseService.getFastestPlan(creditsLimit.getDepartment(), creditsLimit.getCreditsLimit());
        System.out.println("result = " + result);
        return result;
    }
}