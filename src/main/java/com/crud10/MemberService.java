package com.crud10;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public CreateMemberResponse create(CreateMemberRequest request) {

        Member member = new Member(request.getName());

        Member save = memberRepository.save(member);

        return CreateMemberResponse.of(save);
    }

    @Transactional
    public FindMemberResponse find(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("아이디 없음"));

        return FindMemberResponse.of(member);
    }

    @Transactional
    public FindAllMemberResponse findAll() {
        List<Member> all = memberRepository.findAll();
        FindAllMemberResponse findAllMemberResponse = new FindAllMemberResponse(all);
        return findAllMemberResponse;


    }


    @Transactional
    public UpdateAllMemberResponse update(Long memberId, UpdateMemberRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("아이디 없음"));

        member.update(member.getName());

        Member save = memberRepository.save(member);

        return new UpdateAllMemberResponse(
                save.getId(), save.getName());
    }

    public void delete(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("아이디 없음"));

        memberRepository.delete(member);
    }
}
