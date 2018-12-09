/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.web.dao;

import com.supinfo.web.entity.item;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author guoyo
 */
@Local
public interface ItemDao {
    item addNewItem(item i);
    List<item> getAllfilesById(Long superId,Long ownerId);
    item getItemById(Long id);
}
