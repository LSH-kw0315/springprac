package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRepository {

    private static Map<Long,Member> store=new HashMap<>();
    //실무에서는 concurrentHashMap, AtomicLong을 사용하는 것을 고려
    private static long sequence=0L;

    private static final MemberRepository instance=new MemberRepository();

    public static MemberRepository getInstance(){
        return instance;
    }

    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(),member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
        //store를 보관하기 위함
    }

    public void clear(){
        store.clear();
    }

    private MemberRepository(){};
}
