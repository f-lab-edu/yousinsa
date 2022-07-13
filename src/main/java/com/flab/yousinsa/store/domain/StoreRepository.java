package com.flab.yousinsa.store.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flab.yousinsa.user.domain.entities.UserEntity;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	boolean existsByStoreOwner(UserEntity user);
}
