package xyz.kmahyyg.eshopdemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SingleUserCart {
     @JsonProperty("cart")
     private List<SingleItemInCart> cart;
}
