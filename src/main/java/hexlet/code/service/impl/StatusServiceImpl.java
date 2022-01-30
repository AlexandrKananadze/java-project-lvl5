package hexlet.code.service.impl;

import hexlet.code.dto.StatusDto;
import hexlet.code.model.Status;
import hexlet.code.repository.StatusRepository;
import hexlet.code.service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public Status createNewStatus(StatusDto statusDto) {
        Status status = new Status(statusDto.getName());
        return statusRepository.save(status);
    }

    @Override
    public Status updateStatus(StatusDto statusDto, Long id) {
        return statusRepository.findById(id)
                .map(status -> {
                    status.setName(statusDto.getName());
                    return status;
                }).orElseThrow(() -> new NotFoundException("Status not found"));
    }
}
