package br.com.fooddelivery.core.jackson;

import br.com.fooddelivery.api.model.CityMixin;
import br.com.fooddelivery.api.model.KitchenMixin;
import br.com.fooddelivery.api.model.RestaurantMixin;
import br.com.fooddelivery.domain.model.City;
import br.com.fooddelivery.domain.model.Kitchen;
import br.com.fooddelivery.domain.model.Restaurant;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class JacksonMixinModule extends SimpleModule {
    private static final long serialVersionUID = 1L;

    public JacksonMixinModule() {
        this.setMixInAnnotation(Restaurant.class, RestaurantMixin.class);
        this.setMixInAnnotation(Kitchen.class, KitchenMixin.class);
        this.setMixInAnnotation(City.class, CityMixin.class);
    }
}