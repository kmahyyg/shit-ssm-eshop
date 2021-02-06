package xyz.kmahyyg.eshopdemo.dao;

import xyz.kmahyyg.eshopdemo.model.SysUserCart;
import org.apache.ibatis.annotations.Mapper;

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