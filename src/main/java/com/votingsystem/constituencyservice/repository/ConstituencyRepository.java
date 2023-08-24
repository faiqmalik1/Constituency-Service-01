package com.votingsystem.constituencyservice.repository;

import com.votingsystem.constituencyservice.model.Constituency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConstituencyRepository extends JpaRepository<Constituency, Long> {

  Optional<Constituency> findByHalkaName(String halkaName);

}