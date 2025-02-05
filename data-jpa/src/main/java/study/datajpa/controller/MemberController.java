package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member =memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDTO> list(Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDTO> map=page.map(member -> new MemberDTO(member.getId(),member.getUsername(),null)
        );
        return map;
    }

    //@PostConstruct
    public void init(){
        for (int i=0;i<100;i++){
            memberRepository.save(new Member("member"+i,i));
        }
        //memberRepository.save(new Member("ABCD"));
    }
}
