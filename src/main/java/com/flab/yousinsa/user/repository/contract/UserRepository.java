package com.flab.yousinsa.user.repository.contract;

import com.flab.yousinsa.user.domain.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUserEmail(String userEmail);
}
