package hexlet.code.controller;

import hexlet.code.dto.StatusDto;
import hexlet.code.model.Status;
import hexlet.code.repository.StatusRepository;
import hexlet.code.service.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;
import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.StatusController.STATUS_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class StatusController {
    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    private final StatusRepository statusRepository;
    private final StatusService statusService;

    public StatusController(StatusRepository statusRepository, StatusService statusService) {
        this.statusRepository = statusRepository;
        this.statusService = statusService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Status createStatus(@Valid @RequestBody StatusDto statusDto) {
        return statusService.createNewStatus(statusDto);
    }

    @GetMapping(ID)
    public Status getStatusById(@PathVariable("id") Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status not found"));
    }

    @GetMapping
    public List<Status> getAll() {
        return statusRepository.findAll();
    }

    @DeleteMapping(ID)
    public void deleteStatus(@PathVariable("id") Long id) {
        statusRepository.deleteById(id);
    }

    @PutMapping(ID)
    public Status updateStatus(@Valid @RequestBody StatusDto statusDto, @PathVariable("id") Long id) {
        return statusService.updateStatus(statusDto, id);
    }
}
