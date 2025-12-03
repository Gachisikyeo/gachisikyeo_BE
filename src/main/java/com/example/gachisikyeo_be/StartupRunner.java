package com.example.gachisikyeo_be;

import com.example.gachisikyeo_be.global.init.LawDongCsvInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {
    private final LawDongCsvInitializer lawDongCsvInitializer;

    @Override
    public void run(String... args) {
        lawDongCsvInitializer.initIfEmpty();
    }
}
