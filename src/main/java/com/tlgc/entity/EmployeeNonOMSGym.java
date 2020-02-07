package com.tlgc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.Date;

/**
 * Created by sang on 2018/7/16.
 */
@Data
public class EmployeeNonOMSGym {
    public interface AccountInfo{}
    public interface AccountDetail extends AccountInfo{}

    private String FirstName;
    private String LastName;
    @JsonView(AccountInfo.class)
    @JsonProperty("userName")
    private String UserName;
    private String FranchiseLicenseNumber;
    private Integer IsDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("createdDate")
    private Date CreatedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("lastModifiedDate")
    private Date LastModifiedDate;
    private String Pin;
    // 如果方法上还有 JsonView(UserDetail.class) 则 给前端展示 这个属性
    @JsonView(AccountDetail.class)
    public String getPin() {
        return Pin;
    }



}
