package plate.back.domain.record.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import plate.back.domain.record.dto.MultiResponseDto;
import plate.back.domain.record.dto.RecordRequestDto;
import plate.back.domain.record.dto.RecordResponseDto;
import plate.back.global.flask.dto.FlaskResponseDto;

public interface RecordService {
    public MultiResponseDto recordLog(FlaskResponseDto flaskResponseDto, String[] vehicleImgArr) throws IOException;

    public List<RecordResponseDto> searchDate(String start, String end) throws ParseException;

    public List<RecordResponseDto> searchPlate(String plate);

    public String updateRecord(RecordRequestDto.Update resqDto) throws IOException;

    public void deleteRecord(RecordRequestDto.Delete resqDto);
}
