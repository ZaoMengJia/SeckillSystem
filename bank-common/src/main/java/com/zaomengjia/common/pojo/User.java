package com.zaomengjia.common.pojo;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户类，记录用户数据
 * */
@Data
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private long uid;


    /**
     * 身份证号
     */
    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    /**
     * true为男，false为女
     */
    @Column(name = "sex")
    private boolean sex;

    @Column(name = "type")
    private int type;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "has_job")
    private boolean hasJob;

    /**
     * 记录次数
     */
    @Column(name = "overdue_record")
    private int overdueRecord;

    /**
     * 是否为失信人
     */
    @Column(name = "is_discredit")
    private boolean isDiscredit;
}
