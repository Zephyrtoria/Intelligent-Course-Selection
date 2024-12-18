/**
 * @Author: Zephyrtoria
 * @CreateTime: 2024-12-17
 * @Description:
 * @Version: 1.0
 */

package com.zephyrtoria.dao;

import com.zephyrtoria.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("course")
public class CourseController {
    @Autowired
    private CourseService courseService;


}
