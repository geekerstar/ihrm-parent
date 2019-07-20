package com.geekerstar.system.client;

import com.geekerstar.common.entity.Result;
import com.geekerstar.domain.company.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 声明接口，通过feign调用其他微服务
 */
//声明调用的微服务名称
@FeignClient("ihrm-company")
public interface DepartmentFeignClient {

    /**
     * 调用微服务的接口
     */
    @RequestMapping(value="/company/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id);

    @RequestMapping(value="/company/department/search",method = RequestMethod.POST)
    public Department findByCode(@RequestParam(value = "code") String code, @RequestParam(value = "companyId") String companyId);
}
