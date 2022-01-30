package hexlet.code.service;

import hexlet.code.dto.StatusDto;
import hexlet.code.model.Status;

public interface StatusService {
    Status createNewStatus(StatusDto statusDto);
    Status updateStatus(StatusDto statusDto, Long id);
}
