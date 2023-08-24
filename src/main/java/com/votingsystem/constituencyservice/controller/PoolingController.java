package com.votingsystem.constituencyservice.controller;

import resources.constituency.PoolingPageResponseDTO;
import resources.constituency.PollingRequestDTO;
import com.votingsystem.constituencyservice.service.PoolingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import resources.ResponseDTO;
import resources.constituency.PoolingResponseDTO;


@RestController
@RequiredArgsConstructor
@RequestMapping("/polling")
public class PoolingController {

  private final PoolingService poolingService;

  /**
   * to create the polling
   *
   * @param pollingRequestDTO: contains the resources to create polling
   * @return : success response if created else return failure response
   */
  @PostMapping("")
  public ResponseDTO createPooling(@RequestBody PollingRequestDTO pollingRequestDTO) {
    return poolingService.createPooling(pollingRequestDTO);
  }

  /**
   * retrieve all the polling
   *
   * @param pageable: to give custom size to get pageable object
   * @return :object having polling pageable object else return null
   */
  @GetMapping("")
  public PoolingPageResponseDTO retrievePooling(Pageable pageable) {
    return poolingService.retrieveAllPolling(true, pageable);
  }

  /**
   * to retrieve started polling or the last polling
   *
   * @return : return the polling response else return null
   */
  @GetMapping("/status")
  public PoolingResponseDTO isPoolingStarted() {
    return poolingService.isPoolingStarted();
  }


  @GetMapping("/{pollingId}")
  public PoolingResponseDTO retrievePolling(@PathVariable long pollingId) {
    return poolingService.retrievePooling(pollingId);
  }

}