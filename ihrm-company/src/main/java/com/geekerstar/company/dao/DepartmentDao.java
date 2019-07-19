package com.geekerstar.company.dao;

import com.geekerstar.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author geekerstar
 * date: 2019-07-19 11:17
 * description: 部门Dao接口
 */
public interface DepartmentDao extends JpaRepository<Department,String>, JpaSpecificationExecutor<Department> {
}
