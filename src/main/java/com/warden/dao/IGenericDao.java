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

import java.util.List;

/**
 * AGenericDao - Provide generic interface methods for 
 * performing CRUD operations on any type DAO object.
 *
 * @author odhran
 *
 */
public interface IGenericDao<T> {

    /**
     * 
     * Create a new Entity in the database
     *
     * @param Entity type
     */
    void create(T t);
    
    /**
     * 
     * update Entity in database
     *
     * @param Entity type
     */
    void update(T t);
    
    /**
     * 
     * Find Entity by its id
     *
     * @param id
     * @return
     */
    T findById(long id);

    
    /**
     * Find all the Entity type objects in the system.
     *
     * @return
     */
    List<T> findAll();

    /**
     * delete the entity type from the system.
     *
     * @param object
     */
    void delete(T t);
    
    /**
     * delete the entity type from the system.
     *
     * @param id of the entity
     */
    void delete(Long id);
    
    /**
     *  Do an explicit flush();
     */
    void flush();

}