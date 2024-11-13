package com.mokura.mokura_api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResGetHistoryClientDto {
    public String device_name;
    public String start_date;
    public String end_date;
    public String duration;
}
