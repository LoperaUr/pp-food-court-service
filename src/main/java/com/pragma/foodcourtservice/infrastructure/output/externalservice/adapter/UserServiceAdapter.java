package com.pragma.foodcourtservice.infrastructure.output.externalservice.adapter;

import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.User;
import com.pragma.foodcourtservice.domain.api.IUserServicePort;
import com.pragma.foodcourtservice.infrastructure.constants.InfrastructureConstants;
import com.pragma.foodcourtservice.infrastructure.output.externalservice.client.UserFeignClient;
import com.pragma.foodcourtservice.infrastructure.output.externalservice.dto.UserDTO;
import com.pragma.foodcourtservice.infrastructure.output.externalservice.mapper.IUserMapper;
import com.pragma.foodcourtservice.infrastructure.output.security.helper.TokenRelayService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceAdapter implements IUserServicePort {

    private final UserFeignClient userFeignClient;
    private final IUserMapper userMapper;
    private final TokenRelayService tokenRelayService;

    @Override
    public User getUserById(Long userId) {
        try {
            String authorization = tokenRelayService
                    .resolveAuthorizationHeader()
                    .orElseThrow(() -> new DomainException(DomainConstants.MSG_USER_SERVICE_UNAUTHORIZED, HttpStatus.UNAUTHORIZED));

            UserDTO dto = userFeignClient.getUserById(userId, authorization);
            return userMapper.toModel(dto);
        } catch (FeignException.NotFound ex) {
            log.error(InfrastructureConstants.LOG_USER_FETCH_NOT_FOUND, userId, ex.getMessage());
            throw new DomainException(DomainConstants.MSG_OWNER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            log.error(InfrastructureConstants.LOG_USER_FETCH_UNAUTHORIZED, userId, ex.getMessage());
            throw new DomainException(DomainConstants.MSG_USER_SERVICE_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        } catch (FeignException ex) {
            log.error(InfrastructureConstants.LOG_USER_FETCH_COMMUNICATION_ERROR, userId, ex.getMessage());
            throw new DomainException(DomainConstants.MSG_USER_SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}

