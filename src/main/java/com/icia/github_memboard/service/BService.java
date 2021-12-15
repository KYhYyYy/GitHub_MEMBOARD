package com.icia.github_memboard.service;

import com.icia.github_memboard.dao.BDAO;
import com.icia.github_memboard.dto.BOARD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BService {

    @Autowired
    private BDAO dao;

    private ModelAndView mav = new ModelAndView();

    // 글 작성 메소드
    public ModelAndView bWrite(BOARD board) throws IOException {
        // 1. 파일 불러오기
        MultipartFile bFile = board.getBFile();

        // 2. 원본 파일 이름 가져오기
        String originalFileName = bFile.getOriginalFilename();

        // 3. 랜덤한 문자열 만들기
        String uuid = UUID.randomUUID().toString().substring(1,7);

        // 4. 3번(난수)과 2번(원본파일이름) 합치기!
        String bFileName = uuid + "_" + originalFileName;

        // 5. 파일 저장위치
        String savePath = "H:/boot/springBootWorkspace/GitHub_MEMBOARD/src/main/resources/static/upload/"+bFileName;

        // 6. 파일 선택여부
        if (!bFile.isEmpty()){
            board.setBFileName(bFileName);
            bFile.transferTo(new File(savePath));
        }

        // Q. 입력, 수정, 삭제 시 필요한 데이터타입과 변수는?
        int result = dao.bWrite(board);

        // 성공하면 result = 1, 실패하면 result = 0
        if(result>0){
            mav.setViewName("redirect:/bList");
        } else {
            mav.setViewName("bwrite");
        }

        return mav;
    }

    // 글목록보기 메소드
    public ModelAndView bList() {
        // 상세보기 : DTO
        // 목록보기 : List<DTO>

        List<BOARD> boardList = dao.bList();

        mav.setViewName("blist");
        mav.addObject("boardList", boardList);

        return mav;
    }

    // 게시글 상세보기 메소드
    public ModelAndView bView(int bNo) {

        // (1) 조회수 증가
        dao.bCount(bNo);

        // (2) 게시글 상세보기
        BOARD board = dao.bView(bNo);

        if(board!=null){
            mav.addObject("board", board);
            mav.setViewName("bview");
        } else {
            mav.setViewName("redirect:/bList");
        }

        return mav;
    }

    // 수정 페이지
    public ModelAndView bModiForm(int bNo) {
        // 상세보기 때 만들어 놓은 bView(bNo)메소드 사용
        BOARD board = dao.bView(bNo);

        if(board!=null){
            mav.addObject("board", board);
            mav.setViewName("bmodify");
        } else {
            mav.setViewName("redirect:/bList");
        }

        return mav;
    }

    // 수정 메소드
    public ModelAndView bModify(BOARD board) throws IOException {
        // 1. 파일 불러오기
        MultipartFile bFile = board.getBFile();

        // 2. 원본 파일 이름 가져오기
        String originalFileName = bFile.getOriginalFilename();

        // 3. 랜덤한 문자열 만들기
        String uuid = UUID.randomUUID().toString().substring(1,7);

        // 4. 3번(난수)과 2번(원본파일이름) 합치기!
        String bFileName = uuid + "_" + originalFileName;

        // 5. 파일 저장위치
        String savePath = "H:/boot/springBootWorkspace/GitHub_MEMBOARD/src/main/resources/static/upload/"+bFileName;

        // 6. 파일 선택여부
        if (!bFile.isEmpty()){
            board.setBFileName(bFileName);
            bFile.transferTo(new File(savePath));
        }

        // Q. 입력, 수정, 삭제 시 필요한 데이터타입과 변수는?
        int result = dao.bModify(board);

        // 성공하면 result = 1, 실패하면 result = 0
        if(result>0){
            mav.setViewName("redirect:/bView?bNo="+board.getBNo());
        } else {
            mav.setViewName("redirect:/bModiForm?bNo="+board.getBNo());
        }

        return mav;
    }

    // 삭제 메소드
    public ModelAndView bDelete(int bNo) {
        int result = dao.bDelete(bNo);

        if(result>0){
            mav.setViewName("redirect:/bList");
        } else {
            mav.setViewName("redirect:/bView?bNo="+bNo);
        }

        return mav;
    }
}
