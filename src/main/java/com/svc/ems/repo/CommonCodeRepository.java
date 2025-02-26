package com.svc.ems.repo;

import com.svc.ems.entity.CommonCodeEntity;
import com.svc.ems.entity.CommonCodePkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author 郭庭安
 * @Create 2024/7/27 10:17 PM
 * @Version 1.0
 */
@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCodeEntity, CommonCodePkEntity> {

    // 根據 codeTypes 查詢，並處理當 codeTypes 為空的情況
    List<CommonCodeEntity> findByCodeType(String codeType);

}
