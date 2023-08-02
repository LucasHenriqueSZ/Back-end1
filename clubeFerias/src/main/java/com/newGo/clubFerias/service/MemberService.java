package com.newGo.clubFerias.service;

import com.newGo.clubFerias.controller.MemberController;
import com.newGo.clubFerias.data.dto.CreateMemberDto;
import com.newGo.clubFerias.data.dto.MemberDto;
import com.newGo.clubFerias.exceptions.InvalidMemberException;
import com.newGo.clubFerias.exceptions.MemberNotFoundException;
import com.newGo.clubFerias.mappers.ModelMapper;
import com.newGo.clubFerias.model.Member;
import com.newGo.clubFerias.repository.MemberRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MemberService {

    MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberDto> getAllMembers() {
        List<Member> members = memberRepository.findAllOrderedByDateAssociation();
        if (members.isEmpty())
            throw new MemberNotFoundException("No members found");

        List<MemberDto> memberDtos = ModelMapper.parseListObjects(members, MemberDto.class);
        memberDtos.forEach(memberDto -> addHatoasMemberDto(memberDto));

        return memberDtos;
    }

    public MemberDto createMember(CreateMemberDto createMemberDto) {
        if (createMemberDto == null)
            throw new InvalidMemberException("Member cannot be null");

        if (memberRepository.findByCpf(createMemberDto.getCpf()) != null)
            throw new InvalidMemberException("CPF already exists");

        Member member = ModelMapper.parseObject(createMemberDto, Member.class);
        member.setDateAssociation(LocalDate.now());

        validateMember(member);
        memberRepository.save(member);

        MemberDto memberDto = ModelMapper.parseObject(member, MemberDto.class);
        addHatoasMemberDto(memberDto);

        return memberDto;
    }

    public MemberDto getMemberForNameOrCpf(String nameOrCpf) {
        if (nameOrCpf == null || nameOrCpf.isEmpty())
            throw new MemberNotFoundException("Name or CPF cannot be null");

        Member member = memberRepository.findByCpfOrName(nameOrCpf);
        if (member == null)
            throw new MemberNotFoundException("Member not found");

        MemberDto memberDto = ModelMapper.parseObject(member, MemberDto.class);
        addHatoasMemberDto(memberDto);

        return memberDto;
    }

    public void deleteMember(String cardCode) {
        if (cardCode == null || cardCode.isEmpty())
            throw new MemberNotFoundException("Card code cannot be null");

        Member member = memberRepository.findByCardCode(cardCode);
        if (member == null)
            throw new MemberNotFoundException("Member not found");
        try {
            memberRepository.delete(member);
        } catch (Exception e) {
            throw new MemberNotFoundException(e.getMessage());
        }
    }

    public MemberDto updateMember(String cardCode, CreateMemberDto createMemberDto) {
        if (cardCode == null || cardCode.isEmpty())
            throw new InvalidMemberException("Card code cannot be null");

        if (memberRepository.findByCpf(createMemberDto.getCpf()) != null)
            throw new InvalidMemberException("CPF already exists");

        Member member = memberRepository.findByCardCode(cardCode);
        if (member == null)
            throw new InvalidMemberException("Member not found");


        Member memberUpdate = ModelMapper.parseObject(createMemberDto, Member.class);
        memberUpdate.setDateAssociation(member.getDateAssociation());
        memberUpdate.setCardCode(member.getCardCode());
        memberUpdate.setId(member.getId());

        validateMember(memberUpdate);
        memberRepository.save(memberUpdate);

        MemberDto memberDto = ModelMapper.parseObject(memberUpdate, MemberDto.class);
        addHatoasMemberDto(memberDto);

        return memberDto;
    }

    private void addHatoasMemberDto(MemberDto memberDto) {
        memberDto.add(linkTo(methodOn(MemberController.class).getMemeberForNameOrCpf(memberDto.getName())).withRel("get member for name"));
        memberDto.add(linkTo(methodOn(MemberController.class).getMemeberForNameOrCpf(memberDto.getCpf())).withRel("get member for cpf"));
        memberDto.add(linkTo(methodOn(MemberController.class).updateMember(memberDto.getCardCode(), null)).withRel("update"));
        memberDto.add(linkTo(methodOn(MemberController.class).deleteMember(memberDto.getCardCode())).withRel("delete"));
    }

    private void validateMember(Member memberUpdate) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Member>> violations = validator.validate(memberUpdate);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors: ");
            for (ConstraintViolation<Member> violation : violations) {
                errorMessage.append(violation.getMessage()).append(", ");
            }
            errorMessage.delete(errorMessage.length() - 2, errorMessage.length());
            throw new InvalidMemberException(errorMessage.toString());
        }
    }
}
