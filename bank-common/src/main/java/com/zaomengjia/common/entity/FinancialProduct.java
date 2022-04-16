package com.zaomengjia.common.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 理财产品类，记录银行所有理财产品
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "financial_product")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@SQLDelete(sql = "update `financial_product` set `deleted`= 1 where `id` = ?")
@Where(clause = "`deleted` = 0")
@EnableJpaAuditing
public class FinancialProduct {

    /**
     * 银行所有理财产品的id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    /**
     * 理财产品名
     */
    @Column(name = "name")
    private String name;

    /**
     * 理财产品价格
     */
    @Column(name = "price")
    private int price;

    /**
     * 逻辑删除
     */
    @Column(name = "deleted")
    private int deleted;

    /**
     * 乐观锁
     */
    @Column(name = "version")
    @Version
    private int version;


    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FinancialProduct that = (FinancialProduct) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
