package br.com.fooddelivery.domain.repository;

import br.com.fooddelivery.domain.model.Kitchen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenRepository extends JpaRepository<Kitchen, Integer> {
    Kitchen findByName(String name);
}
