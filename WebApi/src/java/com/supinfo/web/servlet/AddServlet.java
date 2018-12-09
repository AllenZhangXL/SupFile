/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.web.servlet;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.emptyType;
import com.supinfo.web.dao.UserDao;
import com.supinfo.web.dao.jpa.JpaUserDao;
import com.supinfo.web.entity.Customer;
import java.io.IOException;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

/**
 *
 * @author guoyo
 */
@WebServlet(urlPatterns = "/add")
public class AddServlet extends HttpServlet{

    @EJB
    UserDao userDao;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Customer u = new Customer();
        u.setEmail("201092@supinfo.com");
        u.setPassword("test");
        userDao.register(u);
        System.out.println("registerrrrrrrrrrrrrrrrrrrrrrrr"+u.getEmail());
    }
    
}
