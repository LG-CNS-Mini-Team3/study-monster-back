package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.tag.entity.Tag;

public interface TagService {

    Tag findOrCreateTag(String tagName);
}