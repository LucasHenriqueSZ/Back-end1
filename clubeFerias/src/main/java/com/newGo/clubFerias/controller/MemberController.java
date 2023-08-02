package com.newGo.clubFerias.controller;

import com.newGo.clubFerias.data.dto.CreateMemberDto;
import com.newGo.clubFerias.data.dto.MemberDto;
import com.newGo.clubFerias.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
@Tag(name = "Member", description = "Endpoints for Managing Members")
public class MemberController {

    MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/all")
    @Operation(summary = "Find all Members", description = "Find all Members",
            tags = {"Member"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "Application/json", array = @ArraySchema(schema = @Schema(implementation = MemberDto.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
    })
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @PostMapping("/create")
    @Operation(summary = "Create a Member", description = "Create a Member",
            tags = {"Member"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "Application/json", schema = @Schema(implementation = MemberDto.class))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content)
    })
    public ResponseEntity<MemberDto> createMember(@RequestBody CreateMemberDto createMemberDto) {
        return ResponseEntity.ok(memberService.createMember(createMemberDto));
    }

    @GetMapping("{nameOrCpf}")
    @Operation(summary = "Find a Member by Name or CPF", description = "Find a Member by Name or CPF",
            tags = {"Member"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "Application/json", schema = @Schema(implementation = MemberDto.class))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
    })
    public ResponseEntity<MemberDto> getMemeberForNameOrCpf(@PathVariable(value = "nameOrCpf") String nameOrCpf) {
        return ResponseEntity.ok(memberService.getMemberForNameOrCpf(nameOrCpf));
    }

    @PutMapping("{cardCode}/update")
    @Operation(summary = "Update a Member", description = "Update a Member",
            tags = {"Member"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "Application/json", schema = @Schema(implementation = MemberDto.class))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
    })
    public ResponseEntity<MemberDto> updateMember(@PathVariable(value = "cardCode") String cardCode, @RequestBody CreateMemberDto memberDto) {
        return ResponseEntity.ok(memberService.updateMember(cardCode, memberDto));
    }

    @DeleteMapping("{cardCode}/delete")
    @Operation(summary = "Delete a Member", description = "Delete a Member",
            tags = {"Member"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
    })
    public ResponseEntity<Void> deleteMember(@PathVariable(value = "cardCode") String cardCode) {
        memberService.deleteMember(cardCode);
        return ResponseEntity.ok().build();
    }
}
