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

public final class BasicPlanGraph extends BaseGraph {
    public BasicPlanGraph(List<Course> courses, List<CoursePeriod> coursePeriodList, List<Prereq> prereqs, List<Succeed> succeeds) {
        super(courses, coursePeriodList, prereqs, succeeds);
    }

    public List<CourseTableVo> createPlan() {
        List<CourseTableVo> result = new ArrayList<>(8);
        boolean[] visited = new boolean[n];

        // 拓扑排序
        Deque<CourseDetailBo> spring = new LinkedList<>();
        Deque<CourseDetailBo> autumn = new LinkedList<>();
        for (CourseDetailBo eachCourse : graph) {
            String AUTUMN = "Fall";
            if (eachCourse.hasNoTempInEdge() && eachCourse.getSemester().equals(AUTUMN)) {
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
                    addCourseToTable(spring, autumn, visited, curRoundSemester);
                }
            } else {
                // autumn
                int curRoundSize = autumn.size();
                while (curRoundSize-- > 0) {
                    addCourseToTable(autumn, spring, visited, curRoundSemester);
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
        for (CourseDetailBo eachCourse : graph) {
            // 加入队列元素
            // 入度为0的加入队列末尾，如果入度为0且是后继课程则加入队首，注意加入哪个队列要判断属性
            // 寻找入度为0的点
            if (!visited[eachCourse.getIndex()] && eachCourse.hasNoTempInEdge()) {
                String SPRING = "Spring";
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

    private void addCourseToTable(Deque<CourseDetailBo> queue, Deque<CourseDetailBo> nextQueue, boolean[] visited, CourseTableVo curRoundSemester) {
        CourseDetailBo curCourse = queue.removeFirst();
        // 判断是否能加入课程表
        if (curRoundSemester.insertTime(curCourse)) {
            // 加入成功，删除该节点，以及对应边
            deleteNode(curCourse);
            visited[curCourse.getIndex()] = true;
            for (String succeed : curCourse.getSucceed()) {
                if (!visited[basicIdToIndex.get(succeed)]) {
                    nextQueue.addFirst(getCourseBo(succeed));
                }
            }
        } else {
            // 加入失败，放回队列，这样子下一轮就会是最先出队
            queue.addLast(curCourse);
        }
    }
}
