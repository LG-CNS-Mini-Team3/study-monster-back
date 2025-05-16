package com.example.study_monster_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyMonsterBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyMonsterBackApplication.class, args);
	}

}
