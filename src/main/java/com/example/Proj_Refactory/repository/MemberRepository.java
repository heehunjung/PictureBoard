package com.example.Proj_Refactory.repository;

import com.example.Proj_Refactory.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>{
    List<Member> findAllByOrderByIdDesc();
}
