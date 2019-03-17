package com.dangdangnet.goods.service;

import com.dangdangnet.goods.entity.Order;

public interface FlowService {
    public void insertAll(Order[] orders, String username, String userid);

    public void updateGoods(Order[] orders);

    public void delGoods(Order[] orders);
}
