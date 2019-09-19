package com.lmm.service;

import com.lmm.beans.Bean;

/**
 * 版权声明：Copyright(c) 2019
 *
 * @program: mini-spring
 * @Author myFlowerYourGrass
 * @Date 2019-09-19 17:38
 * @Version 1.0
 * @Description
 */
@Bean
public class SalaryService {
    public Integer calSalary(Integer experience){
        return experience*100;
    }
}
