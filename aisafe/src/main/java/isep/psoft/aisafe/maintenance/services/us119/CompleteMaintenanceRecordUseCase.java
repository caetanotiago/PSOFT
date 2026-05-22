package isep.psoft.aisafe.maintenance.services.us119;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.CompletionNotes;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service("CompleteMaintenanceRecordUseCase")
public class CompleteMaintenanceRecordUseCase {

    @Autowired
    private MaintenanceRecordRepository repository;

    @Autowired
    private MaintenanceRecordAssembler assembler;

    @Transactional
    public MaintenanceRecordOutputDto execute(Long recordId, CompleteRecordInputDto dto, String version) {
        // 1. Validar a versão (ETag)
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("ETag version is required via If-Match header.");
        }
        long clientVersion;
        try {
            clientVersion = Long.parseLong(version.replace("\"", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ETag format.");
        }

        // 2. Encontrar a entidade
        MaintenanceRecord record = repository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Maintenance Record not found with id: " + recordId));

        // 3. Verificar a versão (Optimistic Locking)
        if (record.getVersion() != clientVersion) {
            throw new OptimisticLockException("The resource was modified by another user. Please refresh and try again.");
        }

        // 4. Executar a lógica de negócio
        CompletionNotes notes = new CompletionNotes(dto.getNotes(), LocalDate.now());
        record.complete(notes);

        // 5. Persistir e retornar
        MaintenanceRecord savedRecord = repository.save(record);
        return assembler.toModel(savedRecord);
    }
}
