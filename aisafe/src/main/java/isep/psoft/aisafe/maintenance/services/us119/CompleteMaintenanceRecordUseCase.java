package isep.psoft.aisafe.maintenance.services.us119;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.CompletionNotes;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service("CompleteMaintenanceRecordUseCase")
public class CompleteMaintenanceRecordUseCase {

    @Autowired
    private MaintenanceRecordRepository repository;

    @Autowired
    private MaintenanceRecordAssembler assembler;

    @Transactional
    public MaintenanceRecordOutputDto execute(Long recordId, CompleteRecordInputDto dto, String version) {

        MaintenanceRecord record = repository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance Record not found with id: " + recordId));

        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("ETag version is required via If-Match header.");
        }

        long clientVersion;
        try {
            clientVersion = Long.parseLong(version.replace("\"", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ETag format.");
        }

        if (record.getVersion() == null) {
            throw new IllegalStateException("The record to be updated must have a version.");
        }

        if (!record.getVersion().equals(clientVersion)) {
            throw new OptimisticLockException("The resource was modified by another user. Please refresh and try again.");
        }

        CompletionNotes notes = new CompletionNotes(dto.getNotes(), LocalDate.now());
        record.complete(notes);

        MaintenanceRecord savedRecord = repository.save(record);
        return assembler.toModel(savedRecord);
    }
}