package xyz.kmahyyg.eshopdemo.model;

import lombok.Data;
import xyz.kmahyyg.eshopdemo.enums.OrderStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * null
 *
 * @TableName sys_orders
 */
@Data
public class SysOrders implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String oid;

    private OrderStatusEnum status;

    private String items;

    private String uid;

    private BigDecimal finalPrice;

    private Long genTime;

    private Long paidTime;

    private Long doneTime;

    private Long refundTime;

    private Integer paymentId;

    private Integer deliveryId;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(500);
        sb.append("订单ID: ").append(this.oid);
        sb.append("\n 订单状态： ").append(this.status.toString());
        sb.append("\n 订单条目： ").append(this.items);
        sb.append("\n 订单总价： ").append(this.finalPrice.toString());
        sb.append("\n 订单关联用户ID： ").append(this.uid);
        sb.append("\n 订单创建时间： ").append(this.genTime.toString());
        sb.append("\n 订单支付时间： ").append(String.valueOf(this.paidTime));
        sb.append("\n 订单完成时间： ").append(String.valueOf(this.doneTime));
        sb.append("\n 订单支付方式： ").append(this.paymentId.toString());
        sb.append("\n 订单物流方式： ").append(this.deliveryId.toString()).append("\n\n\n");
        return sb.toString();
    }

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
        SysOrders other = (SysOrders) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getOid() == null ? other.getOid() == null : this.getOid().equals(other.getOid()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getItems() == null ? other.getItems() == null : this.getItems().equals(other.getItems()))
                && (this.getUid() == null ? other.getUid() == null : this.getUid().equals(other.getUid()))
                && (this.getFinalPrice() == null ? other.getFinalPrice() == null : this.getFinalPrice().equals(other.getFinalPrice()))
                && (this.getGenTime() == null ? other.getGenTime() == null : this.getGenTime().equals(other.getGenTime()))
                && (this.getPaidTime() == null ? other.getPaidTime() == null : this.getPaidTime().equals(other.getPaidTime()))
                && (this.getDoneTime() == null ? other.getDoneTime() == null : this.getDoneTime().equals(other.getDoneTime()))
                && (this.getRefundTime() == null ? other.getRefundTime() == null : this.getRefundTime().equals(other.getRefundTime()))
                && (this.getPaymentId() == null ? other.getPaymentId() == null : this.getPaymentId().equals(other.getPaymentId()))
                && (this.getDeliveryId() == null ? other.getDeliveryId() == null : this.getDeliveryId().equals(other.getDeliveryId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOid() == null) ? 0 : getOid().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getItems() == null) ? 0 : getItems().hashCode());
        result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
        result = prime * result + ((getFinalPrice() == null) ? 0 : getFinalPrice().hashCode());
        result = prime * result + ((getGenTime() == null) ? 0 : getGenTime().hashCode());
        result = prime * result + ((getPaidTime() == null) ? 0 : getPaidTime().hashCode());
        result = prime * result + ((getDoneTime() == null) ? 0 : getDoneTime().hashCode());
        result = prime * result + ((getRefundTime() == null) ? 0 : getRefundTime().hashCode());
        result = prime * result + ((getPaymentId() == null) ? 0 : getPaymentId().hashCode());
        result = prime * result + ((getDeliveryId() == null) ? 0 : getDeliveryId().hashCode());
        return result;
    }

}