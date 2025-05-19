package com.violet6bee.otp_rest_api.repository;

import com.violet6bee.otp_rest_api.entity.OtpSettingsEntity;
import com.violet6bee.otp_rest_api.entity.OtpTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpTableEntity, Long> {
}
