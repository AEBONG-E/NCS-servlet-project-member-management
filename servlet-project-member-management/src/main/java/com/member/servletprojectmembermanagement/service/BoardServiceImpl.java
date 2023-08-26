package com.member.servletprojectmembermanagement.service;

import com.member.servletprojectmembermanagement.dao.BoardDao;
import com.member.servletprojectmembermanagement.dto.BoardDto;
import com.member.servletprojectmembermanagement.dto.MemberDto;
import com.member.servletprojectmembermanagement.vo.BoardVo;
import com.member.servletprojectmembermanagement.vo.MemberVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
public class BoardServiceImpl implements BoardService {

    private static BoardServiceImpl instance;
    BoardDao boardDao = BoardDao.getInstance();

    private static final int LISTCOUNT = 5; //한페이지에 노출 할 게시물의 수

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public BoardServiceImpl() {}

    public BoardServiceImpl(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    public static BoardServiceImpl getInstance(BoardDao dao) {
        if (instance == null) {
            instance = new BoardServiceImpl(dao);
        }
        return instance;
    }

    /*
     * 게시글 생성
     */
    @Override
    public boolean registerBoard(HttpServletRequest request) throws SQLException, ClassNotFoundException, Exception {

        BoardDto boardDto = new BoardDto();
        MemberDto memberDto = new MemberDto();

        HttpSession session = request.getSession();
        memberDto.setId((String) session.getAttribute("sessionMemberId"));
        memberDto.setName(request.getParameter("name"));

        MemberVo member = MemberDto.toVo(memberDto);

        uploadFileForm(request, boardDto, memberDto); //파일 저장

        boardDto.setMember(member);
        boardDto.setTitle(request.getParameter("title"));
        boardDto.setContent(request.getParameter("content"));
        boardDto.setHit(Integer.valueOf(request.getParameter("hit"))); //조회수
        boardDto.setIp(request.getRemoteAddr()); //접근 IP
        boardDto.setRippleCnt(0);
        boardDto.setCreatedAt(LocalDateTime.now());

        BoardVo board = BoardDto.toVo(boardDto);
        log.info("BoardService: registerBoard() - board: {}", board);
        return boardDao.save(board);
    }

    /*
     * 게시판 조회
     */
    @Override
    public void getBoardList(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("BoardService: getBoardList()");

        List<BoardDto> boardList; //게시물 담는 리스트
        int pageNum = 1; //페이지 번호
        int limit = LISTCOUNT; //한페이지에 노출 할 게시물의 수
        String items = "title"; //검색 필드
        String text = "Lorem Ipsum"; //검색어
        int totalRecord = 0; //전체 게시물 수
        int totalPage = 0; //전체 페이지 수

        //전달되는 페이지 번호가 있으면 실행
        if (request.getParameter("pageNum") != null) {
            pageNum = Integer.parseInt(request.getParameter("pageNum"));
        }
        items = request.getParameter("items");
        text = request.getParameter("text");

        totalRecord = boardDao.findBoardListCountByItemsAndByText(items, text);

        //현재 페이지에 해당하는 게시물 목록(페이지 개수는 5개 이하)
        boardList = BoardDto.BoardListMapper(boardDao.findList(pageNum, limit, items, text));

        //전체 페이지 계산
        totalPage = (int) Math.ceil((double) totalRecord / limit);

        /*
         * view 에 전달할 값들
         * return 이 없는 이유는
         * setAttribute 로 값들을 모두 전달하기 때문
         */
        request.setAttribute("limit", limit);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalRecord", totalRecord);
        request.setAttribute("boardList", boardList);
        request.setAttribute("items", items);
        request.setAttribute("text", text);

        /*
         * 페이징을 위한 추가 코드
         */
        int pagePerBlock = 5;   //페이지 출력시 나올 범위.
        //전체 블럭 수
        int totalBlock = totalPage % pagePerBlock == 0 ? totalPage / pagePerBlock : totalPage / pagePerBlock + 1;
        int thisBlock = ((pageNum - 1) / pagePerBlock) + 1;    //현재 블럭
        int firstPage = ((thisBlock - 1) * pagePerBlock) + 1;   //블럭의 첫 페이지
        int lastPage = thisBlock * pagePerBlock;    //블럭의 마지막 페이지
        lastPage = Math.min(lastPage, totalPage);

        //view 전달
        request.setAttribute("pagePerBlock", pagePerBlock);
        request.setAttribute("totalBlock", totalBlock);
        request.setAttribute("thisBlock", thisBlock);
        request.setAttribute("firstPage", firstPage);
        request.setAttribute("lastPage", lastPage);
    }

    /*
     * 게시글 조회
     */
    @Override
    public void getBoard(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("BoardService: getBoard()");

        long num = Integer.parseInt(request.getParameter("num"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));

        boardDao.updateBoardHitByNum(num);
        BoardDto board = BoardDto.BoardMapper(boardDao.findBoardByNum(num));

        request.setAttribute("num", num);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("board", board);
    }

    /*
     * 게시글 수정
     */
    @Override
    public boolean updateBoard(HttpServletRequest request) throws SQLException, ClassNotFoundException, Exception {
        BoardDto boardDto = new BoardDto();
        MemberDto memberDto = new MemberDto();

        HttpSession session = request.getSession();
        memberDto.setId((String) session.getAttribute("sessionMemberId"));
        memberDto.setName(request.getParameter("name"));

        MemberVo member = MemberDto.toVo(memberDto);

        uploadFileForm(request, boardDto, memberDto); // 파일 저장, 필요한 경우에만

        boardDto.setNum(Long.parseLong(request.getParameter("num")));
        boardDto.setMember(member);
        boardDto.setTitle(request.getParameter("title"));
        boardDto.setContent(request.getParameter("content"));
//        boardDto.setIp(request.getRemoteAddr()); // 접근 IP
        boardDto.setUpdatedAt(LocalDateTime.now());

        BoardVo board = BoardDto.toVo(boardDto);
        log.info("BoardService: updateBoard() - board: {}", board);

        return boardDao.update(board);
    }


    /**
     * 게시글에 파일 업로드 기능
     * updateBoard() 로직 내부 포함 메소드
     * @param request
     * @param board
     * @throws Exception
     */
    private void uploadFileForm(HttpServletRequest request, BoardDto board, MemberDto member) throws Exception {
        //폼 페이지에서 전송된 파일을 저장할 서버의 경로를 작성.
        String path = "C:\\upload";

        //파일 업로드를 위해 DiskFileUpload 클래스를 생성.
        DiskFileUpload upload = new DiskFileUpload();

        //업로드할 파일의 최대 크기, 메모리상에 저장할 최대 크기, 업로드 된 파일을 임시로 저장할 경로를 작성.
        upload.setSizeMax(1000000);
        upload.setSizeThreshold(4096);
        upload.setRepositoryPath(path);

        //폼 페이지에서 전송된 요청 파라미터를 Iterator 클래스로 변환.
//        Iterator params = items.iterator();
        //폼 페이지에서 전송된 요청 파라미터를 전달받도록 DiskFileUpload 객체 타입의 parseRequest() 메소드 작성.
        List<FileItem> items = upload.parseRequest(request);
        for (FileItem item : items) {
            if (item.isFormField()) {
                //폼 페이지에서 전송된 요청 파라미터가 일반 데이터면 요청 파라미터의 이름과 값을 출력.
                String name = item.getFieldName();
                String value = item.getString("UTF-8");

                switch (name) {
                    case "num":
                        board.setNum(Integer.valueOf(value));
                    case "name":
                        member.setName(value);
                        break;
                    case "title":
                        board.setTitle(value);
                        break;
                    case "content":
                        board.setContent(value);
                        break;
                }
                log.info("BoardService: uploadFileForm() - " + name + "=" + value);
            }
            else {
                /*
                 * 폼 페이지에서 전송된 요청 파라미터가 파일이면
                 * 요청 파라미터의 이름, 저장 파일의 이름, 파일 컨텐츠 유형, 파일 크기에 대한 정보를 출력
                 */
                String fileFiledName = item.getFieldName();
                String fileName = item.getName();
                String contentType = item.getContentType();

                if (!fileName.isEmpty()) {
                    log.info("BoardService: uploadFileForm() - 파일이름 : " + fileName);
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    int fileSize = (int) item.getSize();

                    File file = new File(path + "/" + fileName);
                    item.write(file);

                    board.setFileName(fileName);
                    board.setFileSize((long) fileSize);

                    log.info(" BoardService: uploadFileForm() - 저장 파일 이름 : " + fileName);
                    log.info("BoardService: uploadFileForm() - 파일 크기 : " + fileSize);
                }
            }
        }
    }

    /*
     * 게시글 삭제
     */
    @Override
    public boolean removeBoard(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("BoardService: removeBoard()");

        long num = Integer.parseInt(request.getParameter("num"));
        return boardDao.deleteByNum(num);
    }
}
