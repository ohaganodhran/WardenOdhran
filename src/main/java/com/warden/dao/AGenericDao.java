/**
 * Copyright 2013 Okotech Ltd. 
 * Registered address: 2 Skerry Dhu,Newtowncrommelin,Ballymena,Co.Antrim 
 * All Rights Reserved.
 * 
 * This software, including all software code and architecture, is the confidential and 
 * proprietary information of Okotech Ltd ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with Okotech Ltd.
 */
package com.warden.dao;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Implementation of abstract generic DAO
 * 
 * @author gary
 */
public abstract class AGenericDao<T> implements IGenericDao<T> {

    @PersistenceContext
    protected EntityManager em;

    protected Class<T> entityClass;

    public AGenericDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }


    @Transactional
    public void create(T t) {
        this.em.persist(t);
    }

    @Transactional
    public void update(T t) {
        this.em.merge(t);
    }

    public T findById(long id) {
        return this.em.find(entityClass, id);
    }

    public List<T> findAll() {
        final StringBuffer queryString = new StringBuffer("FROM ");
        queryString.append(entityClass.getSimpleName());

        final Query query = this.em.createQuery(queryString.toString());
        return (List<T>) query.getResultList();
    }

    @Transactional
    public void delete(T t) {
        this.em.remove(this.em.merge(t));
    }

    @Transactional
    public void delete(Long id) {
       this.delete(this.em.find(entityClass, id));
    }

    public void flush() {
        this.em.flush();
    }

}