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
import com.zephyrtoria.pojo.VO.CourseTableVo;

import java.util.*;

public final class CourseGraph {
    private final String SPRING = "Spring";
    private final String AUTUMN = "Fall";

    // 存放数据
    private final List<Course> courses;
    private final List<CoursePeriod> coursePeriodList;
    private final List<Prereq> prereqs;
    private final List<Succeed> succeeds;

    // 邻接表
    private final List<CourseDetailBo> graph;
    // 课程courseBasicId和graph中的索引对应
    private final Map<String, Integer> basicIdToIndex;
    // 节点个数
    private int n;

    /**
     * 初始化图，不允许无参构造创建
     *
     * @param courses
     * @param coursePeriodList
     * @param prereqs
     * @param succeeds
     */
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
                getCourseBo(to).addInEdge();
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

    public List<CourseTableVo> createPlan() {
        List<CourseTableVo> result = new ArrayList<>(8);
        boolean[] visited = new boolean[n];

        // 拓扑排序
        Deque<CourseDetailBo> spring = new LinkedList<>();
        Deque<CourseDetailBo> autumn = new LinkedList<>();
        for (CourseDetailBo eachCourse : graph) {
            if (eachCourse.hasNoInEdge() && eachCourse.getSemester().equals(AUTUMN)) {
                visited[eachCourse.getIndex()] = true;
                autumn.addLast(eachCourse);
            }
        }

        // true->spring; false->autumn
        boolean semesterFlag = false;
        int round = 1;
        while (round <= 8) {
            CourseTableVo curRoundSemester = new CourseTableVo(round);
            if (round % 2 == 0 && spring.isEmpty() || round % 2 == 1 && autumn.isEmpty()) {
                round++;
                continue;
            }
            if (semesterFlag) {
                // spring
                // 取出队列元素
                int curRoundSize = spring.size();
                while (curRoundSize-- > 0) {
                    addCourseToTable(spring, visited, curRoundSemester);
                }
            } else {
                // autumn
                int curRoundSize = autumn.size();
                while (curRoundSize-- > 0) {
                    addCourseToTable(autumn, visited, curRoundSemester);
                }
            }
            addTopologicalItem(spring, autumn, visited);
            round++;
            semesterFlag = !semesterFlag;
            result.add(curRoundSemester);
        }
        return result;
    }


    private void addTopologicalItem(Deque<CourseDetailBo> spring, Deque<CourseDetailBo> autumn, boolean[] visited) {
        // TODO: succeed处理
        for (CourseDetailBo eachCourse : graph) {
            // 加入队列元素
            // 入度为0的加入队列末尾，如果入度为0且是后继课程则加入队首，注意加入哪个队列要判断属性
            // 寻找入度为0的点
            if (!visited[eachCourse.getIndex()] && eachCourse.hasNoInEdge()) {
                if (eachCourse.getSemester().equals(SPRING)) {
                    // 春季学期课程
                    spring.addLast(eachCourse);
                } else {
                    // 秋季学期
                    autumn.addLast(eachCourse);
                }
                visited[eachCourse.getIndex()] = true;
            }
        }
    }

    private void addCourseToTable(Deque<CourseDetailBo> queue, boolean[] visited, CourseTableVo curRoundSemester) {
        CourseDetailBo curCourse = queue.removeFirst();
        // 判断是否能加入课程表
        if (curRoundSemester.insertTime(curCourse)) {
            // 加入成功，删除该节点，以及对应边
            deleteNode(curCourse);
            visited[curCourse.getIndex()] = true;
        } else {
            // 加入失败，放回队列，这样子下一轮就会是最先出队
            queue.addLast(curCourse);
        }

    }

    /**
     * 删除点以及对应的边，从当前节点的出边出发，减小终点的入边数量
     *
     * @param deletedCourse
     */
    private void deleteNode(CourseDetailBo deletedCourse) {
        List<String> outEdges = deletedCourse.getOutEdges();
        for (String edge : outEdges) {
            CourseDetailBo courseBo = getCourseBo(edge);
            courseBo.deleteInEdge();
        }
    }


    private CourseDetailBo getCourseBo(String courseBasicId) {
        return graph.get(basicIdToIndex.get(courseBasicId));
    }

    private boolean hasCourseBo(String courseBasicId) {
        return basicIdToIndex.containsKey(courseBasicId);
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
