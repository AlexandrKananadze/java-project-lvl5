package hexlet.code.service.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private LabelRepository labelRepository;

    @Override
    public Label newLabel(LabelDto labelDto) {
        Label label = new Label(labelDto.getName());
        return labelRepository.save(label);
    }

    @Override
    public Label changeLabel(Long id, LabelDto labelDto) {
        return labelRepository.findById(id)
                .map(label -> {
                    label.setName(labelDto.getName());
                    return labelRepository.save(label);
                }).orElseThrow(() -> new NotFoundException("Label not found"));
    }
}
