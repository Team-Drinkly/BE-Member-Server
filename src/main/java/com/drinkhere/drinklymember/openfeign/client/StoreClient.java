package com.drinkhere.drinklymember.openfeign.client;

import com.drinkhere.drinklymember.openfeign.dto.response.CountFreeDrinkHistories;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "countFreeDrinkHistoryClient", url = "${store-service.url}")
public interface StoreClient {
    @GetMapping("/m/free-drink/client/{memberId}/{subscribeId}")
    CountFreeDrinkHistories getCountFreeDrinkHistoriesBySubscribeId(
            @PathVariable("memberId") Long memberId,
            @PathVariable("subscribeId") Long subscribeId
    );
}
