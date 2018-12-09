/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.web.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 *
 * @author guoyo
 */
@StaticMetamodel(item.class)
public class item_ {

    public static volatile SingularAttribute<item, Long> id;
    public static volatile SingularAttribute<item, Long> superId;
    public static volatile SingularAttribute<item, String> name;
    public static volatile SingularAttribute<item, Boolean> isFolder;
    public static volatile SingularAttribute<item, Long> fileSize;
    public static volatile SingularAttribute<item, Long> ownerId;
    public static volatile SingularAttribute<item, String> path;
}
