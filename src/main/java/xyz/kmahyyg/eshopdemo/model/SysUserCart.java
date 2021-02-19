package xyz.kmahyyg.eshopdemo.model;

import lombok.Data;

import java.io.Serializable;

/**
 * null
 *
 * @TableName sys_user_cart
 */
@Data
public class SysUserCart implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String uid;

    private SingleUserCart items;


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysUserCart other = (SysUserCart) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUid() == null ? other.getUid() == null : this.getUid().equals(other.getUid()))
                && (this.getItems() == null ? other.getItems() == null : this.getItems().equals(other.getItems()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
        result = prime * result + ((getItems() == null) ? 0 : getItems().hashCode());
        return result;
    }

}