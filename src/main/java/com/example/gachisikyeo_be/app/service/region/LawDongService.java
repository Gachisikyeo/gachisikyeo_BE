package com.example.gachisikyeo_be.app.service.region;

import com.example.gachisikyeo_be.app.repository.region.LawDongRepository;
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
}
