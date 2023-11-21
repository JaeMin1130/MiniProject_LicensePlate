package plate.back.domain.record.dto;

import lombok.Getter;

public class RecordRequestDto {

    @Getter
    public static class Update {
        private Integer recordId;
        private String licensePlate;
    }

    @Getter
    public static class Delete {
        private Integer recordId;
    }
}
