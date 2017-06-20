package com.agh.inzynierka.service;


import com.agh.inzynierka.model.Tag;
import com.agh.inzynierka.model.User;

import java.util.List;

public interface TagService {
    public boolean saveTag(Tag tag, User user);

    List<String> getTagsNameFromTags(List<Tag> tagList);

    List<Tag> getTagsFromTagsId(List<String> tagsId);

    List<Tag> getTags(User user);
}
