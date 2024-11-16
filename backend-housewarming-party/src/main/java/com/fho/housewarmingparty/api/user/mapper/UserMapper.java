package com.fho.housewarmingparty.api.user.mapper;

import com.fho.housewarmingparty.api.user.dto.UserDTO;
import com.fho.housewarmingparty.api.user.entity.User;
import com.fho.housewarmingparty.utils.mapper.DataMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends DataMapper<User, UserDTO> {

    @Override
    @Mapping(target = "password", ignore = true)
    UserDTO toDto(User entity);
}
