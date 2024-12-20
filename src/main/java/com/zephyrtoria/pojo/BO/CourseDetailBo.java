/**
 * @Author: Zephyrtoria
 * @CreateTime: 2024-12-20
 * @Description:
 * @Version: 1.0
 */

package com.zephyrtoria.pojo.BO;

import com.zephyrtoria.pojo.Course;
import com.zephyrtoria.pojo.CoursePeriod;
import com.zephyrtoria.pojo.Prereq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class CourseDetailBo {
    // 同一门课程共享的信息
    private String courseBasicId;

    private String courseName;

    private String semester;

    private String category;

    private Double credit;

    private Integer begWeek;

    private Integer lastWeek;

    // 区分succeed和edges
    private List<String> succeed;

    private List<String> edges;

    // 同一门课程不同老师，不共享的信息
    private Map<String, Course> spToCourse;
    // 根据courseSpId来获取对应的CoursePeriod，且一门课会对应多个CoursePeriod，所以采用容器存储
    private Map<String, List<CoursePeriod>> spToPeriods;

    public CourseDetailBo(Course course) {
        this.courseBasicId = course.getCourseBasicId();
        this.courseName = course.getCourseName();
        this.semester = course.getSemester();
        this.category = course.getCategory();
        this.credit = course.getCredit();
        this.begWeek = course.getBegWeek();
        this.lastWeek = course.getLastWeek();
        succeed = new LinkedList<>();
        edges = new LinkedList<>();
        spToCourse = new HashMap<>();
        spToPeriods = new HashMap<>();
    }


    public void addCourse(Course course) {
        String sp = course.getCourseSpId();
        // 可能出现重复数据（指完全重复）
        if (spToCourse.containsKey(sp)) {
            return;
        }
        spToCourse.put(sp, course);
    }


    public void addPeriod(CoursePeriod coursePeriod) {
        String sp = coursePeriod.getCourseSpId();

        if (spToPeriods.containsKey(sp)) {
            // 已有，说明是一门课一周有多次上课
            spToPeriods.get(sp).add(coursePeriod);
        } else {
            // 没有，新建
            ArrayList<CoursePeriod> temp = new ArrayList<>();
            temp.add(coursePeriod);
            spToPeriods.put(sp, temp);
        }
    }


    public void addEdges(String to) {
        edges.add(to);
    }

    public void addSucceed(String to) {
        succeed.add(to);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CourseDetailBo{courseBasicId=").append(courseBasicId);
        sb.append("', courseName='").append(courseName);
        sb.append("', semester='").append(semester);
        sb.append("', category='").append(category);
        sb.append("', credit='").append(credit);
        sb.append("', begWeek='").append(begWeek);
        sb.append("', lastWeek='").append(lastWeek);
        sb.append("', succeed={");
        for (String s : succeed) {
            sb.append(s).append(", ");
        }
        sb.append("}, edges={");
        for (String edge : edges) {
            sb.append(edge).append(", ");
        }
        sb.append("}, spToCourse={");
        for (String s : spToCourse.keySet()) {
            sb.append(s).append(", ");
        }
        sb.append("}, spToPeriods={");
        for (Map.Entry<String, List<CoursePeriod>> entry : spToPeriods.entrySet()) {
            sb.append(entry.getKey()).append("=[");
            for (CoursePeriod coursePeriod : entry.getValue()) {
                sb.append(coursePeriod.toString()).append(", ");
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDetailBo that = (CourseDetailBo) o;
        return Objects.equals(courseBasicId, that.courseBasicId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(courseBasicId);
    }
}
