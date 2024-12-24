package com.zephyrtoria.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName succeed
 */
@TableName(value ="succeed")
@Data
public class Succeed implements Serializable {
    private Integer id;

    private String courseBasicId;

    private String succeedCourseBasicId;

    private static final long serialVersionUID = 1L;
}