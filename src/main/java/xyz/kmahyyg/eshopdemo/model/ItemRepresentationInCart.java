package xyz.kmahyyg.eshopdemo.model;

import lombok.Data;
import xyz.kmahyyg.eshopdemo.model.SysItems;

@Data
public class ItemRepresentationInCart {
    private SysItems currentItem;
    private int itemNum;

    public ItemRepresentationInCart(SysItems curItem, int itemNum){
        this.currentItem = curItem;
        this.itemNum = itemNum;
    }
}
