/**
 * @Author: Zephyrtoria
 * @CreateTime: 2024-12-24
 * @Description:
 * @Version: 1.0
 */

package com.zephyrtoria.algorithm;

import com.zephyrtoria.pojo.BO.CourseDetailBo;
import com.zephyrtoria.pojo.Course;
import com.zephyrtoria.pojo.CoursePeriod;
import com.zephyrtoria.pojo.Prereq;
import com.zephyrtoria.pojo.Succeed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseGraph {    // 邻接表
    protected final List<CourseDetailBo> graph;
    // 课程courseBasicId和graph中的索引对应
    protected final Map<String, Integer> basicIdToIndex;
    // 节点个数
    protected int n;

    /**
     * 初始化图，不允许无参构造创建
     *
     * @param courses
     * @param coursePeriodList
     * @param prereqs
     * @param succeeds
     */
    protected BaseGraph(List<Course> courses, List<CoursePeriod> coursePeriodList, List<Prereq> prereqs, List<Succeed> succeeds) {
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
            if (hasCourseBo(basicId)) {
                // 图中已经存入了该门课，即当前数据是同一门课的不同老师
                // 获取data索引
                getCourseBo(basicId).addCourse(course);
            } else {
                // 还没有存入该门课
                CourseDetailBo courseDetailBo = new CourseDetailBo(n, course);
                courseDetailBo.addCourse(course);
                graph.add(courseDetailBo);
                basicIdToIndex.put(basicId, n++);
            }
        }

        // 存入所有CoursePeriod
        for (CoursePeriod period : coursePeriodList) {
            String basicId = period.getCourseBasicId();
            // 要根据院系划分课程，所以要判断是否存在
            if (hasCourseBo(basicId)) {
                getCourseBo(basicId).addPeriod(period);
            }
        }

        // 存入prereq
        for (Prereq prereq : prereqs) {
            String from = prereq.getPrereqId();
            String to = prereq.getCourseBasicId();
            if (hasCourseBo(from) && hasCourseBo(to)) {
                // 出度维护
                getCourseBo(from).addOutEdge(to);
                // 入度维护
                getCourseBo(to).addInEdge(from);
            }
        }

        // 存入succeed
        for (Succeed succeed : succeeds) {
            String from = succeed.getCourseBasicId();
            String to = succeed.getSucceedCourseBasicId();
            if (hasCourseBo(from) && hasCourseBo(to)) {
                getCourseBo(from).addSucceed(to);
            }
        }
    }

    protected int getIndexByCourseBo(CourseDetailBo courseDetailBo) {
        return basicIdToIndex.get(courseDetailBo.getCourseBasicId());
    }

    protected CourseDetailBo getCourseBo(String courseBasicId) {
        return graph.get(basicIdToIndex.get(courseBasicId));
    }

    protected boolean hasCourseBo(String courseBasicId) {
        return basicIdToIndex.containsKey(courseBasicId);
    }

    /**
     * 删除点以及对应的边，从当前节点的出边出发，减小终点的入边数量
     *
     * @param deletedCourse
     */
    protected void deleteNode(CourseDetailBo deletedCourse) {
        List<String> outEdges = deletedCourse.getOutEdges();
        for (String edge : outEdges) {
            CourseDetailBo courseBo = getCourseBo(edge);
            courseBo.deleteInEdge();
        }
    }

    /**
     * 展示图数据
     */
    public void showData() {
        for (CourseDetailBo courseDetailBo : graph) {
            System.out.println(courseDetailBo.toString());
        }
    }
}
