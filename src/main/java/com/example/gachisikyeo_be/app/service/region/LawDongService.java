package com.example.gachisikyeo_be.app.service.region;

import com.example.gachisikyeo_be.app.repository.region.LawDongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LawDongService {

    private final LawDongRepository lawDongRepository;

    public List<String> getSidoList() {
        return lawDongRepository.findAllSido();
    }

    public List<String> getSigunguList(String sido) {
        return lawDongRepository.findSigunguBySido(sido);
    }

    public List<String> getDongList(String sido, String sigungu) {
        return lawDongRepository.findDongBySidoAndSigungu(sido, sigungu);
    }
}
