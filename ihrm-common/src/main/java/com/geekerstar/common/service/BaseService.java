package com.geekerstar.common.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BaseService<T> {

    protected Specification<T> getSpec(String companyId) {
        Specification<T> spect = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                //根据企业id查询
                return cb.equal(root.get("companyId").as(String.class),companyId);
            }
        };
        return spect;
    }
}
