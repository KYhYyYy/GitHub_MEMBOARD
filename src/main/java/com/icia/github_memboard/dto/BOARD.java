package com.icia.github_memboard.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

@Data
@Alias("board")
public class BOARD {
    private int bNo;                // 게시글 번호
    private String bWriter;         // 작성자
    private String bTitle;           // 글제목
    private String bContent;        // 글내용
    private String bDate;           // 작성일
    private int bHit;               // 조회수
    private MultipartFile bFile;     // 업로드 파일
    private String bFileName;      // 파일 이름
}
