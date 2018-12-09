/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.web.dao;

import com.supinfo.web.entity.Customer;
import javax.ejb.Local;
import javax.ejb.Remote;
import jdk.nashorn.internal.ir.annotations.Reference;

/**
 *
 * @author guoyo
 */
@Local
public interface UserDao {
    Customer login(String emailString,String password);
    Customer register(Customer user);
    Customer getCustomerByEmail(String email);
    Customer getCustomerById(Long id);
    Customer updateUser(Customer c);
}
