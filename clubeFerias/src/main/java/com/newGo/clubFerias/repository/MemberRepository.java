package com.newGo.clubFerias.repository;

import com.newGo.clubFerias.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m order by m.dateAssociation desc")
    List<Member> findAllOrderedByDateAssociation();

    @Query("select m from Member m where m.cpf = ?1")
    Member findByCpf(String cpf);

    @Query("select m from Member m where m.cardCode = ?1")
    Member findByCardCode(String cardCode);

    @Query("select m from Member m where m.name = ?1 or m.cpf = ?1")
    Member findByCpfOrName(String nameOrCpf);
}
