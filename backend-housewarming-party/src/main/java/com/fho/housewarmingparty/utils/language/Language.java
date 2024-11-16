package com.fho.housewarmingparty.utils.language;

import java.util.Locale;

import lombok.Getter;

@Getter
public enum Language {

    PT(new Locale("pt", "BR")),
    EN(Locale.US);

    private final Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }
}
