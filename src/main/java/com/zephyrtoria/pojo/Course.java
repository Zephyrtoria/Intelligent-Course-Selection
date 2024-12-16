/**
 * @Author: Zephyrtoria
 * @CreateTime: 2024-12-16
 * @Description: 课程类
 * @Version: 1.0
 */

package com.zephyrtoria.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Course {
    private String courseBasicId;   // 课程基础序号
    private Integer courseSpId; // 课程班级序号
    private String courseName;  // 课程名称
    private String teacher;     // 教师
    private String department;  // 开设院系
    private String semester;   // 学期
    private String catalog;     // 课程类别
    private Double credit;     // 学分
    private Integer begWeek;    // 开始周数
    private Integer lastWeek;   // 持续周数
    private Integer limits;     // 选课人数上限
    private List<String> prerequisites;     // 前置课程
    private List<String> successors;        // 后继课程
}
