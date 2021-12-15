package com.icia.gitmemboard.service;

import com.icia.gitmemboard.dao.MDAO;
import com.icia.gitmemboard.dto.MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class MService {

    @Autowired
    private MDAO dao;

    @Autowired
    private HttpSession session;

    private ModelAndView mav = new ModelAndView();

    // 회원가입
    public ModelAndView mJoin(MEMBER member) throws IOException {
        System.out.println("[2] service : member -> " + member);

        // (1) 파일 불러오기
        MultipartFile mProfile = member.getMProfile();

        // (2) 파일 이름 설정하기
        String originalFileName = mProfile.getOriginalFilename();

        // 스프링 파일 업로드 할 때 문제점! + 파일 이름이 같을 경우!

        // (3) 난수 생성하기
        String uuid = UUID.randomUUID().toString().substring(1, 7);

        // (4) 난수와 파일이름 합치기
        String mProfileName = uuid + "_" + originalFileName;

        // (5) 파일 저장위치
        String savePath = "H:/boot/springBootWorkspace/GitHub_MEMBOARD/src/main/resources/static/profile/" + mProfileName;

        // (6) 파일 선택여부
        if (!mProfile.isEmpty()) {
            member.setMProfileName(mProfileName);
            mProfile.transferTo(new File(savePath));
        } else {
            member.setMProfileName("default.png");
        }

        // Q. 어떤 작업? 회원가입(입력)
        // Q. 입력, 수정, 삭제 시 int result 사용!
        int result = dao.mJoin(member);
        System.out.println("[3] service : result -> " + result);
        if (result > 0) {
            // 성공
            mav.setViewName("index");
        } else {
            // 실패
            mav.setViewName("joinForm");
        }

        return mav;
    }

    // 로그인
    public ModelAndView mLogin(MEMBER member) {
        System.out.println("[2] service : member -> " + member);

        // 입력한 id와 pw가 일치할 경우
        // id가 존재하는지 존재하지 않는지.. 존재한다면 id를 가져온다!

        // String loginId = dao.mLogin(member);

        MEMBER member1 = dao.mLogin(member);
        System.out.println("[3] service : member1 -> " + member1);
        if (member1 != null) {
            // 로그인 성공
            session.setAttribute("loginId", member1.getMId());
            session.setAttribute("loginProfile", member1.getMProfileName());
            mav.setViewName("index");
        } else {
            // 로그인 실패
            mav.setViewName("loginForm");
        }

        return mav;
    }

    // 회원목록보기
    public ModelAndView mList() {
        System.out.println("[2] service");

        // 목록 -> List, 상세 -> DTO
        List<MEMBER> memberList = dao.mList();

        System.out.println("[3] service : memberList -> " + memberList);

        mav.setViewName("mlist");
        mav.addObject("memberList", memberList);

        return mav;
    }

    // 회원 상세보기
    public ModelAndView mView(String mId) {
        MEMBER member = dao.mView(mId);

        if (member != null) {
            // 검색한 회원의 정보가 존재할 때 (not null일 때)
            mav.addObject("member", member);
            mav.setViewName("mview");
        } else {
            // 검색한 회원의 정보가 존재하지 않을 때 -> 리스트로 돌아가기
            // html파일이 아닌 controller의 주소로 값을 보낼 때 redirect:/주소
            mav.setViewName("redirect:/mList");
        }

        return mav;
    }

    // 회원 수정 페이지
    public ModelAndView mModiForm(String mId) {
        MEMBER member = dao.mView(mId);

        if (member != null) {
            // 검색한 회원의 정보가 존재할 때 (not null일 때)
            mav.addObject("member", member);
            mav.setViewName("mmodify");
        } else {
            // 검색한 회원의 정보가 존재하지 않을 때 -> 리스트로 돌아가기
            // html파일이 아닌 controller의 주소로 값을 보낼 때 redirect:/주소
            mav.setViewName("redirect:/mList");
        }

        return mav;
    }

    // 회원 수정
    public ModelAndView mModify(MEMBER member) {
        System.out.println("[2] service : member -> " + member);

        int result = dao.mModify(member);
        System.out.println("[3] service : result -> " + result);
        if (result > 0) {
            // 성공
            mav.setViewName("redirect:/mView?mId=" + member.getMId());
        } else {
            // 실패
            mav.setViewName("redirect:/mModiForm?mId=" + member.getMId());
        }

        return mav;
    }

    // 회원 삭제
    public ModelAndView mDelete(String mId) {
        int result = dao.mDelete(mId);

        if(result>0){
            mav.setViewName("redirect:/mList");
        } else {
            mav.setViewName("index");
        }

        return mav;
    }
}
