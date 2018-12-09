package com.supinfo.web.dao.jpa;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.emptyType;
import com.supinfo.web.dao.UserDao;
import com.supinfo.web.entity.Customer;
import com.supinfo.web.entity.Customer_;
import com.supinfo.web.entity.item;
import com.supinfo.web.entity.item_;
import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author guoyo
 */
@Stateless
public class JpaUserDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Customer login(String emailString, String password) {
        String passwd;
        Customer u = getCustomerByEmail(emailString);
        //Customer e = getCustomerByEmail(username);
        System.out.println("email:::::::::::::"+u.getEmail());
        if (u != null) {
            passwd = u.getPassword();
            if (passwd.equals(password)) {
                return u;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Customer register(Customer user) {
        em.persist(user);
        return user;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
        Root<Customer> item  = query.from(Customer.class);
        predicates.add(builder.equal(item.get(Customer_.email),email));
        query.where(predicates.toArray(new javax.persistence.criteria.Predicate[predicates.size()]));
        Customer c = em.createQuery(query).getSingleResult();
        return c;
    }

    @Override
    public Customer getCustomerById(Long id) {
        return em.find(Customer.class, id);
    }

    @Override
    public Customer updateUser(Customer c) {
        em.merge(c);
        return c;
    }
}
