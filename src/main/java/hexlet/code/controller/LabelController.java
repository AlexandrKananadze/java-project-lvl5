package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
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

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String ID = "/{id}";

    private LabelRepository labelRepository;
    private LabelService labelService;

    @Operation(summary = "Get all labels")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    @Operation(summary = "Get label by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping(ID)
    public Label getById(@PathVariable("id") Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Label not found"));
    }

    @Operation(summary = "Delete label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "422", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping(ID)
    public void deleteLabel(@PathVariable("id") Long id) {
        labelRepository.deleteById(id);
    }

    @Operation(summary = "New label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "422", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Label newLabel(@Valid @RequestBody LabelDto labelDto) {
        return labelService.newLabel(labelDto);
    }

    @Operation(summary = "Change label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "422", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PutMapping(ID)
    public Label changeLabel(@PathVariable("id") Long id, @Valid @RequestBody LabelDto labelDto) {
        return labelService.changeLabel(id, labelDto);
    }
}
