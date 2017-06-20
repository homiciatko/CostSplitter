package com.agh.inzynierka.service.impl;

import com.agh.inzynierka.dao.TagDao;
import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.Tag;
import com.agh.inzynierka.model.User;
import com.agh.inzynierka.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TagDao tagDao;

    @Override
    public boolean saveTag(Tag tag, User user) {

        List<String> tagsRef;
        List<Tag> tags;

        if (user.getTagsRef()== null || user.getTagsRef().isEmpty()) {
            tagsRef = new ArrayList<>();
            tags = new ArrayList<>();
            tags.add(tag);
            user.setTags(tags);
            tagsRef.add(tagDao.save(tag).getId());
            user.setTagsRef(tagsRef);
            userDao.save(user);
            return true;
        }

        tagsRef = user.getTagsRef();
        tags = getTagsFromTagsId(tagsRef);
        Set<String> tagsName = new HashSet<>(getTagsNameFromTags(tags));

        if (tag.getId() == null && tagsName.add(tag.getName())) {
            tagsRef.add(tagDao.save(tag).getId());
            user.setTagsRef(tagsRef);
            userDao.save(user);
            return true;
        } else {
            tagDao.save(tag);
            return true;
        }
    }

    @Override
    public List<String> getTagsNameFromTags(List<Tag> tagList) {
        List<String> tagsNameList = new ArrayList<>();

        tagList.forEach(tag -> tagsNameList.add(tag.getName()));
        return tagsNameList;
    }

    @Override
    public List<Tag> getTagsFromTagsId(List<String> tagsId) {
        List<Tag> tagList = new ArrayList<>();
        tagsId.forEach(s -> tagList.add(tagDao.findOne(s)));

        return tagList;
    }

    @Override
    public List<Tag> getTags(User user) {

        List<Tag> tagList = new ArrayList<>();
        if (user.getTagsRef() == null) {

            return tagList;
        }
        user.getTagsRef().forEach(s -> tagList.add(tagDao.findOne(s)));

        return tagList;
    }
}
