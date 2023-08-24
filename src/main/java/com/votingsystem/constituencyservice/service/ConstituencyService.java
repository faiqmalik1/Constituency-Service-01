package com.votingsystem.constituencyservice.service;

import CustomException.CommonException;
import com.votingsystem.constituencyservice.feignController.VoterController;
import com.votingsystem.constituencyservice.model.Constituency;
import com.votingsystem.constituencyservice.model.Pooling;
import com.votingsystem.constituencyservice.repository.ConstituencyRepository;
import com.votingsystem.constituencyservice.repository.PoolingRepository;
import constants.ReturnMessage;
import org.springframework.http.HttpStatus;
import resources.ResponseDTO;
import resources.constituency.ConstituencyListResponseDTO;
import resources.constituency.ConstituencyRequestDTO;
import com.votingsystem.constituencyservice.resources.ModelToResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resources.BaseService;
import resources.constituency.ConstituencyResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConstituencyService extends BaseService {

  private final ConstituencyRepository constituencyRepository;
  private final PoolingRepository poolingRepository;
  private final VoterController voterController;

  /**
   * to create new constituency
   *
   * @param constituencyRequestDTO:contains the resources to create a new constituency
   * @return :return success response if everything works fine else throw error
   */
  @Transactional
  public ResponseDTO createConstituency(ConstituencyRequestDTO constituencyRequestDTO) {
    Optional<Constituency> optionalConstituency = constituencyRepository.findByHalkaName(constituencyRequestDTO.getHalkaName());
    if (optionalConstituency.isPresent()) {
      throw new CommonException(ReturnMessage.CONSTITUENCY_ALREADY_EXISTS.getValue(), HttpStatus.BAD_REQUEST);
    }
    Constituency constituency = new Constituency();
    constituency.setHalkaName(constituencyRequestDTO.getHalkaName());
    if (constituencyRequestDTO.getPoolingId() != 0) {
      Optional<Pooling> optionalPooling = poolingRepository.findById(constituencyRequestDTO.getPoolingId());
      if (optionalPooling.isEmpty()) {
        throw new CommonException(ReturnMessage.INVALID_POLLING_ID.getValue(), HttpStatus.BAD_REQUEST);
      }
      Pooling verifiedPooling = optionalPooling.get();
      if (verifiedPooling.getEndDate().before(new Date())) {
        throw new CommonException(ReturnMessage.POLLING_ALREADY_ENDED.getValue(), HttpStatus.BAD_REQUEST);
      }
      constituency.setPooling(verifiedPooling);
    }
    constituency.setCreatedAt(new Date());
    constituencyRepository.save(constituency);
    return generateSuccessResponse();
  }

  /**
   * to retrieve constituency by id
   *
   * @param constituencyId :id of constituency
   * @return :return constituency if found else return null
   */
  public ConstituencyResponseDTO retrieveConstituency(long constituencyId) {
    Optional<Constituency> optionalConstituency = constituencyRepository.findById(constituencyId);
    return optionalConstituency.map(constituency -> ModelToResponse.parseConstituencyToResponse(constituency, 0)).orElse(null);
  }

  /**
   * to retrieve constituency by name
   *
   * @param halkaName: name of constituency
   * @return :return constituency if found else return null
   */
  public ConstituencyResponseDTO retrieveConstituency(String halkaName) {
    Optional<Constituency> optionalConstituency = constituencyRepository.findByHalkaName(halkaName);
    return optionalConstituency.map(constituency -> {
      return ModelToResponse.parseConstituencyToResponse(constituency, 0);
    }).orElse(null);
  }

  /**
   * to retrieve all the constituencies
   *
   * @return :return the list of available constituencies if found else return null
   */
  public ConstituencyListResponseDTO retrieveConstituencies() {
    List<Constituency> constituencyList = constituencyRepository.findAll();
    if (constituencyList.isEmpty()) {
      return null;
    }
    List<ConstituencyResponseDTO> constituencyResponseDTOList = constituencyList.stream().map(constituency -> {
      long voterCount = voterController.countAllVoterInConstituency(constituency.getConstituencyId());
      return ModelToResponse.parseConstituencyToResponse(constituency, voterCount);
    }).toList();
    return new ConstituencyListResponseDTO(constituencyResponseDTOList);
  }
}