package com.votingsystem.constituencyservice.feignController;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import resources.user.UserResponseDTO;
import resources.user.ValidateResponseDTO;


@FeignClient(name = "USER-SERVICE")
public interface UserController {

  /**
   * function to validate the user token
   *
   * @param token: token to be validated containing user id
   * @return :return the user response containing the role, name and id of user if user found else return null
   */
  @GetMapping(value = "/users/validate")
  public ValidateResponseDTO validateToken(@CookieValue("Authorization") String token);
}