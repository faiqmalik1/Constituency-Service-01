package com.votingsystem.constituencyservice.resources;

import com.votingsystem.constituencyservice.model.Constituency;
import com.votingsystem.constituencyservice.model.Pooling;
import org.springframework.data.domain.Page;
import resources.constituency.ConstituencyResponseDTO;
import resources.constituency.PoolingPageResponseDTO;
import resources.constituency.PoolingResponseDTO;

public class ModelToResponse {

  public static ConstituencyResponseDTO parseConstituencyToResponse(Constituency constituency, long voterCount) {
    ConstituencyResponseDTO constituencyResponseDTO = new ConstituencyResponseDTO();
    constituencyResponseDTO.setConstituencyId(constituency.getConstituencyId());
    constituencyResponseDTO.setHalkaName(constituency.getHalkaName());
    constituencyResponseDTO.setVoterCount(voterCount);
    return constituencyResponseDTO;
  }

  public static PoolingResponseDTO parsePoolingToPoolingResponse(Pooling pooling) {
    PoolingResponseDTO response = new PoolingResponseDTO();
    response.setPoolingId(pooling.getPoolingId());
    response.setStartDate(pooling.getStartDate().toString());
    response.setEndDate(pooling.getEndDate().toString());
    return response;
  }

  public static PoolingPageResponseDTO parsePoolingPageToPoolingResponse(Page<Pooling> poolingPage) {
    PoolingPageResponseDTO poolingPageResponseDTO = new PoolingPageResponseDTO();
    Page<PoolingResponseDTO> poolingResponsePage = poolingPage.map(ModelToResponse::parsePoolingToPoolingResponse);
    poolingPageResponseDTO.setPoolingResponsePage(poolingResponsePage);
    return poolingPageResponseDTO;
  }


}