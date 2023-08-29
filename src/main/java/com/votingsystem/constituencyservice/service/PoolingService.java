package com.votingsystem.constituencyservice.service;

import CustomException.CommonException;
import com.votingsystem.constituencyservice.model.Pooling;
import com.votingsystem.constituencyservice.repository.ConstituencyRepository;
import com.votingsystem.constituencyservice.repository.PoolingRepository;
import com.votingsystem.constituencyservice.resources.ModelToResponse;
import constants.ReturnMessage;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import resources.ResponseDTO;
import resources.constituency.PoolingPageResponseDTO;
import resources.constituency.PollingRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import resources.BaseService;
import resources.constituency.PoolingResponseDTO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PoolingService extends BaseService {

  private final PoolingRepository poolingRepository;
  private final ConstituencyRepository constituencyRepository;

  /**
   * to create the polling
   *
   * @param pollingRequestDTO: contains the resources to create polling
   * @return : success response if created else return failure response
   */
  @Transactional
  public ResponseDTO createPooling(PollingRequestDTO pollingRequestDTO) {
    try {
      if (pollingRequestDTO.getEndDate().before(new Date())) {
        return generateFailureResponse(ReturnMessage.POLLING_END_DATE_IN_PAST.getValue());
      }
      List<Pooling> pollingList = poolingRepository.findAll();
      for (Pooling p : pollingList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        LocalDateTime parsedStartDate = LocalDateTime.parse(pollingRequestDTO.getStartDate().toString(), formatter);

        LocalDateTime localEndDateTime = p.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        boolean parsedStartDateIsBeforeLocalEnd = parsedStartDate.isBefore(localEndDateTime);

        if (parsedStartDateIsBeforeLocalEnd) {
          return generateFailureResponse(ReturnMessage.POLLING_END_DATE_IN_PAST.getValue());
        }

      }
      Pooling pooling = new Pooling();
      pooling.setEndDate(pollingRequestDTO.getEndDate());
      pooling.setStartDate(pollingRequestDTO.getStartDate());
      poolingRepository.save(pooling);
      return generateSuccessResponse();
    } catch (Exception ex) {
      throw new CommonException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * retrieve all the polling
   *
   * @param available: status of polling to retrieve
   * @param pageable:  to give custom size to get pageable object
   * @return :object having polling pageable object else return null
   */
  public PoolingPageResponseDTO retrieveAllPolling(boolean available, Pageable pageable) {
    Page<Pooling> poolingPage = poolingRepository.findAll(pageable);
    return ModelToResponse.parsePoolingPageToPoolingResponse(poolingPage);
  }

  /**
   * to retrieve started polling or the last polling
   *
   * @return : return the polling response else return null
   */
  public PoolingResponseDTO isPoolingStarted() {
    Optional<Pooling> optionalPooling = poolingRepository.findActivePooling(new Date());
    if (optionalPooling.isPresent()) {
      return ModelToResponse.parsePoolingToPoolingResponse(optionalPooling.get());
    }
    List<Pooling> pollingList = poolingRepository.findLastPollingWithEndDateInPast();
    List<Pooling> sortedPollingList = pollingList.stream()
            .sorted(Comparator.comparing(Pooling::getEndDate).reversed())
            .toList();
    if (sortedPollingList.isEmpty()) {
      return null;
    }
    return ModelToResponse.parsePoolingToPoolingResponse(sortedPollingList.get(0));
  }

  public PoolingResponseDTO retrievePooling(long pollingId) {
    Optional<Pooling> optionalPooling = poolingRepository.findById(pollingId);
    if (optionalPooling.isEmpty()) {
      throw new CommonException(ReturnMessage.POLLING_NOT_STARTED.getValue(), HttpStatus.BAD_REQUEST);
    }
    return ModelToResponse.parsePoolingToPoolingResponse(optionalPooling.get());
  }
}