package com.votingsystem.constituencyservice.controller;

import resources.constituency.ConstituencyRequestDTO;
import com.votingsystem.constituencyservice.service.ConstituencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import resources.ResponseDTO;
import resources.constituency.ConstituencyListResponseDTO;
import resources.constituency.ConstituencyResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/constituency")
public class ConstituencyController {

  private final ConstituencyService constituencyService;

  /**
   * to create new constituency
   *
   * @param constituencyRequestDTO:contains the resources to create a new constituency
   * @return :return success response if everything works fine else throw error
   */
  @PostMapping("")
  public ResponseDTO createConstituency(@RequestBody ConstituencyRequestDTO constituencyRequestDTO) {
    return constituencyService.createConstituency(constituencyRequestDTO);
  }

  /**
   * to retrieve constituency by id
   *
   * @param constituencyId:id of constituency
   * @return :return constituency if found else return null
   */
  @GetMapping("/id/{constituencyId}")
  public ConstituencyResponseDTO retrieveConstituencyById(@PathVariable(name = "constituencyId") long constituencyId) {
    return constituencyService.retrieveConstituency(constituencyId);
  }

  /**
   * to retrieve constituency by name
   *
   * @param halkaName: name of constituency
   * @return :return constituency if found else return null
   */
  @GetMapping("/name/{halkaName}")
  public ConstituencyResponseDTO retrieveConstituencyByName(@PathVariable(name = "halkaName") String halkaName) {
    return constituencyService.retrieveConstituency(halkaName);
  }

  /**
   * to retrieve all the constituencies
   *
   * @return :return the list of available constituencies if found else return null
   */
  @GetMapping("")
  public ConstituencyListResponseDTO retrieveConstituencies() {
    return constituencyService.retrieveConstituencies();
  }

  @PostMapping("/create")
  public ConstituencyResponseDTO createConstituency() {
    return constituencyService.createConstituency();
  }

}
