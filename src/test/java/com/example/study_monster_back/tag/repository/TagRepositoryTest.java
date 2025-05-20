package com.example.study_monster_back.tag.repository;

import com.example.study_monster_back.tag.entity.Tag;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@DisplayName("TagRepository 테스트")
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;


    @Test
    void 존재하는_태그를_찾으면_해당_태그를_반환한다() {
        //given
        String tagName = "existing";
        Tag tag = Tag.builder().name(tagName).build();
        tagRepository.save(tag);

        //when
        Optional<Tag> foundTag = tagRepository.findByName(tagName);

        //then
        assertTrue(foundTag.isPresent());
        assertEquals(tagName, foundTag.get().getName());
    }

    @Test
    void 존재하지_않는_태그를_찾으면_비어있는_값을_반환한다() {
        //given
        String nonExistingTagName = "nonexisting";

        //when
        Optional<Tag> foundTag = tagRepository.findByName(nonExistingTagName);

        //then
        assertTrue(foundTag.isEmpty());
    }
}
