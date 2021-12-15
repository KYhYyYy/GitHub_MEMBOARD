package com.icia.gitmemboard.dao;

import com.icia.gitmemboard.dto.MEMBER;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MDAO {

    int mJoin(MEMBER member);

    MEMBER mLogin(MEMBER member);

    List<MEMBER> mList();

    MEMBER mView(String mId);

    int mModify(MEMBER member);

    int mDelete(String mId);
}
