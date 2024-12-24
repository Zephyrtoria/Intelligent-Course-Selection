/**
 * @Author: Zephyrtoria
 * @CreateTime: 2024-12-24
 * @Description: 最快修读方案实现
 * @Version: 1.0
 */

package com.zephyrtoria.algorithm;


import com.zephyrtoria.pojo.BO.CourseDetailBo;
import com.zephyrtoria.pojo.Course;
import com.zephyrtoria.pojo.CoursePeriod;
import com.zephyrtoria.pojo.Prereq;
import com.zephyrtoria.pojo.Succeed;

import java.util.*;

public class FastestPlanGraph extends BaseGraph {
    private final Map<CourseDetailBo, Double> creditMap;
    private final Map<CourseDetailBo, Double> earliestMap;
    private final Map<CourseDetailBo, Double> lastestMap;
    private final Map<CourseDetailBo, Integer> inEdgesMap;
    private CourseDetailBo start, end;

    public FastestPlanGraph(List<Course> courses, List<CoursePeriod> coursePeriodList, List<Prereq> prereqs, List<Succeed> succeeds) {
        super(courses, coursePeriodList, prereqs, succeeds);
        creditMap = new HashMap<>();
        earliestMap = new HashMap<>();
        lastestMap = new HashMap<>();
        inEdgesMap = new HashMap<>();

        for (CourseDetailBo courseDetailBo : graph) {
            creditMap.put(courseDetailBo, courseDetailBo.getCredit());
            inEdgesMap.put(courseDetailBo, courseDetailBo.getInEdges().size());
        }
    }

    /**
     * 拓扑排序生成
     *
     * @return
     */
    private List<CourseDetailBo> topologicalSort() {
        Queue<CourseDetailBo> queue = new LinkedList<>();
        List<CourseDetailBo> result = new ArrayList<>();

        // 手动创造起点和终点
        start = new CourseDetailBo(0);
        end = new CourseDetailBo(-1);
        graph.add(start);
        graph.add(end);
        basicIdToIndex.put(start.getCourseBasicId(), n++);
        basicIdToIndex.put(end.getCourseBasicId(), n++);
        creditMap.put(start, 0.0);
        creditMap.put(end, 0.0);


        for (CourseDetailBo courseDetailBo : graph) {
            if (courseDetailBo.equals(start) || courseDetailBo.equals(end)) {
                continue;
            }
            // 入度为0，与起点连接
            if (courseDetailBo.hasNoTempInEdge()) {
                start.addOutEdge(courseDetailBo.getCourseBasicId());
                courseDetailBo.addInEdge(start.getCourseBasicId());
            }

            // 出度为0，与终点连接
            if (courseDetailBo.getOutEdges().isEmpty()) {
                courseDetailBo.addOutEdge(end.getCourseBasicId());
                end.addInEdge(courseDetailBo.getCourseBasicId());
            }
        }

        inEdgesMap.put(start, 0);
        inEdgesMap.put(end, end.getInEdges().size());

        queue.add(start);
        while (!queue.isEmpty()) {
            CourseDetailBo cur = queue.remove();
            deleteNode(cur);
            result.add(cur);
            for (String outEdge : cur.getOutEdges()) {
                CourseDetailBo courseBo = getCourseBo(outEdge);
                if (courseBo.hasNoTempInEdge()) {
                    queue.add(courseBo);
                }
            }
        }
        return result;
    }

    private List<CourseDetailBo> criticalPath(List<CourseDetailBo> topologicalSorted) {
        // 最早发生时间 EST
        // 初始化
        for (CourseDetailBo courseDetailBo : graph) {
            earliestMap.put(courseDetailBo, 0.0);
        }

        for (CourseDetailBo cur : topologicalSorted) {
            for (String outEdge : cur.getOutEdges()) {
                CourseDetailBo neighbor = getCourseBo(outEdge);
                earliestMap.put(neighbor, Math.max(earliestMap.get(neighbor), earliestMap.get(cur) + creditMap.get(cur)));
            }
        }

        // 最迟发生时间 LST
        // 初始化
        Double maxTime = earliestMap.values().stream().max(Double::compareTo).orElse(0.0);
        for (CourseDetailBo courseDetailBo : graph) {
            lastestMap.put(courseDetailBo, maxTime);
        }

        // 逆序遍历
        Collections.reverse(topologicalSorted);
        for (CourseDetailBo cur : topologicalSorted) {
            for (String inEdge : cur.getInEdges()) {
                CourseDetailBo neighbor = getCourseBo(inEdge);
                lastestMap.put(neighbor, Math.min(lastestMap.get(neighbor), lastestMap.get(cur) - creditMap.get(neighbor)));
            }
        }

        List<CourseDetailBo> criticalPath = new ArrayList<>();
        Collections.reverse(topologicalSorted);
        for (CourseDetailBo cur : topologicalSorted) {
            if (cur.equals(start) || cur.equals(end)) {
                continue;
            }
            if (earliestMap.get(cur).equals(lastestMap.get(cur))) {
                criticalPath.add(cur);
            }
        }
        return criticalPath;
    }


    public List<List<String>> AOEForFastestPlan(List<Double> credits) {
        // 拓扑排序
        List<CourseDetailBo> topologicalSorted = topologicalSort();

        // 计算关键路径
        List<CourseDetailBo> criticalPath = criticalPath(topologicalSorted);

        topologicalSorted.remove(start);
        topologicalSorted.remove(end);

        // 关键路径上的课程必然会分配到不同的学期，所以如果关键路径长度大于8，必然不能成功规划
        if (criticalPath.size() > 8) {
            return null;
        }

        // 结果
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            result.add(new ArrayList<>());
        }
        // 记录每个学期已经使用的学分
        List<Double> recordCredits = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            recordCredits.add(0.0);
        }

        int round = 0;
        double creditLimit = credits.get(round);
        List<String> curSemester = result.get(round);
        double currentCredits = 0.0;
        currentCredits = recordCredits.get(round);

        for (CourseDetailBo curCourse : topologicalSorted) {
            // 只要按拓扑顺序取出元素，必然不会有未完成的前置课程
            double courseCredit = curCourse.getCredit();
            if (currentCredits + courseCredit > creditLimit) {
                // 超出学期数限制
                if (round >= 8) {
                    return null;
                }
                // 继承之前使用过的学分
                creditLimit = credits.get(round);
                // 切换到下一学期
                curSemester = result.get(round);
                currentCredits = recordCredits.get(round);
                round++;
            }
            curSemester.add(curCourse.getCourseName());
            currentCredits += courseCredit;
            for (String outEdge : curCourse.getOutEdges()) {
                CourseDetailBo neighbor = getCourseBo(outEdge);
                inEdgesMap.put(neighbor, inEdgesMap.get(neighbor) - 1);
            }
        }

        return result;
    }
}
