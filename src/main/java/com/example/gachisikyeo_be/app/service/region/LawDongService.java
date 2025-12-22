package com.example.gachisikyeo_be.app.service.region;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.app.dto.LawDongDto;
import com.example.gachisikyeo_be.app.repository.region.LawDongRepository;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.LawDongNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LawDongService {

    private final LawDongRepository lawDongRepository;

    @Transactional(readOnly = true)
    public List<String> getSidoList() {
        return lawDongRepository.findAllSido();
    }

    @Transactional(readOnly = true)
    public List<String> getSigunguList(String sido) {
        return lawDongRepository.findSigunguBySido(sido);
    }

    @Transactional(readOnly = true)
    public List<String> getDongList(String sido, String sigungu) {
        return lawDongRepository.findDongBySidoAndSigungu(sido, sigungu);
    }

    @Transactional
    public LawDongDto resolveRegion(String sido, String sigungu, String dong) {
        LawDong lawDong = lawDongRepository
                .findBySidoAndSigunguAndDong(sido, sigungu, dong)
                .orElseThrow(() -> new LawDongNotFoundException(ErrorCode.REGION_NOT_FOUND));

        return LawDongDto.from(lawDong);
    }
}
