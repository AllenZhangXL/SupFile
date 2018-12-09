package com.supinfo.web.entity;


import com.supinfo.web.entity.Customer;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(Customer.class)
public class Customer_ {
    public static volatile SingularAttribute<Customer, Long> id;
    public static volatile SingularAttribute<Customer, String> email;
    public static volatile SingularAttribute<Customer, String> password;
    public static volatile SingularAttribute<Customer, Long> availableSpace;
}
