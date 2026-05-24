package isep.psoft.aisafe.maintenance.services.common;

import isep.psoft.aisafe.maintenance.assemblers.TemplateAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.dto.TemplateDTO;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("ViewMaintenanceTemplateByIdUseCase")
public class ViewMaintenanceTemplateByIdUseCase {

    @Autowired
    private MaintenanceTemplateRepository repository;

    @Autowired
    private TemplateAssembler assembler;

    @Transactional(readOnly = true)
    public Optional<TemplateDTO> execute(Long templateId) {
        return repository.findById(templateId).map(assembler::toDTO);
    }
}
