package isep.psoft.aisafe.maintenance.services.common;

import isep.psoft.aisafe.maintenance.assemblers.TemplateAssembler;
import isep.psoft.aisafe.maintenance.dto.TemplateDTO;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("ViewMaintenanceTemplateByIdUseCase")
@RequiredArgsConstructor
public class ViewMaintenanceTemplateByIdUseCase {

    private final MaintenanceTemplateRepository repository;

    private final TemplateAssembler assembler;

    @Transactional(readOnly = true)
    public Optional<TemplateDTO> execute(Long templateId) {
        return repository.findById(templateId).map(assembler::toDTO);
    }
}
