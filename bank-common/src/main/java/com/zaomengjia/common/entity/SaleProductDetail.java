package com.zaomengjia.common.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.Objects;


/**
 * 秒杀活动的商品明细类，包括商品名，商品数量,属于哪一个秒杀活动
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@Entity(name = "sale_product_detail")
@EnableJpaAuditing
public class SaleProductDetail {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id")
    private String id;

    /**
     * 隶属于哪一个秒杀活动
     */
    @Column(name = "seckill_activity_id")
    private String seckillActivityId;

    /**
     * 理财的产品
     */
    @Column(name = "financial_product_id")
    private String financialProductId;

    /**
     * 活动抢购数量
     */
    @Column(name = "quantity")
    private long quantity;

    /**
     * 总数
     */
    @Column(name = "total")
    private int total;

    /**
     * 乐观锁
     */
    @Column(name = "version")
    @Version
    private int version;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SaleProductDetail that = (SaleProductDetail) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
