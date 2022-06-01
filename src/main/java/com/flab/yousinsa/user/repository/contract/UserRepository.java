package com.flab.yousinsa.user.repository.contract;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.flab.yousinsa.user.domain.entities.UserEntity;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUserEmail(String userEmail);
}
