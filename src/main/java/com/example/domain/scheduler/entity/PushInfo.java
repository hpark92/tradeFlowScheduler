package com.example.domain.scheduler.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Table(name="TB_PUSH_INFO")
@ToString
public class PushInfo {
    @Id @GeneratedValue
    private int pushId;

    @Column
    private String tokenId;

    @Column
    private Date startDrive;

    @Column
    private String osType;

   public PushInfo() {}

}
