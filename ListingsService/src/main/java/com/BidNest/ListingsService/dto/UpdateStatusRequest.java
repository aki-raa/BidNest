package com.BidNest.ListingsService.dto;

import com.BidNest.ListingsService.Status;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    private Status status;
}