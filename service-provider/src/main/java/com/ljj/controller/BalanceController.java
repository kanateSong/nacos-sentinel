package com.ljj.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ljj.domain.Balance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Author 李佳杰
 * @Date 2019/12/21
 * @Version V1.0
 **/
@RestController
@RefreshScope
public class BalanceController {

    @Value("${sleep:0}")
    private int sleep;

    final static Map<Integer, Balance> balanceMap = new HashMap() {
        {
            put(1, new Balance(1, 10, 1000));
            put(2, new Balance(2, 0, 10000));
            put(3, new Balance(3, 100, 0));
        }
    };

    @RequestMapping("/service/balance")
    @SentinelResource(value = "protected-resource", blockHandler = "handleBlock")
    public Balance getBalance(Integer id) {
        System.out.println("request: /service/balance?id=" + id + ", sleep: " + sleep);
        if (sleep > 0) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (id != null && balanceMap.containsKey(id)) {
            return balanceMap.get(id);
        }
        return new Balance(0, 0, 0, "不存在");
    }

    @RequestMapping("/service2/balance")
    @SentinelResource(value = "protected-resource", blockHandler = "handleBlock2")
    public Balance getBalance2(Integer id) {
        System.out.println("request: /service2/balance?id=" + id + ", sleep: " + sleep);
        if (sleep > 0) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (id != null && balanceMap.containsKey(id)) {
            return balanceMap.get(id);
        }
        return new Balance(0, 0, 0, "不存在");
    }

    public Balance handleBlock(Integer id, BlockException e) {
        return new Balance(0, 0, 0, "限流");
    }

    public Balance handleBlock2(Integer id, BlockException e) {
        return new Balance(0, 0, 0, "限流2");
    }
}
