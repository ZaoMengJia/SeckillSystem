package com.zaomengjia.common.vo.page;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/8 00:28
 */
@Data
public class PageVO<T> {
    private List<T> data;
    private int pageNum;
    private int pageTotal;

    public PageVO(Page<T> page) {
        data = page.toList();
        pageNum = page.getNumber();
        pageTotal = page.getTotalPages();
    }
}
