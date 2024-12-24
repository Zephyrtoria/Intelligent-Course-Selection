/**
 * @Author: Zephyrtoria
 * @CreateTime: 2024-12-20
 * @Description: 返回前端的课表
 * @Version: 1.0
 */

package com.zephyrtoria.pojo.VO;

import com.zephyrtoria.pojo.BO.CourseDetailBo;
import com.zephyrtoria.pojo.Course;
import com.zephyrtoria.pojo.CoursePeriod;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CourseTableVo {
    private final static Integer TIME_PARTITION = 14;   // 从1开始
    private final static Integer DAYS = 7;
    private final static Integer WEEKS = 18;    // 从1开始
    private final static Map<String, Integer> WEEK_TO_INDEX;

    static {
        WEEK_TO_INDEX = new HashMap<>();
        WEEK_TO_INDEX.put("Mon", 1);
        WEEK_TO_INDEX.put("Tue", 2);
        WEEK_TO_INDEX.put("Wed", 3);
        WEEK_TO_INDEX.put("Thu", 4);
        WEEK_TO_INDEX.put("Fri", 5);
        WEEK_TO_INDEX.put("Sat", 6);
        WEEK_TO_INDEX.put("Son", 7);
    }

    @Getter
    private final Integer semesterId;

    private final Course[][][] tables;

    public CourseTableVo(int semesterId) {
        this.semesterId = semesterId;
        tables = new Course[WEEKS + 1][DAYS + 1][TIME_PARTITION + 1];
    }

    /**
     * 插入时间
     *
     * @param courseDetailBo
     * @return 插入成功返回true;否则返回false
     */
    public boolean insertTime(CourseDetailBo courseDetailBo) {
        int begWeek = courseDetailBo.getBegWeek();
        int lastWeek = courseDetailBo.getLastWeek();

        // 要在多个老师中挑选满足的
        Map<String, List<CoursePeriod>> spToPeriods = courseDetailBo.getSpToPeriods();
        for (Map.Entry<String, List<CoursePeriod>> entry : spToPeriods.entrySet()) {
            // 一门课会有多个时间段，需要全部满足
            boolean timeValid = true;
            List<CoursePeriod> coursePeriods = entry.getValue();
            for (CoursePeriod coursePeriod : coursePeriods) {
                String day = coursePeriod.getDay();
                Integer beg = coursePeriod.getBeg();
                Integer last = coursePeriod.getLast();
                if (!checkTimeValid(begWeek, lastWeek, day, beg, last)) {
                    timeValid = false;
                    break;
                }
            }
            // 时间合法，说明一个或多个时间段均满足
            if (timeValid) {
                // 插入table中
                Course course = courseDetailBo.getCourseBySp(entry.getKey());
                for (CoursePeriod coursePeriod : coursePeriods) {
                    Integer day = WEEK_TO_INDEX.get(coursePeriod.getDay());
                    Integer beg = coursePeriod.getBeg();
                    Integer last = coursePeriod.getLast();
                    // 遍历周
                    for (int w = begWeek, wCount = 0; wCount < lastWeek; w++, wCount++) {
                        // 遍历当天
                        for (int i = beg, count = 0; count < last; i++, count++) {
                            tables[w][day][i] = course;
                        }
                    }
                }
                // 插入成功
                return true;
            }
        }
        // 插入失败
        return false;
    }

    /**
     * 检查是否有时间冲突
     *
     * @param begWeek
     * @param lastWeek
     * @param day
     * @param beg
     * @param last
     * @return
     */
    private boolean checkTimeValid(int begWeek, int lastWeek, String day, int beg, int last) {
        // 遍历上课周
        for (int week = begWeek, count = 0; count < lastWeek; count++, week++) {
            int d = WEEK_TO_INDEX.get(day);
            // 遍历天
            for (int begin = beg, dcount = 0; dcount < last; begin++, dcount++) {
                if (tables[week][d][begin] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("第").append(semesterId).append("学期\n");
        for (int i = 1; i <= WEEKS; i++) {
            sb.append("WEEK").append(i).append('\n');
            for (int j = 1; j <= TIME_PARTITION; j++) {
                for (int k = 1; k <= DAYS; k++) {
                    if (tables[i][k][j] != null) {
                        sb.append(tables[i][k][j].getCourseName()).append("\t\t\t\t");
                    } else {
                        sb.append("无\t\t\t\t");
                    }
                }
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public Map convertToViewMode() {
        Map result = new HashMap();
        String[][] names = new String[DAYS][TIME_PARTITION];
        for (int i = 1; i <= WEEKS; i++) {
            for (int j = 1; j <= DAYS; j++) {
                for (int k = 1; k <= TIME_PARTITION; k++) {
                    if (tables[i][j][k] != null) {
                        names[j - 1][k - 1] = tables[i][j][k].getCourseName();
                    } else {
                        names[j - 1][k - 1] = "";
                    }
                }
            }
            result.put("WEEK" + i, names);
        }
        return result;
    }

}
