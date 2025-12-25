package com.example.gachisikyeo_be.app.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "페이지 응답 공통 DTO (프론트 친화)")
@Getter
@AllArgsConstructor
public class PageResponse<T> {
    @Schema(description = "목록")
    private final List<T> items;

    @Schema(description = "현재 페이지(0부터)")
    private final int page;

    @Schema(description = "요청 size (unpaged면 -1)")
    private final int size;

    @Schema(description = "전체 요소 수")
    private final long totalElements;

    @Schema(description = "전체 페이지 수")
    private final int totalPages;

    @Schema(description = "다음 페이지 존재 여부")
    private final boolean hasNext;

    public static <T> PageResponse<T> from(Page<T> page) {
        int size = page.getPageable().isPaged() ? page.getSize() : -1;

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                size,
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }
}
