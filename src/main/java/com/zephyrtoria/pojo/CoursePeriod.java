package com.zephyrtoria.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName course_period
 */
@TableName(value ="course_period")
@Data
public class CoursePeriod implements Serializable {
    private Integer id;

    private String courseBasicId;

    private String courseSpId;

    private String day;

    private Integer beg;

    private Integer last;

    private static final long serialVersionUID = 1L;
}