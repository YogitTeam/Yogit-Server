package com.yogit.server.report.service.comment;

import com.yogit.server.board.entity.Comment;
import com.yogit.server.board.exception.comment.NotFoundCommentException;
import com.yogit.server.board.repository.CommentRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateCommentReportReq;
import com.yogit.server.report.dto.res.CommentReportRes;
import com.yogit.server.report.entity.CommentReport;
import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.report.exception.AlreadyReportCommentException;
import com.yogit.server.report.exception.MaxReportingCntException;
import com.yogit.server.report.repository.CommentReportRepository;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import com.yogit.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentReportServiceImpl implements CommentReportService{

    private final CommentReportRepository commentReportRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<CommentReportRes> createCommentReport(CreateCommentReportReq dto) {

        userService.validateRefreshToken(dto.getReportingUserId(), dto.getRefreshToken());

        User reportingUser = userRepository.findByUserId(dto.getReportingUserId())
                .orElseThrow(() -> new NotFoundUserException());
        User reportedUser = userRepository.findByUserId(dto.getReportedUserId())
                .orElseThrow(() -> new NotFoundUserException());
        Comment reportedComment = commentRepository.findCommentById(dto.getReportedCommentId())
                .orElseThrow(() -> new NotFoundCommentException());

        //validation: 신고하는 유저의 신고 한 횟수 검증, 일주일에 신고 5번 이하 허용
        if(reportingUser.getReportingCnt() > 5){
            throw new MaxReportingCntException();
        }
        //validation: 신고 받는 코멘트가 이미 신고 받았는지 검증
        if(!commentReportRepository.findByReportingUserIdAndReportedCommentId(dto.getReportingUserId(), dto.getReportedCommentId()).isEmpty()){
            throw new AlreadyReportCommentException();
        }


        CommentReport commentReport = new CommentReport(dto.getContent(), reportingUser, reportedUser, dto.getReportTypeNum(), ReportStatus.ONGOIN, reportedComment);
        commentReportRepository.save(commentReport);

        reportedComment.changeReportedCnt();//신고 당한 횟수 +1 증가

        CommentReportRes res = CommentReportRes.toDto(commentReport);
        return ApplicationResponse.create("코멘트 신고가 생성되었습니다.", res);
    }
}
