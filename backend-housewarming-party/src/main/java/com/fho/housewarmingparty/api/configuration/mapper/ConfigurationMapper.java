package com.fho.housewarmingparty.api.configuration.mapper;

import com.fho.housewarmingparty.api.configuration.dto.ConfigurationDTO;
import com.fho.housewarmingparty.api.configuration.entity.Configuration;
import com.fho.housewarmingparty.utils.mapper.DataMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigurationMapper extends DataMapper<Configuration, ConfigurationDTO> {
}
