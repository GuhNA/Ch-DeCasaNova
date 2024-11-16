package com.fho.housewarmingparty.api.image.mapper;

import com.fho.housewarmingparty.api.image.dto.ImageDTO;
import com.fho.housewarmingparty.api.image.entity.Image;
import com.fho.housewarmingparty.utils.mapper.DataMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper extends DataMapper<Image, ImageDTO> {
}
