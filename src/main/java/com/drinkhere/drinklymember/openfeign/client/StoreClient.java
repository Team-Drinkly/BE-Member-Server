package com.drinkhere.drinklymember.openfeign.client;

import com.drinkhere.drinklymember.openfeign.dto.response.CountFreeDrinkHistories;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "countFreeDrinkHistoryClient", url = "${store-service.url}")
public interface StoreClient {
    @GetMapping("/{subscribeId}")
    CountFreeDrinkHistories getCountFreeDrinkHistoriesBySubscribeId(@PathVariable("subscribeId") Long subscribeId);
}
