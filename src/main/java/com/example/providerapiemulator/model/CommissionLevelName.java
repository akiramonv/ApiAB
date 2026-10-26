package com.example.providerapiemulator.model;

/**
 * Уровень, с которого берётся комиссия. В БД billing отдельной таблицы нет,
 * значение хранится строкой в providers.commissionlevel и payment."CommissionLevel".
 */
public enum CommissionLevelName {
    ProviderLevel,
    AccountLevel,
    ServiceLevel
}
