package com.example.Proj_Refactory.service;

import com.example.Proj_Refactory.domain.Member;
import com.example.Proj_Refactory.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//    public String checkPerson(){
//
//    }
}
