/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * 提供简单的非泛型接口，用于直接执行原生SQL语句或者JPQL语句进行操作。
 *
 * @author XINEN
 */
@NoRepositoryBean
public interface SimpleRepository extends QueryRepository, ModifingRepository {

}
