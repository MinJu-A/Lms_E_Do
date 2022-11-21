package com.edo.user.controller;

import com.edo.community.entity.Community;
import com.edo.community.repository.CommunityRepository;
import com.edo.lecture.entity.Lecture;
import com.edo.lecture.repository.LectureRepository;
import com.edo.lecture.repository.LectureSubscribeRepository;
import com.edo.lecture.service.LectureService;
import com.edo.user.dto.MemberDto;
import com.edo.user.entity.Member;
import com.edo.user.repository.MemberRepository;
import com.edo.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final LectureRepository lectureRepository;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final LectureService lectureService;

    private final LectureSubscribeRepository lectureSubscribeRepository;

    @GetMapping(value = "/login")
    public String Login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        log.info("loginForm view resolve");

        return "member/login";
    }

    //   이용약관
    @GetMapping(value = "/join")
    public String MemberJoinGet(Model model) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 회원가입 들어가는 중 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return "member/memberJoin";
    }


    // 이메일 등록
    @GetMapping(value = "/joinInfo")
    public String memberJoinInfoGet() {
        return "member/memberjoinInfo";
    }

    //    회원가입 값 전달
    @PostMapping(value = "/joinInfo")
    public String MemberJoinPost(@Valid MemberDto memberDto, Model model) {

        Member member = Member.createUser(memberDto, passwordEncoder);

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>" + member);

        try {
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            return "member/memberjoinInfo";
        }

//        성공 시 로그인 페이지로 돌아간다
        return "member/login";
    }

    //    이메일 중복 확인을 위한 메소드
    @ResponseBody //view가 아닌 data 그대로를 반환합니다.
    @PostMapping(value = "/joinInfo/validateEmail")
    public Boolean ValidateEmail(@RequestBody MemberDto memberDto) {
        log.info(memberDto.toString());
        try {
//        이메일 중복 검사 실행
            memberService.validateDuplicateMemberEmail(memberDto.getMemberEmail());

        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    //    닉네임 중복 확인을 위한 메소드
    @ResponseBody
    @PostMapping(value = "/joinInfo/validateNickname")
    public Boolean ValidateNickname(@RequestBody MemberDto memberDto) {
        log.info(memberDto.toString());
        try {
//        닉네임 중복 검사 실행
            memberService.validateDuplicateNickname(memberDto.getMemberNickname());
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }


    // 마이페이지
    @GetMapping(value = "/mypage")

    public String myPageGet(Model model,
                            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                            @RequestParam(value = "size", required = false, defaultValue = "4") int size,
                            Principal principal) {

        Member member = memberService.communityMember(principal.getName());
        log.info(">>>>>>>>>>>>>>>>>>>member를 가져오나요<<<<<<<<<<<<<<<<<<<<<<<<<" + principal.getName());
        List<Member> memberList = memberRepository.findAllByOrderByMemberId();
        log.info(">>>>>>>>>>>>>>>>>memberList 가져오기 성공!<<<<<<<<<<<<<<<<<<<<<<<" + memberList.toString());
        model.addAttribute("member", member);

        List<Community> communityMainList = communityRepository.findAllById(5);
        log.info(">>>>>>>>>>>>>>>>>communitylist  사이즈 가져오기<<<<<<<<<<<<<<<<<<<<<<<" + communityMainList.size());

        List<Long> lectureIds = lectureSubscribeRepository.searchLectureIdByMemberAndHeart(member.getMemberId());
        List<Lecture> lectureList = new ArrayList<>();

        for(Long lectureId : lectureIds){
            Lecture lecture = lectureService.getLectureById(lectureId);
            lectureList.add(lecture);
        }
        model.addAttribute("communityMainList", communityMainList);
        model.addAttribute("posts", lectureService.getPage(pageNumber, size));
        model.addAttribute("postlike",lectureList);

        return "mypage/mypageMain";
    }

    @ResponseBody
    @PostMapping(value = "/mypage/editNickname")
    public Boolean editNickname(@RequestBody MemberDto memberDto) {
        log.info(memberDto.getMemberNickname() + "<<<<<<<<<<<<<<<<<<<<<<<<<");
        try {
            memberService.validateDuplicateNickname(memberDto.getMemberNickname());
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }


    @PostMapping(value = "/mypage")
    public String myPagePost(MemberDto memberDto, Model model){


        return "/mypage/mypageMain";
    }

    //로그인 실패 시 에러 메세지 나타냄 
    @GetMapping(value = "/login/error")
    public String loginErrorGet(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "exception", required = false) String exception, Model model) {
        log.info("=================> 오류 발생" + error + ", " + exception);
        model.addAttribute("loginErrorMsg", exception);
        return "/member/login";
    }
}


