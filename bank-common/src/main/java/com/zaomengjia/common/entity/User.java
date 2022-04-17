//package com.zaomengjia.common.entity;
//
//
//import lombok.*;
//import org.hibernate.Hibernate;
//
//import javax.persistence.*;
//import java.util.Date;
//import java.util.Objects;
//
///**
// * 用户类，记录用户数据
// * */
//@Getter
//@Setter
//@ToString
//@RequiredArgsConstructor
//@Entity(name = "user")
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "uid")
//    private String uid;
//
//    /**
//     * 身份证号
//     */
//    @Column(name = "id_number")
//    private String idNumber;
//
//    @Column(name = "user_name")
//    private String userName;
//
//    @Column(name = "password")
//    private String password;
//
//    /**
//     * true为男，false为女
//     */
//    @Column(name = "sex")
//    private boolean sex;
//
//    @Column(name = "type")
//    private int type;
//
//    @Column(name = "birthday")
//    private Date birthday;
//
//    @Column(name = "has_job")
//    private boolean hasJob;
//
//    /**
//     * 记录次数
//     */
//    @Column(name = "overdue_record")
//    private int overdueRecord;
//
//    /**
//     * 是否为失信人
//     */
//    @Column(name = "is_discredit")
//    private boolean isDiscredit;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        User user = (User) o;
//        return uid != null && Objects.equals(uid, user.uid);
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }
//}
