package com.zephyrtoria.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName prereq
 */
@TableName(value ="prereq")
@Data
public class Prereq implements Serializable {
    private Integer id;

    private String courseBasicId;

    private String prereqId;

    private static final long serialVersionUID = 1L;
}