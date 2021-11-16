package com.tqk.dubbo.service;

/**
 *@author tianqikai
 *类说明：变动库存服务接口
 */
public interface StockService {
    /**
     * 增加库存
     * @param goodsId
     * @param addAmout
     */
    void addStock(String goodsId, int addAmout);

    /**
     * 扣减库存
     * @param goodsId
     * @param deduceAmout
     */
    void deduceStock(String goodsId, int deduceAmout);
}
