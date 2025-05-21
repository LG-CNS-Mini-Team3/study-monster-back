package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.tag.entity.Tag;
import com.example.study_monster_back.tag.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@DisplayName("TagService 테스트")
public class TagServiceImplTest {
    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("존재하는 태그를 찾으면 해당 태그를 반환한다")
    void 존재하는_태그를_찾으면_해당_태그를_반환한다() {
        // given
        String tagName = "existing";
        Tag tag = Tag.builder().name(tagName).build();
        tagRepository.save(tag);

        // when
        Tag result = tagService.findOrCreateTag(tagName);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(tagName);
        assertThat(result.getId()).isEqualTo(tag.getId());
    }

    @Test
    void 존재하지_않는_태그는_새로_생성하여_반환한다() {
        // given
        String tagName = "newTag";

        // when
        Tag result = tagService.findOrCreateTag(tagName);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(tagName);
        assertThat(result.getId()).isNotNull();


        Optional<Tag> savedTag = tagRepository.findByName(tagName);
        assertThat(savedTag).isPresent();
        assertThat(savedTag.get().getId()).isEqualTo(result.getId());
    }
}
