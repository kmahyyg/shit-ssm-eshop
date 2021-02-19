package xyz.kmahyyg.eshopdemo.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.kmahyyg.eshopdemo.model.SysUserCart;


/**
 * @Entity xyz.kmahyyg.eshopdemo.model.SysUserCart
 */
@Mapper
public interface SysUserCartDao {
    int deleteByUserId(String uid);

    int insert(SysUserCart record);

    int updateByUserId(SysUserCart record);

    SysUserCart selectByUserId(String uid);
}
