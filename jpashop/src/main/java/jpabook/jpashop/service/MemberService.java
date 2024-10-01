package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryNew;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//데이터 변경 시에는 트랜잭션이 필요.
// 클래스에 적용 시 public에 모두 걸림
//readOnly를 true로 하면 쓰기를 허용하지 않는다
@RequiredArgsConstructor
public class MemberService {

    //필드에서도 Autowired도 주입할 수 있지만, 이 경우 가짜 리포지토리를 주입해주는 게 어렵다.
    private final MemberRepositoryNew memberRepository;

//    @Autowired
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository=memberRepository;
//    }

    //회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //오류 시 예외
        //실제로는 멀티스레드 환경이므로 그걸 고려해야한다.
        List<Member> findMembers=memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            System.out.println("이미 존재하는 회원입니다.");
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    
    public Member findOne(Long id){
        return memberRepository.findById(id).get();
    }

    @Transactional
    public void update(Long id, String name) {
        Member member= memberRepository.findById(id).get();
        member.setName(name);
    }
}
