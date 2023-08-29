package com.member.servletprojectmembermanagement.service;

import com.member.servletprojectmembermanagement.dao.BoardDao;
import com.member.servletprojectmembermanagement.dao.RippleDao;
import com.member.servletprojectmembermanagement.dto.BoardDto;
import com.member.servletprojectmembermanagement.dto.MemberDto;
import com.member.servletprojectmembermanagement.dto.RippleDto;
import com.member.servletprojectmembermanagement.vo.BoardVo;
import com.member.servletprojectmembermanagement.vo.MemberVo;
import com.member.servletprojectmembermanagement.vo.RippleVo;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
public class RippleServiceImpl implements RippleService {

    private static RippleServiceImpl instance;
    RippleDao rippleDao = RippleDao.getInstance();

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public RippleServiceImpl() {}

    public RippleServiceImpl(RippleDao rippleDao) {
        this.rippleDao = rippleDao;
    }
    public static RippleServiceImpl getInstance(RippleDao dao) {
        if (instance == null) {
            instance = new RippleServiceImpl(dao);
        }
        return instance;
    }


    /*
     * 댓글 생성
     * 댓글 생성 시 게시글 의 댓글 갯수 카운트 반영 메소드 동작
     */
    @Override
    public boolean registerRipple(HttpServletRequest request) throws Exception {
        BoardDao boardDao = BoardDao.getInstance();

        RippleDto rippleDto = new RippleDto();
        BoardDto boardDto = new BoardDto();
        MemberDto memberDto = new MemberDto();

        HttpSession session = request.getSession();
        memberDto.setId((String) session.getAttribute("sessionMemberId"));
        memberDto.setName((String) session.getAttribute("sessionMemberName"));

        boardDto.setNum(Long.parseLong(request.getParameter("num")));

        MemberVo member = MemberDto.toVo(memberDto);
        BoardVo board = BoardDto.toVo(boardDto);

        rippleDto.setBoard(board);
        rippleDto.setMember(member);
        rippleDto.setContent(request.getParameter("content"));
        rippleDto.setIp(request.getRemoteAddr());
        rippleDto.setCreatedAt(LocalDateTime.now());


        RippleVo ripple = RippleDto.toVo(rippleDto);
        boolean rippleSaved = rippleDao.save(ripple);
        log.info("RippleService Ripple saved: " + rippleSaved);

        if (rippleSaved) {
            boolean updatedCount = boardDao.updateRippleCountByNum(ripple.getBoard().getNum(), rippleDao.findRippleCountByBoardNum(ripple.getBoard().getNum()));
            log.info("RippleService Updated ripple count: " + updatedCount);
            return true;
        } else {
            return false;
        }
    }

    /*
     * 댓글 목록 조히
     */
    @Override
    public List<RippleDto> getRipplesByBoardNum(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("RippleService : getRipplesByBoardNum()");
        long boardNum = Long.parseLong(request.getParameter("boardNum"));
        List<RippleDto> rippleList = RippleDto.RippleListMapper(rippleDao.findRipplesByBoardNum(boardNum));
        //session 을 가지고 있는 경우 댓글 삭제 버튼 생성
        for (RippleDto ripple: rippleList) {
            if (ripple.getMember().getId().equals((String) request.getSession().getAttribute("sessionMemberId"))) {
                ripple.setLogin(true);
            } else {
                ripple.setLogin(false);
            }
        }
        return rippleList;
    }

    /*
     * 댓글 삭제
     */
    @Override
    public boolean removeRippleByRippleId(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("RippleService : removeRippleByRippleId()");
        BoardDao boardDao = BoardDao.getInstance();
        long rippleId = Long.parseLong(request.getParameter("rippleId"));
        long boardNum = rippleDao.findBoardByRippleId(rippleId);

        if (rippleDao.deleteRippleByRippleId(rippleId)) {
            boardDao.updateRippleCountByNum(boardNum, rippleDao.findRippleCountByBoardNum(boardNum));
            return true;
        } else {
            return false;
        }
    }
}
