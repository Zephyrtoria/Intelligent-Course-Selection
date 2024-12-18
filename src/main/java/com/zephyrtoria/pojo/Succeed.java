package com.zephyrtoria.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

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