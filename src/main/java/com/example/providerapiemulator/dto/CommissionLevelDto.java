package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.CommissionLevelName;

/**
 * Отдельной таблицы уровней комиссии в billing нет - это фиксированный набор значений
 * колонок providers.commissionlevel и payment."CommissionLevel".
 */
public record CommissionLevelDto(
        CommissionLevelName name,
        String description
) {}
