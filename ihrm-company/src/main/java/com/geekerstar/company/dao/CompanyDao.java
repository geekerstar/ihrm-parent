package com.geekerstar.company.dao;

import com.ihrm.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author geekerstar
 * date: 2019-07-18 20:16
 * description:
 *
 * 自定义Dao接口继承
 *      JpaRepository<实体类，主键>
 *      JpaSpecificationExecutor <实体类>
 *
 */
public interface CompanyDao extends JpaRepository<Company,String>, JpaSpecificationExecutor<Company> {

}
