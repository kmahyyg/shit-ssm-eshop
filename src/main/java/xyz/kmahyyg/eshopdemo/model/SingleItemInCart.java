package xyz.kmahyyg.eshopdemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SingleItemInCart {
    @JsonProperty("itemId")
    private int itemId;

    @JsonProperty("itemNum")
    private int itemNum;
}
