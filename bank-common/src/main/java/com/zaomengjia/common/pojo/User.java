package com.zaomengjia.common.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户类，记录用户数据
 * */
@Data
@TableName("user")
public class User {
    @TableId(value = "uid",type = IdType.AUTO)
    private long uid;
    @TableField("id_number")
    private String idNumber;//身份证号
    @TableField("user_name")
    private String userName;
    @TableField("password")
    private String password;
    @TableField("sex")
    private boolean sex;//true为男，false为女
    @TableField("is_admin")
    private boolean isAdmin;
    @TableField("birthday")
    private Date birthday;
    @TableField("has_job")
    private boolean hasJob;
    @TableField("overdue_record")
    private int overdueRecord;//记录次数
    @TableField("is_discredit")
    private boolean isDiscredit;//是否为失信人
}
