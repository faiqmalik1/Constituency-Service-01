package com.votingsystem.constituencyservice.feignController;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "VOTER-SERVICE")
public interface VoterController {

  /**
   * count all voter in constituency
   *
   * @param constituencyId: id of constituency
   * @return : count of voter
   */
  @GetMapping("voter/{constituencyId}/count")
  public long countAllVoterInConstituency(@PathVariable long constituencyId);
}
