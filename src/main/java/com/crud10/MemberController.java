package com.crud10;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member/api")
public class MemberController {


    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiRespose<CreateMemberResponse>> createMember(@RequestBody CreateMemberRequest request) {
        CreateMemberResponse createMemberResponse = memberService.create(request);
        ApiRespose<CreateMemberResponse> apiRespose = new ApiRespose<>("성공", 200, createMemberResponse);
        return ResponseEntity.ok(apiRespose);

    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiRespose<FindMemberResponse>> findMember(@PathVariable Long memberId) {
        FindMemberResponse findMemberResponse = memberService.find(memberId);
        ApiRespose<FindMemberResponse> apiRespose = new ApiRespose<>("성공", 200, findMemberResponse);
        return ResponseEntity.ok(apiRespose);
    }


    @GetMapping
    public ResponseEntity<ApiRespose<FindAllMemberResponse>> findAllMember() {
        FindAllMemberResponse Response = memberService.findAll();
        ApiRespose<FindAllMemberResponse> apiRespose = new ApiRespose<>("성공", 200, Response);
        return ResponseEntity.ok(apiRespose);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiRespose<UpdateAllMemberResponse>> updateAllMember(@PathVariable Long memberId,
            @RequestBody UpdateMemberRequest request) {
        UpdateAllMemberResponse Response = memberService.update(memberId,request);
        ApiRespose<UpdateAllMemberResponse> apiRespose = new ApiRespose<>("성공", 200, Response);
        return ResponseEntity.ok(apiRespose);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiRespose<DeleteAllMemberResponse>> deleteMember(@PathVariable Long memberId) {
        DeleteAllMemberResponse delete = memberService.delete(memberId);
        ApiRespose<DeleteAllMemberResponse> apiRespose = new ApiRespose<>("성공", 200, delete);
        return ResponseEntity.ok(apiRespose);
    }
}
