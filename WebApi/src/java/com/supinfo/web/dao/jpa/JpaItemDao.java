package com.supinfo.web.dao.jpa;

import com.supinfo.web.dao.ItemDao;
import com.supinfo.web.entity.Customer;
import com.supinfo.web.entity.Customer_;
import com.supinfo.web.entity.item;
import com.supinfo.web.entity.item_;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author guoyo
 */
@Stateless
public class JpaItemDao implements ItemDao{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public item addNewItem(item i) {
        em.persist(i);
        return i;
    }

    @Override
    public List<item> getAllfilesById(Long superId, Long ownerId) {
        List<Predicate> predicates = new ArrayList<>();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<item> query = builder.createQuery(item.class);
        Root<item> item  = query.from(item.class);
        predicates.add(builder.equal(item.get(item_.superId),superId));
        predicates.add(builder.equal(item.get(item_.ownerId), ownerId));
        query.where(predicates.toArray(new Predicate[predicates.size()]));
        List<item> c = em.createQuery(query).getResultList();
        return c;
    }

    @Override
    public item getItemById(Long id) { 
        return em.find(item.class, id);
    }
}
