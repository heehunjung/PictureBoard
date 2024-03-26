package com.example.Proj_Refactory.service;

import com.example.Proj_Refactory.domain.Member;
import com.example.Proj_Refactory.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    MemberRepository memberRepository;
    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository =memberRepository;
    }
    public boolean checkUsername(String username){
        Optional<Member> foundMember = memberRepository.findByUsername(username);
        if (foundMember.isPresent()) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean checkPerson(String name, Long number) {
        List<Member> foundMembers = memberRepository.findByName(name);
        for (Member member : foundMembers) {
            if (member.getNumber().equals(number)) {
                return true; // 이름과 번호가 모두 일치하는 회원이 있으면 true 반환
            }
        }
        return false; // 일치하는 회원이 없으면 false 반환
    }

}
