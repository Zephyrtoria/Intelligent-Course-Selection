/**
 * @Author: Zephyrtoria
 * @CreateTime: 2024-12-20
 * @Description: 图类
 * @Version: 1.0
 */

package com.zephyrtoria.algorithm;

import com.zephyrtoria.pojo.BO.CourseDetailBo;
import com.zephyrtoria.pojo.Course;
import com.zephyrtoria.pojo.CoursePeriod;
import com.zephyrtoria.pojo.Prereq;
import com.zephyrtoria.pojo.Succeed;

import java.util.*;

public final class CourseGraph {
    // 存放备用
    private final List<Course> courses;
    private final List<CoursePeriod> coursePeriodList;
    private final List<Prereq> prereqs;
    private final List<Succeed> succeeds;

    // 邻接表
    private List<CourseDetailBo> graph;
    // 课程courseBasicId和graph中的索引对应
    private Map<String, Integer> basicIdToIndex;
    // 节点个数
    private int n;

    // 不允许无参构造创建
    public CourseGraph(List<Course> courses, List<CoursePeriod> coursePeriodList, List<Prereq> prereqs, List<Succeed> succeeds) {
        this.courses = courses;
        this.coursePeriodList = coursePeriodList;
        this.prereqs = prereqs;
        this.succeeds = succeeds;

        graph = new ArrayList<>();
        basicIdToIndex = new HashMap<>();
        n = 0;

        /*
         * 1. 存入所有Course
         * 2. 存入所有CoursePeriod
         * 3. 存入边，prereq和succeed都存
         * */

        // 存入所有Course
        for (Course course : courses) {
            String basicId = course.getCourseBasicId();
            if (basicIdToIndex.containsKey(basicId)) {
                // 图中已经存入了该门课，即当前数据是同一门课的不同老师
                // 获取data索引
                int index = basicIdToIndex.get(basicId);
                graph.get(index).addCourse(course);
            } else {
                // 还没有存入该门课
                CourseDetailBo courseDetailBo = new CourseDetailBo(course);
                courseDetailBo.addCourse(course);
                graph.add(courseDetailBo);
                basicIdToIndex.put(basicId, n++);
            }
        }

        // 存入所有CoursePeriod
        for (CoursePeriod period : coursePeriodList) {
            String basicId = period.getCourseBasicId();
            if (basicIdToIndex.containsKey(basicId)) {
                // 正常来说是必须有的，否则就算缺失信息
                int index = basicIdToIndex.get(basicId);
                graph.get(index).addPeriod(period);
            }
        }

        // 存入prereq
        for (Prereq prereq : prereqs) {
            String from = prereq.getPrereqId();
            String to = prereq.getCourseBasicId();
            int index = basicIdToIndex.get(from);
            graph.get(index).addEdges(to);
        }

        // 存入succeed
        for (Succeed succeed : succeeds) {
            String from = succeed.getCourseBasicId();
            String to = succeed.getSucceedCourseBasicId();
            int index = basicIdToIndex.get(from);
            graph.get(index).addSucceed(to);
        }
    }

    public void showData() {
        for (CourseDetailBo courseDetailBo : graph) {
            System.out.println(courseDetailBo.toString());
        }
    }
}
