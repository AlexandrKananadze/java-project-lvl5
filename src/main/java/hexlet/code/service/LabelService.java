package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

public interface LabelService {
    Label newLabel(LabelDto labelDto);
    Label changeLabel(Long id, LabelDto labelDto);
}
