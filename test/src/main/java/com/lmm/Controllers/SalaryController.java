package com.lmm.Controllers;

import com.lmm.beans.AutoWired;
import com.lmm.service.SalaryService;
import com.lmm.web.mvc.Controller;
import com.lmm.web.mvc.RequestMapping;
import com.lmm.web.mvc.RequestParam;

/**
 * 版权声明：Copyright(c) 2019
 *
 * @program: mini-spring
 * @Author myFlowerYourGrass
 * @Date 2019-09-19 15:51
 * @Version 1.0
 * @Description
 */
@Controller
public class SalaryController {
    @AutoWired
    private SalaryService salaryService;

    @RequestMapping("/getSalary.json")
    public Integer getSalary(@RequestParam("name") String name,
                             @RequestParam("experience") String experience) {
        return salaryService.calSalary(Integer.parseInt(experience));
    }
}
