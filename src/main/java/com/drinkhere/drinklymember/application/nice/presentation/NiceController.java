package com.drinkhere.drinklymember.application.nice.presentation;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.nice.dto.response.CreateNiceApiRequestDataDto;
import com.drinkhere.drinklymember.nice.service.InitializeNiceUseCase;
import com.drinkhere.drinklymember.nice.service.NiceCallBackUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class NiceController {
    private final InitializeNiceUseCase initializeNiceUseCase;
    private final NiceCallBackUseCase niceCallBackUseCase;

    @GetMapping("/nice/{mid}")
    public ApplicationResponse<CreateNiceApiRequestDataDto> initNiceApi(
            @PathVariable("mid") Long memberId
    ) {
        return ApplicationResponse.ok(initializeNiceUseCase.initializeNiceApi(memberId));
    }

    @GetMapping("/nice/call-back")
    public ApplicationResponse<String> handleNiceCallBack(
            @RequestParam("mid") Long memberId,
            @RequestParam("token_version_id") String tokenVersionId,
            @RequestParam("enc_data") String encData,
            @RequestParam("integrity_value") String integrityValue
    ) {
        niceCallBackUseCase.processCallback(memberId, encData);
        return ApplicationResponse.created("call-back url 생성");
    }

}
