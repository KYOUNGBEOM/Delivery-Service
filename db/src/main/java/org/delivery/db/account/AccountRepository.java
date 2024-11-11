package org.delivery.db.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Override
    Optional<AccountEntity> findById(Long aLong);
}
