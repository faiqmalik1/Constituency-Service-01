package com.votingsystem.constituencyservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Constituency {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long constituencyId;
  private String halkaName;
  private Date createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private Pooling pooling;
}