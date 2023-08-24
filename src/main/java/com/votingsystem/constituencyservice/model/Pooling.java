package com.votingsystem.constituencyservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pooling {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long poolingId;

  private Date startDate;
  private Date endDate;

  @OneToMany(mappedBy = "pooling", fetch = FetchType.LAZY)
  private Set<Constituency> constituencies = new HashSet<>();

}