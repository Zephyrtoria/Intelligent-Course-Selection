package com.zephyrtoria.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

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