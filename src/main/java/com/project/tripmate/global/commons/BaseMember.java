package com.project.tripmate.global.commons;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;


@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditorAwareImpl.class)
public class BaseMember extends Base{

    @CreatedBy
    @Column(length = 50 , updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(length = 50 , insertable = false)
    private String modifiedBy;



}
