package br.com.fooddelivery.api.model.entry;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class KitchenIdEntry {
    @NotNull
    private Integer id;
}