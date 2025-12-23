package com.example.gachisikyeo_be.app.repository.region;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LawDongRepository extends JpaRepository<LawDong, Long> {
    Optional<LawDong> findByLawCode(String lawCode);

    @Query("select distinct l.sido from LawDong l order by l.sido asc")
    List<String> findAllSido();

    @Query("select distinct l.sigungu from LawDong l " +
            "where l.sido = :sido order by l.sigungu asc")
    List<String> findSigunguBySido(String sido);

    @Query("select distinct l.dong from LawDong l " +
            "where l.sido = :sido and l.sigungu = :sigungu order by l.dong asc")
    List<String> findDongBySidoAndSigungu(String sido, String sigungu);

    Optional<LawDong> findBySidoAndSigunguAndDong(String sido, String sigungu, String dong);
}
