/**
 * @Author: Zephyrtoria
 * @CreateTime: 2024-12-16
 * @Description: 上课时间
 * @Version: 1.0
 */

package com.zephyrtoria.pojo;

import lombok.Data;

@Data
public class CoursePeriod {
    private String CourseBasicId;   // 课程基础序号
    private Integer courseSpId;     // 课程班级序号
    private String day;        // 上课日期
    private Integer beg;        // 开始节数
    private Integer last;       // 持续节数
}
